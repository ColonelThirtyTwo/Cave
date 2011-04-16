
package rendering.camera;

import structures.Vec2;
import tile.World;

/**
 * Controls rendering; mostly just camera transformations.
 * @author Colonel32
 */
public interface Camera
{
	public void draw();
	public void setWorld(World world);

	public void screen2world(Vec2 buffer, double screenx, double screeny);
	public void world2screen(Vec2 buffer, double worldx, double worldy);
}
