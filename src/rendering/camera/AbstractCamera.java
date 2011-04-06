
package rendering.camera;

import org.lwjgl.opengl.GL11;
import rendering.RenderUtil;
import structures.AABB;
import tile.World;

/**
 * Abstract camera class
 * @author Colonel32
 */
public abstract class AbstractCamera implements Camera
{
	protected World world;
	public AbstractCamera(World w)
	{
		this.world = w;
	}

	protected abstract AABB getCameraBounds();

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
}
