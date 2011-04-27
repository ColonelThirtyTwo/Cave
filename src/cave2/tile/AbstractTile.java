
package cave2.tile;

import cave2.structures.Vec2;

/**
 * Abstract tile class
 * @author Colonel32
 */
public abstract class AbstractTile implements Tile
{
	/**
	 * World that the tile was in, or the last world it was in if the tile is
	 * not in a world.
	 */
	protected World world;
	/**
	 * Coordinates of the tile.
	 */
	protected int x, y;
	/**
	 * True if the tile is in a world, false if it is not.
	 */
	protected boolean isInWorld;

	public void addedToWorld(World w, int x, int y)
	{
		world = w;
		this.x = x;
		this.y = y;
		isInWorld = true;
	}

	public void removedFromWorld(World w, int x, int y)
	{
		assert(w == world && x == this.x && y == this.y);
		isInWorld = false;
	}

	public World getWorld() { return world; }

	public int getX() { return x; }
	public int getY() { return y; }
	public void getPos(Vec2 v) { v.x = x; v.y = y; }
}
