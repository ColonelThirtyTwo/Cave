
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.Item;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import cave2.tile.BreakableTile;
import cave2.tile.Tile;
import org.newdawn.slick.opengl.Texture;

/**
 * Tool that does 1 point of "normal" damage per second on any breakable tiles.
 * @author Colonel32
 */
public class DrillItem extends Item
{
	private int time;
	private int bob;
	private BreakableTile drill;

	public DrillItem(double x, double y)
	{
		super(x,y);
		time = -1;
	}

	public void draw(AABB bounds)
	{
		bob++;
		if(!box.overlaps(bounds)) return;

		Texture img = ResourceManager.getInstance().getImage("entities/drill.png");
		double floaty = Math.sin(bob/100.0)*0.1;
		RenderUtil.drawImage(img, box.center.x-box.size.x, box.center.y-box.size.y+floaty,
				box.size.x*2, box.size.y*2, 1.0);
	}

	public void drawIcon()
	{
		Texture img = ResourceManager.getInstance().getImage("entities/drill.png");
		RenderUtil.drawImage(img, 0, 0, 1, 1);
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
		if(isdown)
		{
			Tile t = ply.getWorld().getTileAt((int)Math.floor(x), (int)Math.floor(y));
			if(t != null && t instanceof BreakableTile)
				drill = (BreakableTile)t;
		}
		else
			drill = null;
	}

	public void altUse(Player ply, double x, double y, boolean isdown)
	{

	}

	public void collidedWith(CollisionEntity e)
	{

	}

	public void think(int timeDelta)
	{
		super.think(timeDelta);
		if(time == -1) time = timeDelta;
		else time += timeDelta;

		if(world == null)
		{
			if(drill != null)
			{
				if(drill.getHealth() >= 0)
					drill.damage("normal", timeDelta / 1000.0);
				else
					drill = null;
			}
		}
	}

	public void equip()
	{
	}

	public void unequip()
	{
	}
}
