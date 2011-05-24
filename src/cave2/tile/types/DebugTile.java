
package cave2.tile.types;

import cave2.rendering.RenderUtil;
import cave2.structures.ResourceManager;

/**
 * Tile meant for testing
 * @author Colonel 32
 */
public class DebugTile extends Ground
{
	public void draw(double x, double y, double w, double h)
	{
		RenderUtil.drawImage(ResourceManager.getImage("colors/blue.png"), x, y, w, h);
	}
}
