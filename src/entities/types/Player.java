
package entities.types;

import entities.CollisionEntity;
import input.InputCallback;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import rendering.RenderUtil;
import structures.AABB;
import structures.ResourceManager;

/**
 * Player. Moves with keyboard input.
 * @author Colonel32
 */
public class Player extends CollisionEntity
{
	private class PlyInput extends InputCallback
	{
		public void keyEvent(int key, boolean isdown)
		{
			if(key == KEY_UP)
				diry = isdown ? -1 : 0;
			else if(key == KEY_DOWN)
				diry = isdown ? 1 : 0;
			else if(key == KEY_RIGHT)
				dirx = isdown ? 1 : 0;
			else if(key == KEY_LEFT)
				dirx = isdown ? -1 : 0;
		}

		public void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel)
		{

		}

		public void mouseButtonEvent(int x, int y, int button, boolean down)
		{

		}
//		private void fix()
//		{
//			if(dirx >  1) dirx = 1;
//			if(dirx < -1) dirx = -1;
//			if(diry >  1) diry = 1;
//			if(diry < -1) diry = -1;
//		}
	}

	public static int KEY_UP = Keyboard.KEY_W;
	public static int KEY_DOWN = Keyboard.KEY_S;
	public static int KEY_LEFT = Keyboard.KEY_A;
	public static int KEY_RIGHT = Keyboard.KEY_D;

	/**
	 * Speed, in tiles per second.
	 */
	protected double speed;

	/**
	 * Direction that entity should move on the x-axis.
	 */
	protected int dirx;
	/**
	 * Direction that entity should move on the y-axis.
	 */
	protected int diry;

	private InputCallback plyInputCallback;

	public Player(double x, double y)
	{
		super(x,y,.5,.5);
		speed = 5.0;
		dirx = diry = 0;
		plyInputCallback = new PlyInput();
		plyInputCallback.enable();
	}

	public void collidedWith(CollisionEntity e) {}

	public void think(int delta)
	{
		double step = delta / 1000.0 * speed;

		box.center.x += dirx * step;
		box.center.y += diry * step;

		doTileCollisions();
		doTileCallbacks();
	}

	public void draw(AABB abox)
	{
		if(!abox.overlaps(box)) return;
		
		Texture img = ResourceManager.getInstance().getImage("entities/player.png");
		//RenderUtil.drawImage(img, box.center.x - box.size.x, box.center.y - box.size.y, box.size.x*2, box.size.y*2);
		RenderUtil.drawImage(img, box, 1.0);
	}

	
}
