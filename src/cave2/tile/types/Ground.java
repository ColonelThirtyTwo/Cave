
package cave2.tile.types;

import cave2.entities.Entity;
import cave2.rendering.RenderUtil;
import cave2.structures.ResourceManager;
import cave2.tile.AbstractTile;

/**
 * The ground.
 * @author Colonel32
 */
public class Ground extends AbstractTile
{
	public void draw(double x, double y, double w, double h)
	{
//		GL11.glColor3d(.6, .4, .2); // Brown
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // Bind default texture
//		RenderUtil.drawBox(x, y, w, h);
		RenderUtil.drawImage(ResourceManager.getImage("tiles/ground.png"), x, y, w, h);
	}

	public void entityEntered(Entity e) {}
	public void entityExited(Entity e) {}

	public boolean isAnimated() { return false; }
	public boolean isSolid() { return false; }
	public boolean isTransparent() { return true; }
}
