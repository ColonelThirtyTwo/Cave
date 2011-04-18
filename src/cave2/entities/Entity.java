
package cave2.entities;

import cave2.structures.AABB;
import cave2.tile.World;

/**
 * Entity interface
 * @author Colonel32
 */
public interface Entity
{
	/**
	 * Does entity logic.
	 * @param timeDelta Difference between current time and the last call to think();
	 */
	public void think(int timeDelta);
	/**
	 * Draws entity at its position. No entity logic should be done here (that is the job of think())
	 * @param clip Camera AABB, or null (in which case, draw the entity)
	 */
	public void draw(AABB clip);

	public double getX();
	public double getY();

	public void addedToWorld(World w);
	public void removedFromWorld(World w);

	public World getWorld();
}
