
package cave2.structures.pathfinding;

import java.util.*;
import cave2.tile.Tile;

/**
 *
 * @author Colonel 32
 */
public class Path implements Iterator
{
	private Queue<Tile> list;
	private boolean fixed;

	Path()
	{
		list = new LinkedList<Tile>();
		fixed = false;
	}

	/**
	 * Adds a tile to the path.
	 */

	void addTile(Tile t)
	{
		assert(!fixed);
		list.add(t);
	}

	/**
	 * Fixes the path to prevent modification.
	 */
	void fix()
	{
		fixed = true;
	}

	/**
	 * @return the next tile in the path
	 */
	public Tile next()
	{
		assert(fixed);
		return list.poll();
	}

	public boolean hasNext()
	{
		return !list.isEmpty();
	}

	/**
	 * Throws UnsupportedOperationException
	 * @throws UnsupportedOperationException when called
	 */
	public void remove()
	{
		throw new UnsupportedOperationException("Not supported.");
	}
}
