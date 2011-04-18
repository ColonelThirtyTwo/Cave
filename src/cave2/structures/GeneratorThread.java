
package cave2.structures;

import cave2.tile.Chunk;
import cave2.tile.Generator;
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
		public Task(Generator gen, Chunk chunk)
		{
			this.gen = gen;
			c = chunk;
		}
		Generator gen;
		Chunk c;
	}

	private static ThreadGroup group = new ThreadGroup("GeneratorThreadGroup");
	private static BlockingQueue<Task> taskQueue;

	/**
	 * Adds c to the queue of chunks needing to be processed.
	 * @param g Generator to use. generate() Must be thread-safe.
	 */
	public static void add(Chunk c, Generator g)
	{
		taskQueue.add(new Task(g,c));
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

	public GeneratorThread(ThreadGroup group, int threadno)
	{
		super(group,"GeneratorThread"+threadno);
	}

	public void run()
	{
		try
		{
			Task t = null;
			while(t == null)
		}
		catch(InterruptedException e)
		{
			return;
		}
		
	}
}
