
package cave2.entities.types;

import cave2.entities.CollisionEntity;
import cave2.entities.Creature;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class Bullet extends CollisionEntity
{
	private double dirx, diry;

	public Bullet(double x, double y, double vx, double vy)
	{
		super(x,y,.05,.05);
		dirx = vx;
		diry = vy;
	}

	public void collidedWith(CollisionEntity e)
	{
		if(world == null) return;
		if(e instanceof Creature && !(e instanceof Player))
		{
			((Creature)e).damage("normal", 5);
			world.removeEntity(this);
		}
	}

	public void think(int timeDelta)
	{
		if(world == null) return;
		box.center.x += dirx * 0.8;
		box.center.y += diry * 0.8;

		if(doTileCollisions())
		{
			world.removeEntity(this);
			return;
		}

	}

	public void draw(AABB clip)
	{
		if(world == null) return;
		if(!box.overlaps(clip)) return;
		Texture t = ResourceManager.getImage("entities/bullet.png");
		RenderUtil.drawImage(t, box, 1);
	}

}
