
package cave2.structures.pathfinding;

import cave2.structures.LogU;
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

	private static class FuturePathTask implements Future<Path>
	{
		Path path;
		PathConfig cfg;
		private Object notifier;
		private boolean isDone;
		private boolean isCanceled;
		private Exception error;
		private Thread thread;

		FuturePathTask(PathConfig cfg)
		{
			path = new Path();
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
			isDone = true;
			error = e;
			notifier.notifyAll();
		}

		public boolean cancel(boolean mayInterruptIfRunning)
		{
			if(isDone) return false;
			if(mayInterruptIfRunning && thread != null) thread.interrupt();
			isCanceled = isDone = true;
			notifier.notifyAll();
			return true;
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

		public Path get() throws InterruptedException, ExecutionException
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

		public Path get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
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
		public PathThread(int num)
		{
			super(group,"PathFinder"+num);
		}
		
		public void run()
		{
			while(!doquit)
			{
				try
				{
					FuturePathTask task = queue.take();
				}
				catch(InterruptedException e)
				{
					if(doquit) return;
					// else do nothing; we already poped the task.
				}
			}
		}
	}

	private static final double sqrt_2 = Math.sqrt(2);
	private static ThreadGroup group = new ThreadGroup("Pathfinding");
	private static BlockingQueue<FuturePathTask> queue = new LinkedBlockingQueue<FuturePathTask>();
	private static boolean doquit = true;

	public static void init(int threads)
	{
		if(!doquit)
		{
			log.log(Level.WARNING, "Attempted to initialize path finder threads, but was already initialized.");
			return;
		}

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
		{
			log.log(Level.WARNING, "Attempted to deinitialize path finder threads, but not initialized.");
			return;
		}
		doquit = true;
		group.interrupt();
		queue.clear();
	}

	public static Future<Path> queuePath(PathConfig cfg)
	{
		FuturePathTask t = new FuturePathTask(cfg);
		queue.add(t);
		return t;
	}

}
