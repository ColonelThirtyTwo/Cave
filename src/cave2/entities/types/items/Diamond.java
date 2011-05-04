
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.Item;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.tile.generators.OrePerlinGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.ModuleBase;
import libnoiseforjava.module.RidgedMulti;
import libnoiseforjava.module.ScaleBias;
import libnoiseforjava.module.ScalePoint;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel32
 */
public class Diamond extends Item
{
	private int bob;
	public Diamond(double x, double y)
	{
		super(x,y);
		bob = 0;
	}

	public void drawIcon()
	{
		Texture img = ResourceManager.getInstance().getImage("entities/diamond.png");
		RenderUtil.drawImage(img, 0, 0, 1, 1);
	}

	public void equip()
	{
	}

	public void unequip()
	{
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
	}

	public void altUse(Player ply, double x, double y, boolean isdown)
	{
	}

	public void collidedWith(CollisionEntity e)
	{
	}

	public void draw(AABB bounds)
	{
		bob++;
		if(!box.overlaps(bounds)) return;

		Texture img = ResourceManager.getInstance().getImage("entities/diamond.png");
		double floaty = Math.sin(bob/100.0)*0.1;
		RenderUtil.drawImage(img, box.center.x-box.size.x, box.center.y-box.size.y+floaty,
				box.size.x*2, box.size.y*2, 1.0);
	}
}
