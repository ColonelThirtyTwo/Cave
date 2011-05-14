
package cave2.tile.types;

import cave2.entities.Entity;
import cave2.entities.types.items.Diamond;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.tile.Tile;
import cave2.tile.generators.OrePerlinGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;
import libnoiseforjava.module.RidgedMulti;
import libnoiseforjava.module.ScaleBias;
import libnoiseforjava.module.ScalePoint;
import org.newdawn.slick.opengl.Texture;

/**
 * A wall o diamonds.
 * @author Colonel32
 */
public class DiamondWall extends BreakableWall
{
	private static final Logger log = LogU.getLogger();

	public DiamondWall()
	{
		health = 3;
	}

	public void damage(String type, double amount)
	{
		if(health <= 0) return;
		health -= amount;
		if(health <= 0)
		{
			log.log(Level.FINER, "Tile was destroyed due to damage.",this);
			world.setTile(x, y, new Ground());
			Entity e = new Diamond(x+0.5,y+0.5);
			world.addEntity(e);
		}
	}

	public double getMaxHealth() { return 3.0; }

	protected Texture getTileset()
	{
		return ResourceManager.getImage("tiles/diamondore.png");
	}

	private static class DiamondConfig implements OrePerlinGenerator.OreConfig
	{
		private static final Logger log = LogU.getLogger();
		private ModuleBase module;

		private DiamondConfig(int seed)
		{
			try
			{
				RidgedMulti p = new RidgedMulti();
				p.setSeed(seed);
				p.setOctaveCount(2);
				p.setNoiseQuality(NoiseQuality.QUALITY_BEST);
				ScaleBias n = new ScaleBias(p);
				n.setBias(0.5);
				n.setScale(0.5);
				ScalePoint s = new ScalePoint(n);
				s.setScale(0.05);

				module = s;
			}
			catch (ExceptionInvalidParam ex)
			{
				log.log(Level.SEVERE,null,ex);
			}
		}

		public ModuleBase getNoiseGen() { return module; }
		public double getThreshold() { return 0.9; }
		public Tile allocateTile() { return new DiamondWall(); }
	}

	public static void addToOreGenerator(int seed)
	{
		OrePerlinGenerator.OreConfig cfg = new DiamondConfig(seed);
		OrePerlinGenerator.addOre(cfg);
	}
}
