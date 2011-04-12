
package entities.types;

import entities.CollisionEntity;
import org.newdawn.slick.opengl.Texture;
import rendering.RenderUtil;
import structures.AABB;
import structures.ResourceManager;

/**
 * Rectangle entity. Does tile collisions.
 * @author Colonel32
 */
public class TestEntity extends CollisionEntity
{
	public TestEntity(double x, double y)
	{
		super(x,y,0.5,0.5);
	}

	public void collidedWith(CollisionEntity e)
	{}

	public void think(int delta)
	{
		super.doTileCallbacks();
		super.doTileCollisions();
	}
	public void draw(AABB camera)
	{
		// Don't draw if we are drawing way off screen
		if(!box.overlaps(camera)) return;

		Texture t = ResourceManager.getInstance().getImage("entities/testent.png");
		RenderUtil.drawImage(t, box.center.x - box.size.x, box.center.y - box.size.y,
				box.size.x*2, box.size.y*2);
	}
}
