/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cave2.tile.generators;

import cave2.tile.Chunk;
import cave2.tile.Generator;
import cave2.tile.Tile;
import cave2.tile.World;
import cave2.tile.types.BreakableWall;
import cave2.tile.types.Ground;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;
import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.ScaleBias;
import libnoiseforjava.module.ScalePoint;

/**
 *
 * @author Colonel32
 */
public class OrePerlinGenerator implements Generator
{
	private static final Logger log = Logger.getLogger("cave2");;

	public static interface OreConfig
	{
		public ModuleBase getNoiseGen();
		public double getThreshold();
		public Tile allocateTile();
	}

	private static List<OreConfig> orelist = new ArrayList<OreConfig>();

	/**
	 * Adds an ore to the generator.
	 */
	public static void addOre(OreConfig conf)
	{
		log.log(Level.INFO, "Adding ore generator: {0}",conf);
		orelist.add(conf);
	}

	public static void reset()
	{
		log.log(Level.INFO, "Resetting ores");
		orelist.clear();
	}

	private double threshold;
	private ModuleBase generator;

	public OrePerlinGenerator(double threshold, int seed)
	{
		this.threshold = threshold;
//		this.threshold = 0.9;

		try
		{
			Perlin basePerlin = new Perlin();
			basePerlin.setSeed(seed);
			basePerlin.setOctaveCount(2);
			basePerlin.setNoiseQuality(NoiseQuality.QUALITY_BEST);
//			RidgedMulti basePerlin = new RidgedMulti();
//			basePerlin.setSeed(seed);
//			basePerlin.setOctaveCount(2);
			ScaleBias normalizer = new ScaleBias(basePerlin);
			normalizer.setBias(0.5);
			normalizer.setScale(0.5);
			ScalePoint scalar = new ScalePoint(normalizer);
			scalar.setScale(0.2);

			generator = scalar;
		}
		catch(ExceptionInvalidParam e)
		{
			log.log(Level.SEVERE, null, e);
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
				boolean iswall = generator.getValue(ox+x, oy+y, 0) > threshold;
				if(!iswall)
				{
					Tile t = new Ground();
					c.tiles[x][y] = t;
					t.addedToWorld(w, ox+x, oy+y);
				}
				else
				{
					Tile t = null;
					for(int i=0; i<orelist.size(); i++)
					{
						OreConfig cfg = orelist.get(i);
						
						if(cfg.getNoiseGen().getValue(ox+x, oy+y, 0) >= cfg.getThreshold())
						{
							t = cfg.allocateTile();
							break;
						}
					}
					if(t == null)
						t = new BreakableWall();
					c.tiles[x][y] = t;
					t.addedToWorld(w, ox+x, oy+y);
				}
			}
		c.isGenerated = true;
	}
}
