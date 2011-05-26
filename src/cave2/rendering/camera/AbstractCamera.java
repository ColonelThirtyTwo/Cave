
package cave2.rendering.camera;

import org.lwjgl.opengl.GL11;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.Vec2;
import cave2.tile.World;

/**
 * Abstract camera class. Renders everything inside of getCameraBounds without any
 * additional transformations.
 * @author Colonel32
 */
public abstract class AbstractCamera implements Camera
{
	protected World world;
	public AbstractCamera()
	{
		world = null;
	}

	public void setWorld(World w) { world = w; }

	public void draw()
	{
		AABB box = getCameraBounds();

		int screenx = RenderUtil.width();
		int screeny = RenderUtil.height();

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		// Set up the camera
		GL11.glRotatef(0,1,1,1);
		GL11.glScaled(screenx / box.size.x / 2, screeny / box.size.y / 2, 1);
		GL11.glTranslated(box.size.x - box.center.x, box.size.y - box.center.y, 0);

		world.draw(box);

		GL11.glPopMatrix();
	}

	public void screen2world(Vec2 v, double x, double y)
	{
		int screenx = RenderUtil.width();
		int screeny = RenderUtil.height();
		AABB box = getCameraBounds();
		x -= screenx / 2;
		y = screeny / 2 - y;
		x /= screenx / box.size.x / 2;
		y /= screeny / box.size.y / 2;
		x += box.center.x;
		y += box.center.y;
		v.x = x;
		v.y = y;
	}

	public void world2screen(Vec2 v, double x, double y)
	{
		int screenx = RenderUtil.width();
		int screeny = RenderUtil.height();
		AABB box = getCameraBounds();
		x -= box.center.x;
		y -= box.center.y;
		x *= screenx / box.size.x / 2;
		y *= screeny / box.size.y / 2;
		x += screenx / 2;
		y = screeny / 2 - y;
		v.x = x;
		v.y = y;
	}
}
