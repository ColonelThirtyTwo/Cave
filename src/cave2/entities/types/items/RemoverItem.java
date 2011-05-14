
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.Item;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import cave2.tile.types.Ground;
import org.newdawn.slick.opengl.Texture;

/**
 * Remover item. Tool that replaces the square that the user clicks with ground
 * @author Colonel32
 */
public class RemoverItem extends Item
{
	private int time;

	public RemoverItem(double x, double y)
	{
		super(x,y);
		time = -1;
	}

	public void draw(AABB bounds)
	{
		if(!box.overlaps(bounds)) return;

		Texture img = ResourceManager.getImage("entities/remover.png");
		double floaty = Math.sin(time/500.0)*0.1;
		RenderUtil.drawImage(img, box.center.x-box.size.x, box.center.y-box.size.y+floaty,
				box.size.x*2, box.size.y*2, 1.0);
	}

	public void drawIcon()
	{
		Texture img = ResourceManager.getImage("entities/remover.png");
		RenderUtil.drawImage(img, 0, 0, 1, 1);
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
		if(isdown)
			ply.getWorld().setTile((int)Math.floor(x), (int)Math.floor(y), new Ground());
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
	}

	public void equip()
	{
	}

	public void unequip()
	{
	}
}
