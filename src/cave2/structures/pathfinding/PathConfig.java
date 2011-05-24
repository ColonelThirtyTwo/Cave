
package cave2.structures.pathfinding;

import cave2.tile.Tile;
import cave2.tile.World;

/**
 *
 * @author Colonel 32
 */
public interface PathConfig
{
	public Tile getStart();
	public Tile getEnd();
	public World getWorld();
	public double getMovementCost(Tile origin, Tile[] adj, int adjindex);
	public double getHeuristic(Tile origin, Tile[] adj, int adjindex);
}
