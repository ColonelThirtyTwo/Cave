
package cave2;

import cave2.entities.Entity;
import cave2.entities.types.Player;
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
import cave2.rendering.RenderError;
import cave2.rendering.RenderUtil;
import cave2.rendering.camera.*;
import cave2.rendering.hud.HudElement;
import cave2.structures.GeneratorThread;
import cave2.structures.ResourceManager;
import cave2.tile.World;
import cave2.tile.generators.*;

/**
 *
 * @author Colonel32
 */
public class Main
{
	private static final Logger log = Logger.getLogger("cave2");

	private static long lastFrame;
	private static World world;
	private static Camera cam;

	private static double fps_smoother;

    public static void main(String[] args)
	{
		log.setLevel(Level.FINE);

		fps_smoother = 0;

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

		//cam = new StaticCamera(world,0,0,RenderUtil.width()/40,RenderUtil.height()/40);
		Entity e = new Player(5,5);
		cam = new EntFollowCamera(e,RenderUtil.width()/40,RenderUtil.height()/40);

		world = new World(new SimplePerlinGenerator(0.5,(new Random()).nextInt()), cam);
		world.addEntity(e);

		(new PlayerInputListener((Player)e)).enable();

		log.log(Level.INFO,"Finished initialising, entering main loop.");
		try
		{
			while(mainLoop()) {}
		}
		catch(RenderError err)
		{
			log.log(Level.SEVERE, "Error: {0}", err.getMessage());
		}

		ResourceManager.getInstance().unloadAll();
		RenderUtil.destroy();
    }

	private static boolean mainLoop()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) return false;
		if(Display.isCloseRequested()) return false;

		long thisFrame = System.currentTimeMillis();
		int delta = (int)(thisFrame - lastFrame);

		HudElement.poll();
		world.think(delta);
		cam.draw();

		double fps = 1000.0 / delta;
		//fps_smoother = (fps_smoother * 0.99) + (fps * 0.01);
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
		catch(LWJGLException err) { throw new RuntimeException(err.getMessage()); }

		DisplayMode max = modes[0];
		for(int i=1; i<modes.length; i++)
			if(modes[i].isFullscreenCapable() && modes[i].getBitsPerPixel() >= 24 &&
					modes[i].getWidth() > max.getWidth())
				max = modes[i];
		return max;
	}

	/** Shut up netbeans... */
	private Main() {}
}
