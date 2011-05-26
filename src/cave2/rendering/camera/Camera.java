
package cave2.rendering.camera;

import cave2.structures.AABB;
import cave2.structures.Vec2;
import cave2.tile.World;

/**
 * Controls rendering; mostly just camera transformations.
 * @author Colonel32
 */
public interface Camera
{
	public void draw();
	public void setWorld(World world);
	public AABB getCameraBounds();

	public void screen2world(Vec2 buffer, double screenx, double screeny);
	public void world2screen(Vec2 buffer, double worldx, double worldy);
}
