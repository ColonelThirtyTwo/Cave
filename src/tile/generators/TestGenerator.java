
package tile.generators;

import tile.Chunk;
import tile.Generator;
import tile.Tile;
import tile.World;
import tile.types.*;

/**
 * Static chunk generator, used for testing.
 * @author Colonel32
 */
public class TestGenerator implements Generator
{
	public Chunk generate(World world, int cx, int cy)
	{
		Chunk c = new Chunk(cx,cy);

		for(int x=0; x<Chunk.CHUNK_SIZE; x++)
			for(int y=0; y<Chunk.CHUNK_SIZE; y++)
			{
				Tile t;
				if(x == 0 || x == Chunk.CHUNK_SIZE - 1 ||
						y == 0 || y == Chunk.CHUNK_SIZE - 1)
					//t = new Blue();
					t = new Wall();
				else
					t = new Ground();
				c.tiles[x][y] = t;
				t.addedToWorld(world, Chunk.CHUNK_SIZE * cx + x, Chunk.CHUNK_SIZE * cy + y);
			}

		return c;
	}
}
