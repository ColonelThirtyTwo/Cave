
package cave2.tile.generators;

import cave2.tile.Chunk;
import cave2.tile.Generator;
import cave2.tile.Tile;
import cave2.tile.World;
import cave2.tile.types.*;

/**
 * Static chunk generator, used for testing.
 * @author Colonel32
 */
public class TestGenerator implements Generator
{
	public void generate(World world, Chunk c, int cx, int cy)
	{
		for(int x=0; x<Chunk.CHUNK_SIZE; x++)
			for(int y=0; y<Chunk.CHUNK_SIZE; y++)
			{
				Tile t;
				if(x % 3 == 0 && y % 3 == 0)
					t = new Wall();
				else
					t = new Ground();
				c.tiles[x][y] = t;
				if(t != null) t.addedToWorld(world, Chunk.CHUNK_SIZE * cx + x, Chunk.CHUNK_SIZE * cy + y);
			}
		c.isGenerated = true;
	}
}
