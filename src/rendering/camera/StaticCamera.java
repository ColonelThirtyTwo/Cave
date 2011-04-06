
package rendering.camera;

import structures.AABB;
import tile.World;

/**
 * Camera that stays in place, without moving
 * @author Colonel32
 */
public class StaticCamera extends AbstractCamera
{
	protected AABB box;
	public StaticCamera(World world, double x, double y, double w, double h)
	{
		super(world);
		box = new AABB(x,y,w/2,h/2);
	}

	protected AABB getCameraBounds() { return box; }
}
