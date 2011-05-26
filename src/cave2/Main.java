
package cave2;

import cave2.entities.*;
import cave2.entities.types.*;
import cave2.entities.types.items.*;
import cave2.input.InputCallback;
import cave2.input.MenuListener;
import cave2.input.PlayerInputListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import cave2.structures.exceptions.RenderError;
import cave2.rendering.RenderUtil;
import cave2.rendering.camera.*;
import cave2.rendering.hud.HudElement;
import cave2.rendering.hud.InventoryBar;
import cave2.structures.AABB;
import cave2.structures.GeneratorThread;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.structures.exceptions.QuitException;
import cave2.structures.pathfinding.PathFinder;
import cave2.tile.World;
import cave2.tile.generators.*;
import cave2.tile.types.DiamondWall;
import java.awt.Canvas;

/**
 *
 * @author Colonel32
 */
public class Main
{
	private static final Logger log = LogU.getLogger();
	static { log.setLevel(Level.FINE); }

	private static long lastFrame;
	private static World world;
	private static Camera cam;
	private static boolean requestExit = false;

	private static Cave2Applet applet;
	static void setApplet(Cave2Applet app)
	{
		applet = app;
	}
	
	public static void stop()
	{
		requestExit = true;
	}

    public static void main(String[] args)
	{
		try
		{
			setup_initial();
			while(mainLoop()) {}
		}
		catch(QuitException e)
		{
			log.log(Level.FINE, "Program terminating due to QuitException.", e);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE,"Terminating due to exception", e);
		}
		finally
		{
			log.log(Level.INFO,"Main loop exited, deinitializing.");
			GeneratorThread.deinit();
			PathFinder.deinit();
			ResourceManager.unloadAll();
			RenderUtil.destroy();
		}
    }

	private static void setup_initial()
	{
		if(applet == null)
		{
			if(!setup_renderer()) return;
		}
		else
		{
			if(!setup_render_applet(applet.canvas)) return;
		}
		restart(new Random());
	}
	
	static boolean setup_renderer()
	{
		DisplayMode mode = getDisplayMode();
		log.log(Level.INFO, "Attempting to set LWJGL display to {0}x{1}x{2}", new Object[]{mode.getWidth(), mode.getHeight(), mode.getBitsPerPixel()});
        try
		{
			RenderUtil.initialize(mode, true);
		}
		catch(RenderError err)
		{
			log.log(Level.SEVERE, "Could not initialize LWJGL: {0}", err.getMessage());
			return false;
		}
		log.log(Level.INFO, "LWJGL Initialized sucessfully.");
		return true;
	}

	static boolean setup_render_applet(Canvas canvas)
	{
		log.log(Level.INFO, "Attempting to set LWJGL display to applet canvas");
		try
		{
			RenderUtil.initialize_applet(canvas);
		}
		catch(RenderError err)
		{
			log.log(Level.SEVERE, "Could not initialize LWJGL: {0}", err.getMessage());
			return false;
		}
		log.log(Level.INFO, "LWJGL Initialized sucessfully.");
		return true;
	}

	public static void restart(Random random)
	{
		log.log(Level.INFO,"(Re)Initializing world");

		GeneratorThread.deinit();
		HudElement.reset();
		OrePerlinGenerator.reset();
		InputCallback.reset();
		PathFinder.deinit();

		DiamondWall.addToOreGenerator(random.nextInt());

		Player e = new Player(5,5);
		Zombie.setPlayer(e);
		
		cam = new EntFollowCamera(e,RenderUtil.width()/40,RenderUtil.height()/40);
		world = new World(new OrePerlinGenerator(0.6,random.nextInt()), cam);
		cam.setWorld(world);

		GeneratorThread.init(2);
		PathFinder.init(3);
		AABB initialWorld = new AABB(0,0,200,200);
		world.generateRegion(initialWorld);
		try
		{
			while(GeneratorThread.hasItems())
				Thread.sleep(100);
		}
		catch(InterruptedException err)
		{
			log.log(Level.WARNING, "Someone tried to interrupt the main thread. Jerks.");
		}

		Entity test = new DrillItem(5,5);
		Entity test2 = new Gun(5,5);
		Entity zombiespawner = new Zombie.AmbientSpawner();
		world.addEntity(e);
		world.addEntity(test);
		world.addEntity(test2);
		world.addEntity(zombiespawner);

		(new InventoryBar(e)).enable();
		(new PlayerInputListener(e)).enable();
		(new MenuListener(e)).enable();

		lastFrame = System.currentTimeMillis();
		log.log(Level.INFO,"Finished initialising world.");
	}

	private static boolean mainLoop()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) return false;
		if(Display.isCloseRequested()) return false;
		if(requestExit) return false;

		long thisFrame = System.currentTimeMillis();
		int delta = (int)(thisFrame - lastFrame);

		HudElement.poll();
		HudElement.doThink(delta);
		world.think(delta);
		cam.draw();
		HudElement.doDraw();

		double fps = 1000.0 / delta;
		Font f = ResourceManager.getFont("monospace");
		f.drawString(0, 0, String.format("FPS: %.3f", fps), Color.yellow);

		RenderUtil.update();
		//Display.sync(60);

		lastFrame = thisFrame;
		return true;
	}

	private static DisplayMode getDisplayMode()
	{
		DisplayMode[] modes;
		try
		{
			modes = Display.getAvailableDisplayModes();
		}
		catch(LWJGLException err)
		{
			throw new RuntimeException("Could not get display modes from LWJGL.", err);
		}

		DisplayMode max = modes[0];
		for(int i=1; i<modes.length; i++)
			if(modes[i].isFullscreenCapable() && modes[i].getBitsPerPixel() >= 24 &&
					modes[i].getWidth() > max.getWidth())
				max = modes[i];
		return max;
	}

	private static boolean verifySecurity()
	{
		try
		{
			SecurityManager m = System.getSecurityManager();
			if(m == null) return true;
			m.checkRead("res/");
		}
		catch(SecurityException e)
		{
			log.log(Level.SEVERE, "A security manager is running. Please disable it.", e);
			return false;
		}
		return true;
	}

	/** Shut up netbeans... */
	private Main() {}
}
