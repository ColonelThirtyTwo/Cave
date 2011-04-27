
package cave2.tile.types;

import cave2.rendering.RenderUtil;
import cave2.structures.ResourceManager;
import cave2.tile.BreakableTile;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel32
 */
public class BreakableWall extends Wall implements BreakableTile
{
	private double health;

	public BreakableWall()
	{
		health = 1.0;
	}

	public void damage(String type, double amount)
	{
		health -= amount;
		if(health <= 0)
			world.setTile(x, y, new Ground());
	}

	public double getHealth() { return health; }

	public void draw(double x, double y, double w, double h)
	{
		super.draw(x,y,w,h);
		drawCracks(x,y,w,h,health,1.0);
	}

	public static void drawCracks(double x, double y, double w, double h, double health, double maxhealth)
	{
		Texture cracks = ResourceManager.getInstance().getImage("tiles/cracks.png");
		int crackedamnt = (int)((1 - health / maxhealth) * 16.0);
		crackedamnt = Math.min(Math.max(crackedamnt,0), 15);

		double u = (crackedamnt % 4) / 4.0;
		double v = (crackedamnt / 4) / 4.0;
		RenderUtil.drawImage(cracks, x, y, w, h, 1.0, u, v, 0.25, 0.25);
	}
}
