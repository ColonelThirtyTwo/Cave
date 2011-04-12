
package entities;

import tile.World;

/**
 * Abstract class for entities without collision
 * @author Colonel32
 */
public abstract class EffectEntity implements Entity
{
	protected World world;
	protected double x, y;
	public double getX() { return x; }
	public double getY() { return y; }

	public void addedToWorld(World w) { world = w; }
	public void removedFromWorld(World w)
	{
		assert(w == world);
		world = null;
	}
}
