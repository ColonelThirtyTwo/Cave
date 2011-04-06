
package cave2;

import input.InputCallback;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import rendering.RenderError;
import rendering.RenderUtil;
import rendering.camera.*;
import structures.ResourceManager;
import tile.World;
import tile.generators.*;

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

	private static List<InputCallback> inputCallbacks;

    public static void main(String[] args)
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
		world = new World(new TestGenerator());
		cam = new StaticCamera(world,0,0,RenderUtil.width()/20,RenderUtil.height()/20);
		inputCallbacks = new ArrayList<InputCallback>();


		log.log(Level.INFO,"Finished initialising, entering main loop.");
		try
		{
			while(mainLoop()) {}
		}
		catch(RenderError err)
		{
			log.log(Level.SEVERE, "Error: {0}", err.getMessage());
		}

		ResourceManager.getInstance().releaseAll();
		RenderUtil.destroy();
    }

	private static boolean mainLoop()
	{
		long thisFrame = System.currentTimeMillis();
		int delta = (int)(thisFrame - lastFrame);

		doEventHandler();
		world.think(delta);
		cam.draw();
		RenderUtil.update();
		Display.sync(60);

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) return false;
		if(Display.isCloseRequested()) return false;
		return true;
	}

	private static void doEventHandler()
	{
		while(Keyboard.next())
		{
			for(int i=0; i<inputCallbacks.size(); i++)
				inputCallbacks.get(i).keyEvent(Keyboard.getEventKey(), Keyboard.getEventKeyState());
		}
		while(Mouse.next())
		{
			if(Mouse.getEventButton() != -1)
				for(int i=0; i<inputCallbacks.size(); i++)
					inputCallbacks.get(i).mouseButtonEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton(), Mouse.getEventButtonState());
			if(Mouse.getEventDWheel() != 0 || Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0)
				for(int i=0; i<inputCallbacks.size(); i++)
					inputCallbacks.get(i).mouseMovedEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventDWheel());
		}
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

	private Main() {
	}
}
