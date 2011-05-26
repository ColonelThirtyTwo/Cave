
package cave2.rendering.camera;

import cave2.structures.AABB;

/**
 * Camera that stays in place, without moving
 * @author Colonel32
 */
public class StaticCamera extends AbstractCamera
{
	protected AABB box;
	public StaticCamera(double x, double y, double w, double h)
	{
		box = new AABB(x,y,w/2,h/2);
	}

	public AABB getCameraBounds() { return box; }
}
