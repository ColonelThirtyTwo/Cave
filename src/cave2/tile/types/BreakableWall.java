
package cave2.tile.types;

import cave2.entities.Entity;
import cave2.entities.types.items.tileitems.WallTileItem;
import cave2.rendering.RenderUtil;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.tile.BreakableTile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel32
 */
public class BreakableWall extends Wall implements BreakableTile
{
	protected double health;

	private static final Logger log = LogU.getLogger();

	public BreakableWall()
	{
		health = 1.0;
	}

	public void damage(String type, double amount)
	{
		if(health <= 0) return;
		health -= amount;
		if(health <= 0)
		{
			log.log(Level.FINER, "Tile was destroyed due to damage.",this);
			world.setTile(x, y, new Ground());
			Entity e = new WallTileItem(x+0.5,y+0.5);
			world.addEntity(e);
		}
	}

	public double getHealth() { return health; }
	public double getMaxHealth() { return 1.0; }

	public void draw(double x, double y, double w, double h)
	{
		super.draw(x,y,w,h);
		drawCracks(x,y,w,h,health,getMaxHealth());
	}

	public static void drawCracks(double x, double y, double w, double h, double health, double maxhealth)
	{
		Texture cracks = ResourceManager.getImage("tiles/cracks.png");
		int crackedamnt = (int)((1 - health / maxhealth) * 16.0);
		crackedamnt = Math.min(Math.max(crackedamnt,0), 15);

		double u = (crackedamnt % 4) / 4.0;
		double v = (crackedamnt / 4) / 4.0;
		RenderUtil.drawImage(cracks, x, y, w, h, 1.0, u, v, 0.25, 0.25);
	}
}
