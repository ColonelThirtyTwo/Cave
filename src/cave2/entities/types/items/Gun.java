
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.Item;
import cave2.entities.types.Bullet;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class Gun extends Item
{
	public Gun(double x, double y)
	{
		super(x,y);
	}

	public void equip()
	{
		
	}

	public void unequip()
	{
		
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
		if(!isdown) return;

		double dx = x - ply.getX();
		double dy = y - ply.getY();
		double dist = Math.sqrt(dx*dx+dy*dy);
		dx /= dist;
		dy /= dist;

		Bullet b = new Bullet(ply.getX(), ply.getY(), dx, dy);
		ply.getWorld().addEntity(b);
	}

	public void altUse(Player ply, double x, double y, boolean isdown)
	{
		
	}

	public void drawIcon()
	{
		Texture t = ResourceManager.getImage("entities/gun.png");
		RenderUtil.drawImage(t, 0,0,1,1);
	}

	public void collidedWith(CollisionEntity e)
	{
	}

	public void draw(AABB clip)
	{
		Texture t = ResourceManager.getImage("entities/gun.png");
		RenderUtil.drawImage(t, box, 1);
	}

}
