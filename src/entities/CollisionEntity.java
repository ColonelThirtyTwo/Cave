
package entities;

import structures.AABB;
import structures.Vec2;
import tile.Tile;
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
	
	/**
	 * @return The x-coordinate of the tile the entity's center is on.
	 */
	public int getTileX() { return (int)Math.floor(box.center.x); }
	/**
	 * @return The y-coordinate of the tile the entity's center is on.
	 */
	public int getTileY() { return (int)Math.floor(box.center.y); }

	/**
	 * Does tile enter/exit callbacks. Should be called by subclasses.
	 */
	protected void doTileCallbacks()
	{
		int gx = getTileX();
		int gy = getTileY();
		if(gx != lastX || gy != lastY)
		{
			Tile last = world.getTileAt(lastX, lastY);
			Tile now = world.getTileAt(gx,gy);
			if(last != null) last.entityExited(this);
			if(now != null) now.entityEntered(this);
			lastX = gx;
			lastY = gy;
		}
	}

	/**
	 * Tile collision algorithm. Moves the entity out of a tile it is colliding with.
	 */
	protected void doTileCollisions()
	{
		Tile[] adj = new Tile[9];
		world.getAdjacent(adj, getTileX(), getTileY());
		adj[8] = world.getTileAt(getTileX(), getTileY());

		AABB tilebox = new AABB(0,0,0.5,0.5);
		for(int i=0; i<adj.length; i++)
		{
			if(adj[i] == null || !adj[i].isSolid()) continue;

			tilebox.center.x = adj[i].getX()+0.5;
			tilebox.center.y = adj[i].getY()+0.5;
			if(!tilebox.overlaps(box)) continue;

			Vec2 overlap = tilebox.getOverlap(box);
			box.center.addInto(overlap);
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