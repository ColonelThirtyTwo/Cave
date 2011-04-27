
package cave2.input;

import cave2.entities.types.Player;
import org.lwjgl.input.Keyboard;
import cave2.rendering.camera.Camera;
import cave2.structures.LogU;
import cave2.structures.Vec2;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles forwarding input to a player class.
 * @author Colonel32
 */
public class PlayerInputListener extends InputCallback
{
	private static final Logger log = LogU.getLogger();

	public static int KEY_UP = Keyboard.KEY_W;
	public static int KEY_DOWN = Keyboard.KEY_S;
	public static int KEY_LEFT = Keyboard.KEY_A;
	public static int KEY_RIGHT = Keyboard.KEY_D;

	public static int BUTTON_MAINUSE = 0;
	public static int BUTTON_ALTUSE = 1;

	private Vec2 xform;
	protected Player ply;

	public PlayerInputListener(Player p)
	{
		ply = p;
		xform = new Vec2();
	}

	public void keyEvent(int key, boolean down)
	{
		if(key == KEY_UP)
			ply.setMovementY(down ? -1 : 0);
		else if(key == KEY_DOWN)
			ply.setMovementY(down ? 1 : 0);
		else if(key == KEY_RIGHT)
			ply.setMovementX(down ? 1 : 0);
		else if(key == KEY_LEFT)
			ply.setMovementX(down ? -1 : 0);
	}

	public void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel)
	{
		if(dwheel != 0)
		{
			log.log(Level.FINE, "Mouse Wheel Event, dwheel = {0}", dwheel);
			ply.setEquippedSlot(ply.getEquippedSlot() - dwheel / 120);
		}
	}

	public void mouseButtonEvent(int x, int y, int button, boolean down)
	{
		Camera cam = ply.getWorld().getCamera();
		cam.screen2world(xform, x, y);

		// LWJGL is <sarcasm>cool</sarcasm> in that 1 = LMB and 2 = RMB...
		if(button == BUTTON_MAINUSE)
			ply.mainUse(xform.x, xform.y, down);
		else if(button == BUTTON_ALTUSE)
			ply.altUse(xform.x, xform.y, down);
	}
	
}
