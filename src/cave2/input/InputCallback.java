
package cave2.input;

import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Callback for input events (key + mouse).
 * TODO: Write projected callbacks (screen2world)
 * @author Colonel32
 */
public abstract class InputCallback
{
	public abstract void keyEvent(int key, boolean down);
	public abstract void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel);
	public abstract void mouseButtonEvent(int x, int y, int button, boolean down);
	
	public void enable()
	{
		callbackList.add(this);
	}
	public void disable()
	{
		callbackList.remove(this);
	}

	private static List<InputCallback> callbackList = new LinkedList<InputCallback>();

	public static void doKeyEvent()
	{
		for(int i=0; i<callbackList.size(); i++)
			callbackList.get(i).keyEvent(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}
	public static void doMouseEvent()
	{
		if(Mouse.getEventButton() != -1)
			for(int i=0; i<callbackList.size(); i++)
				callbackList.get(i).mouseButtonEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButton(), Mouse.getEventButtonState());
		if(Mouse.getEventDWheel() != 0 || Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0)
			for(int i=0; i<callbackList.size(); i++)
				callbackList.get(i).mouseMovedEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventDWheel());
	}

	public static void reset()
	{
		callbackList.clear();
	}
}
