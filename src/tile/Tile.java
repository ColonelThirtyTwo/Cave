
package tile;

import entities.Entity;
import structures.Vec2;

/**
 *
 * @author Colonel32
 */
public interface Tile //implements Serializable
{
	public void addedToWorld(World w, int x, int y);
	public void removedFromWorld(World w, int x, int y);
	public void getPos(Vec2 coord);
	public int getX();
	public int getY();

	/**
	 * Called when entity e's center enters the tile.
	 */
	public void entityEntered(Entity e);
	/**
	 * Called when entity e's center leaves the tile.
	 */
	public void entityExited(Entity e);

	public void draw(double x, double y, double w, double h);

	/**
	 * Is the tile animated? Can we draw it once onto a main image?
	 * (currently has no use)
	 */
	public boolean isAnimated();

	/**
	 * Is the tile transparent? Can monsters and players see through it?
	 */
	public boolean isTransparent();
	/**
	 * Is the tile solid? Can entities pass through it?
	 * @return
	 */
	public boolean isSolid();
}
