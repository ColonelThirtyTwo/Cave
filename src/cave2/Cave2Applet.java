
package cave2;

import cave2.structures.LogU;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Colonel 32
 */
public class Cave2Applet extends Applet
{
	private static final Logger log = LogU.getLogger();

	public Canvas canvas;
	private Thread gamethread;

	private void start_game()
	{
		final Cave2Applet t = this;
		gamethread = new Thread()
		{
			public void run()
			{
				Main.setApplet(t);
				Main.main(null);
			}
		};
		gamethread.start();
	}

	private void stop_game()
	{
		try
		{
			Main.stop();
			gamethread.join();
		}
		catch(InterruptedException e)
		{
			log.log(Level.SEVERE, "Someone interrupted the applet thread", e);
		}
	}

	public void init()
	{
		setLayout(new BorderLayout());
		try
		{
			canvas = new Canvas()
			{
				public final void addNotify()
				{
					super.addNotify();
					start_game();
				}
				public final void removeNotify()
				{
					super.removeNotify();
					stop_game();
				}
			};
			canvas.setSize(getWidth(), getHeight());
			add(canvas);
			canvas.setFocusable(true);
			canvas.requestFocus();
			canvas.setIgnoreRepaint(true);
			setVisible(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Error when intializing applet", e);
			throw new RuntimeException(e);
		}
	}

	public void start()
	{

	}

	public void stop()
	{

	}

	public void destroy()
	{
		remove(canvas);
		super.destroy();
	}
}
