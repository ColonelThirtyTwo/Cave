
package cave2.structures.pathfinding;

import cave2.tile.Tile;

/**
 *
 * @author Colonel 32
 */
public interface PathConfig
{
	public double getCost(Tile origin, Tile next);
}
