
package entities.types;

import entities.CollisionEntity;
//import input.InputCallback;
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

	public Player(double x, double y)
	{
		super(x,y,.5,.5);
		speed = 5.0;
		dirx = diry = 0;
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
		RenderUtil.drawImage(img, box, 1.0);
	}

	/**
	 * Sets the player x movement velocity.
	 * 1  = move to right at normal speed
	 * 0  = stop
	 * -1 = move to left at normal speed
	 */
	public void setMovementX(int x) { dirx = x; }
	/**
	 * Sets the player y movement velocity.
	 * 1  = move downward at normal speed
	 * 0  = stop
	 * -1 = move upward at normal speed
	 */
	public void setMovementY(int y) { diry = y; }
	/**
	 * Main weapon/tool fire
	 * @param x X position in world coordinates
	 * @param y Y position in world coordinates
	 * @param isdown true = start use, false = end use.
	 */
	public void mainUse(double x, double y, boolean isdown)
	{}
	/**
	 * Same as mainUse, except triggers the weapon/tool's alt fire.
	 * @param x X position in world coordinates
	 * @param y Y position in world coordinates
	 * @param isdown true = start use, false = end use.
	 */
	public void altUse(double x, double y, boolean isdown)
	{}
}
