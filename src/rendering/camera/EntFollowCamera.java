
package rendering.camera;

import entities.Entity;
import structures.AABB;
import tile.World;

/**
 * Camera that follows an entity
 * @author Colonel32
 */
public class EntFollowCamera extends AbstractCamera
{
	protected Entity ent;
	protected double w, h;

	public EntFollowCamera(World world, Entity following, double w, double h)
	{
		super(world);
		ent = following;
		this.w = w;
		this.h = h;
	}

	protected AABB getCameraBounds()
	{
		return new AABB(ent.getX(), ent.getY(), w, h);
	}
}
