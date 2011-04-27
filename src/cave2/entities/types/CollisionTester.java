
package cave2.entities.types;

import cave2.entities.CollisionEntity;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import org.newdawn.slick.opengl.Texture;

/**
 * If something collides with the entity
 * @author Colonel 32
 */
public class CollisionTester extends CollisionEntity
{
	private boolean hasCollided;

	public CollisionTester(double x, double y)
	{
		super(x,y,0.5,0.5);
		hasCollided = false;
	}

	public void collidedWith(CollisionEntity e)
	{
		hasCollided = true;
	}

	public void think(int timeDelta)
	{
		doTileCollisions();
		doTileCallbacks();
	}

	public void draw(AABB clip)
	{
		Texture t;
		if(hasCollided)
			t = ResourceManager.getInstance().getImage("colors/red.png");
		else
			t = ResourceManager.getInstance().getImage("colors/blue.png");
		RenderUtil.drawImage(t, box, 1);
		hasCollided = false;
	}

}
