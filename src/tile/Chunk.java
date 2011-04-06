
package tile;

/**
 * A chunk of the map. Always square.
 * @author Colonel32
 */
public class Chunk
{
	/**
	 * Coordinates of the chunk.
	 */
	public final int x, y;
	/**
	 * 2D array of tiles contained in this chunk. Should be CHUNK_SIZE by CHUNK_SIZE
	 */
	public Tile[][] tiles;

	/**
	 * Size of one of the sides of the chunk, in tiles.
	 */
	public static final int CHUNK_SIZE = 50;

	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
	}

	public String toString()
	{
		return "("+x+","+y+")";
	}

	public int hashCode()
	{
		return (x<<16) ^ y;
	}
}
