
package cave2.rendering.hud;

import cave2.input.InputCallback;
import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * A HUD element. Draws after everything else, on top of everything else.
 * Can also swallow input.
 * @author Colonel32
 */
public abstract class HudElement
{
	private static Collection<HudElement> elements = new HashSet<HudElement>();

	public abstract void draw();
	public abstract void think(int timedelta);

	/** @return True to "swallow" the event, false to let it continue */
	public abstract boolean mouseClicked(int screenx, int screeny, boolean isDown);
	/** @return True to "swallow" the event, false to let it continue */
	public abstract boolean keyPressed(int key, boolean isDown);

	/**
	 * Enables rendering and processing of this element
	 */
	public void enable()
	{
		elements.add(this);
	}
	/**
	 * Disables rendering and processing of this element
	 */
	public void disable()
	{
		elements.remove(this);
	}

	public static void poll()
	{
		while(Keyboard.next())
		{
			boolean swallow = false;
			for(HudElement e : elements)
			{
				if(e.keyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState()))
				{
					swallow = true;
					break;
				}
			}
			if(!swallow) InputCallback.doKeyEvent();
		}

		while(Mouse.next())
		{
			if(Mouse.getEventButton() == -1)
			{
				InputCallback.doMouseEvent();
				continue;
			}

			boolean swallow = false;
			for(HudElement e : elements)
			{
				if(e.mouseClicked(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButtonState()))
				{
					swallow = true;
					break;
				}
			}
			if(!swallow) InputCallback.doMouseEvent();
		}
	}
}
