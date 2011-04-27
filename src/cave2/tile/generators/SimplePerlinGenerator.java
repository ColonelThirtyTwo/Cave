
package cave2.tile.generators;

import java.util.logging.Logger;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.*;
import cave2.tile.*;
import cave2.tile.types.*;

/**
 * Puts wall tiles where perlin(x,y,0) > threshold
 * @author Colonel 32
 */
public class SimplePerlinGenerator implements Generator
{
	private static final Logger log = Logger.getLogger("cave2");;

	private double threshold;
	private ModuleBase generator;
	//private SampleMeasurer measurer;
	private Perlin perlin;

	public SimplePerlinGenerator(double threshold, int seed)
	{
		this.threshold = threshold;

		try
		{
			perlin = new Perlin();
			perlin.setSeed(seed);
			perlin.setOctaveCount(2);
			//measurer = new SampleMeasurer(perlin);
			//ScaleBias normalizer = new ScaleBias(measurer);
			ScaleBias normalizer = new ScaleBias(perlin);
			normalizer.setBias(0.5);
			normalizer.setScale(0.5);
			ScalePoint scalar = new ScalePoint(normalizer);
			scalar.setScale(0.2);

			generator = scalar;
		}
		catch(ExceptionInvalidParam e)
		{
			throw new RuntimeException(e.getMessage());
		}

	}

	public void generate(World w, Chunk c, int cx, int cy)
	{
		//measurer.sampleCount = 0;
		int ox = cx * Chunk.CHUNK_SIZE;
		int oy = cy * Chunk.CHUNK_SIZE;
		for(int x=0; x<Chunk.CHUNK_SIZE; x++)
			for(int y=0; y<Chunk.CHUNK_SIZE; y++)
			{
				Tile t;
				if(generator.getValue(ox+x, oy+y, 0) > threshold)
					t = new BreakableWall();
				else
					t = new Ground();
				c.tiles[x][y] = t;
				if(t != null) t.addedToWorld(w, ox+x, oy+y);
			}
		c.isGenerated = true;
	}
}
