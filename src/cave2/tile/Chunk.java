
package cave2.tile;

/**
 * A chunk of the map. Always square.
 * @author Colonel32
 */
public class Chunk
{
	/**
	 * True if the chunk has terrain generated on it and can be used. False if the
	 * chunk has yet to have its terrain generated or if the terrain is being
	 * generated. DO NOT MODIFY OR ACCESS CHUNK IF FALSE.
	 */
	public boolean isGenerated;

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
	public static final int CHUNK_SIZE = 20;

	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
		isGenerated = false;
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
