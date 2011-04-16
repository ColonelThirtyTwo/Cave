
package input;

import entities.types.Player;
import org.lwjgl.input.Keyboard;
import rendering.camera.Camera;
import structures.Vec2;

/**
 * Handles forwarding input to a player class.
 * @author Colonel32
 */
public class PlayerInputListener extends InputCallback
{
	public static int KEY_UP = Keyboard.KEY_W;
	public static int KEY_DOWN = Keyboard.KEY_S;
	public static int KEY_LEFT = Keyboard.KEY_A;
	public static int KEY_RIGHT = Keyboard.KEY_D;

	private Vec2 xform;
	protected Player ply;

	public PlayerInputListener(Player p)
	{
		ply = p;
		xform = new Vec2();
	}

	@Override
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

	@Override
	public void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel)
	{
		
	}

	@Override
	public void mouseButtonEvent(int x, int y, int button, boolean down)
	{
		Camera cam = ply.getWorld().getCamera();
		cam.screen2world(xform, x, y);
		if(button == 1)
			ply.mainUse(xform.x, xform.y, down);
		else if(button == 2)
			ply.altUse(xform.x, xform.y, down);
	}
	
}
