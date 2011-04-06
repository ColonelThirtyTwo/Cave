
package entities;

import structures.AABB;
import tile.World;

/**
 * Abstract base for entities that need collision detection
 * @author Colonel32
 */
public abstract class CollisionEntity implements Entity
{
	protected AABB box;
	protected int lastX, lastY;
	protected World world;

	/**
	 * Creates a new CollisionEntity and initializes its AABB.
	 * @param x Initial x position
	 * @param y Initial y position
	 * @param w Half width
	 * @param h Half height
	 */
	protected CollisionEntity(double x, double y, double w, double h)
	{
		box = new AABB(x,y,w,h);
		
		// Hopefully someone won't put an entity here >_>
		lastX = Integer.MIN_VALUE;
		lastY = Integer.MAX_VALUE;
	}

	public double getX() { return box.center.x; }
	public double getY() { return box.center.y; }
	public AABB getAABB() { return box; }

	public void think()
	{
		int gx = (int)(Math.floor(box.center.x));
		int gy = (int)(Math.floor(box.center.y));
		if(gx != lastX || gy != lastY)
		{
			world.getTileAt(lastX, lastY).entityExited(this);
			world.getTileAt(gx, gy).entityEntered(this);
			lastX = gx;
			lastY = gy;
		}
	}

	/**
	 * Called every frame that this entity collides with e.
	 */
	public abstract void collidedWith(CollisionEntity e);

	public void addedToWorld(World w) { world = w; }
	public void removedFromWorld(World w)
	{
		assert(w == world);
		world = null;
	}
}