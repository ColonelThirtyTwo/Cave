
package cave2.rendering.camera;

import cave2.entities.Entity;
import cave2.structures.AABB;
import cave2.tile.World;

/**
 * Camera that follows an entity
 * @author Colonel32
 */
public class EntFollowCamera extends AbstractCamera
{
	protected Entity ent;
	protected double w, h;
	protected AABB box;

	public EntFollowCamera(Entity following, double w, double h)
	{
		super();
		ent = following;
		this.w = w/2;
		this.h = h/2;
		box = new AABB(ent.getX(), ent.getY(), this.w, this.h);
	}

	protected AABB getCameraBounds()
	{
		box.center.x = ent.getX();
		box.center.y = ent.getY();
		return box;
	}
}
