
package cave2;

import cave2.entities.*;
import cave2.entities.types.*;
import cave2.entities.types.items.*;
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
import cave2.structures.GeneratorThread;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.structures.exceptions.QuitException;
import cave2.tile.World;
import cave2.tile.generators.*;

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

    public static void main(String[] args)
	{
		try
		{
			setup();
		}
		finally
		{
			log.log(Level.INFO,"Main loop exited, deinitializing.");
			GeneratorThread.deinit();
			ResourceManager.getInstance().unloadAll();
			RenderUtil.destroy();
		}
    }

	private static void setup()
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
			return;
		}
		log.log(Level.INFO, "LWJGL Initialized sucessfully.");

		lastFrame = System.currentTimeMillis();

		log.log(Level.INFO, "Intitializing world...");
		GeneratorThread.init(2);

		Player e = new Player(5,5);
		cam = new EntFollowCamera(e,RenderUtil.width()/40,RenderUtil.height()/40);

		world = new World(new SimplePerlinGenerator(0.5,(new Random()).nextInt()), cam);
		world.addEntity(e);

		Entity test = new DrillItem(3,3);
		world.addEntity(test);

		(new InventoryBar(e)).enable();
		(new PlayerInputListener(e)).enable();

		log.log(Level.INFO,"Finished initialising, entering main loop.");
		try
		{
			while(mainLoop()) {}
		}
		catch(RenderError err)
		{
			log.log(Level.SEVERE, "Error: {0}", err.getMessage());
		}
		catch(QuitException exc)
		{
			log.log(Level.FINE, "Program terminating due to QuitException.", exc);
		}
	}

	private static boolean mainLoop()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) return false;
		if(Display.isCloseRequested()) return false;

		long thisFrame = System.currentTimeMillis();
		int delta = (int)(thisFrame - lastFrame);

		HudElement.poll();
		HudElement.doThink(delta);
		world.think(delta);
		cam.draw();
		HudElement.doDraw();

		double fps = 1000.0 / delta;
		Font f = ResourceManager.getInstance().getFont("monospace");
		f.drawString(0, 0, String.format("FPS: %.3f", fps), Color.yellow);

		RenderUtil.update();
		Display.sync(60);

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
			log.log(Level.SEVERE, "A security manager is running. Please disable it.");
			return false;
		}
		return true;
	}

	/** Shut up netbeans... */
	private Main() {}
}
