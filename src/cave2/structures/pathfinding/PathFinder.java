
package cave2.structures.pathfinding;

import cave2.structures.LogU;
import cave2.structures.exceptions.QuitException;
import cave2.tile.Tile;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread group to manage path finding.
 * @author Colonel 32
 */
public class PathFinder
{
	private static final Logger log = LogU.getLogger();

	private static class FuturePathTask implements Future<List<Tile>>
	{
		List<Tile> path;
		PathConfig cfg;
		private final Object notifier;
		private boolean isDone;
		private boolean isCanceled;
		private Exception error;
		private Thread thread;

		FuturePathTask(PathConfig cfg)
		{
			path = null;
			this.cfg = cfg;
			notifier = new Object();
			isDone = isCanceled = false;
			error = null;
			thread = null;
		}


		private boolean assign(Thread t)
		{
			if(isCanceled) return false;
			thread = t;
			return true;
		}

		private void complete(Exception e)
		{
			synchronized(notifier)
			{
				notifier.notifyAll();
				isDone = true;
				error = e;
			}
		}

		public boolean cancel(boolean mayInterruptIfRunning)
		{
			synchronized(notifier)
			{
				if(isDone) return false;
				if(mayInterruptIfRunning && thread != null) thread.interrupt();
				isCanceled = isDone = true;
				notifier.notifyAll();
				return true;
			}
		}

		public boolean isCancelled()
		{
			assert(!isCanceled || (isDone && isCanceled));
			return isCanceled;
		}

		public boolean isDone()
		{
			return isDone;
		}

		public List<Tile> get() throws InterruptedException, ExecutionException
		{
			synchronized(notifier)
			{
				if(isDone)
				{
					if(error != null) throw new ExecutionException(error);
					if(isCanceled) throw new CancellationException();
					return path;
				}
				else
				{
					notifier.wait();
					return get();
				}
			}
		}

		public List<Tile> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
		{
			if(isDone)
			{
				if(error != null) throw new ExecutionException(error);
				if(isCanceled) throw new CancellationException();
				return path;
			}
			else
			{
				unit.timedWait(notifier, timeout);
				return get();
			}
		}

	}
	
	private static class PathThread extends Thread
	{
		private class CostComparator implements Comparator<Tile>
		{
			public int compare(Tile o1, Tile o2)
			{
				return Double.compare(t_score.get(o1), t_score.get(o2));
			}
		}

		private NavigableSet<Tile> openList;
		private Set<Tile> closedList;

		private Map<Tile,Double> t_score;
		private Map<Tile,Double> g_score;
		private Map<Tile,Double> h_score;
		private Map<Tile,Tile> parents;

		private Tile[] adjbuffer;
	 
		PathThread(int num)
		{
			super(group,"PathFinder"+num);
			openList = new TreeSet<Tile>(new CostComparator());
			closedList = new HashSet<Tile>();
			t_score = new HashMap<Tile,Double>();
			g_score = new HashMap<Tile,Double>();
			h_score = new HashMap<Tile,Double>();
			parents = new HashMap<Tile,Tile>();
			adjbuffer = new Tile[8];
		}

		private List<Tile> reconstruct_path(Tile t)
		{
			List<Tile> p = new LinkedList<Tile>();
			while(t != null)
			{
				p.add(0,t);
				t = parents.get(t);
			}
			return p;
		}

		private List<Tile> astar(PathConfig cfg) throws InterruptedException
		{
			Tile start = cfg.getStart();
			t_score.put(start, 0.0);
			h_score.put(start, 0.0);
			g_score.put(start, 0.0);
			parents.put(start, null);
			openList.add(start);

			while(!openList.isEmpty() && !isInterrupted())
			{
				Tile t = openList.pollFirst();
				if(t == cfg.getEnd())
					return reconstruct_path(t);

				closedList.add(t);
				cfg.getWorld().getAdjacent(adjbuffer, t.getX(), t.getY());
				for(int i=0; i<8; i++)
				{
					Tile y = adjbuffer[i];
					if(closedList.contains(y))
						continue;

					double tentative_g_score = g_score.get(t) + cfg.getMovementCost(t, adjbuffer, i);
					boolean tentative_is_better = false;
					boolean add_to_openlist = false;

					if(!g_score.containsKey(y) && !closedList.contains(y))
					{
						// Not in any list, add to open list
						add_to_openlist = true;
						tentative_is_better = true;
					}
					else if(tentative_g_score < g_score.get(y))
						tentative_is_better = true; // Better route

					if(tentative_is_better)
					{
						parents.put(y, t);
						g_score.put(y, tentative_g_score);
						h_score.put(y, cfg.getHeuristic(t, adjbuffer, i));
						t_score.put(y, tentative_g_score + h_score.get(y));
						if(add_to_openlist)
							openList.add(y);
					}
				}
			}
			if(isInterrupted())
			{
				sleep(1); // Force an exception
				throw new RuntimeException("isInterrupted() == true but sleep(1) did not thow an exception!");
			}
			else return null;
		}

		public void run()
		{
			while(!doquit)
			{
				try
				{
					FuturePathTask task = queue.take();
					if(!task.assign(this)) continue;
					
					PathConfig cfg = task.cfg;
					task.path = astar(cfg);
					task.complete(null);
				}
				catch(InterruptedException e)
				{
					if(doquit) return;
					// else do nothing; we already poped the task.
				}
				catch(Exception e)
				{
					log.log(Level.SEVERE, "Error in pathfinding thread.", e);
					error = new QuitException("Error in pathfinding thread.");
				}
				finally
				{
					openList.clear();
					closedList.clear();
					t_score.clear();
					g_score.clear();
					h_score.clear();
					parents.clear();
				}
			}
		}
	}

	private static ThreadGroup group = new ThreadGroup("Pathfinding");
	private static BlockingQueue<FuturePathTask> queue = new LinkedBlockingQueue<FuturePathTask>();
	private static boolean doquit = true;
	private static RuntimeException error = null;

	public static void init(int threads)
	{
		if(!doquit)
			return;

		doquit = false;
		for(int i=0; i<threads; i++)
		{
			Thread t = new PathThread(i);
			t.start();
		}
	}

	public static void deinit()
	{
		if(doquit)
			return;
		doquit = true;
		group.interrupt();
		queue.clear();
	}

	public static Future<List<Tile>> queuePath(PathConfig cfg)
	{
		if(error != null) throw error;
		FuturePathTask t = new FuturePathTask(cfg);
		queue.add(t);
		return t;
	}

}
