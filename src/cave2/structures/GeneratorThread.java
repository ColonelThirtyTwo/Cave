
package cave2.structures;

import cave2.tile.Chunk;
import cave2.tile.Generator;
import cave2.tile.World;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Generates terrain asynchronously, cause libnoise is slow and stuff.
 * @author Colonel 32
 */
public class GeneratorThread extends Thread
{
	private static class Task
	{
		public Task(Generator gen, Chunk chunk, World w)
		{
			this.gen = gen;
			this.w = w;
			c = chunk;
		}
		Generator gen;
		Chunk c;
		World w;
	}

	private static ThreadGroup group = new ThreadGroup("GeneratorThreadGroup");
	private static BlockingQueue<Task> taskQueue;

	/**
	 * Adds c to the queue of chunks needing to be processed.
	 * @param g Generator to use. generate() Must be thread-safe.
	 */
	public static void add(World w, Chunk c, Generator g)
	{
		taskQueue.add(new Task(g,c,w));
	}

	/**
	 * Creates the generator threads.
	 * @param threads Number of threads to generate.
	 */
	public static void init(int threads)
	{
		taskQueue = new LinkedBlockingQueue<Task>();
		for(int i=0; i<threads; i++)
		{
			Thread t = new GeneratorThread(group,i);
			t.start();
		}
	}

	public static void deinit()
	{
		group.interrupt();
	}

	private GeneratorThread(ThreadGroup group, int threadno)
	{
		super(group,"GeneratorThread"+threadno);
	}

	public void run()
	{
		try
		{
			while(true)
			{
				Task t = taskQueue.take();
				assert(!t.c.isGenerated);
				t.gen.generate(t.w, t.c, t.c.x, t.c.y);
			}
		}
		catch(InterruptedException e)
		{
			return;
		}
		
	}
}
