
package cave2.entities.types.items.tileitems;

import cave2.entities.types.items.TileItem;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import cave2.tile.Tile;
import cave2.tile.types.BreakableWall;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class WallTileItem extends TileItem
{
	private int bob;

	public WallTileItem(double x, double y)
	{
		super(x,y);
		bob = 0;
	}
	public WallTileItem(double x, double y, int count)
	{
		super(x,y,count);
	}

	public Class<? extends Tile> getTileClass()
	{
		return BreakableWall.class;
	}

	public void drawIcon()
	{
		Texture t = ResourceManager.getImage("tiles/testwall.png");
		RenderUtil.drawImage(t, 0.15, 0.15, 0.7, 0.7, 1, 0, 0, 0.5, 0.5);
		drawItemCount();
	}

	public void draw(AABB clip)
	{
		bob++;
		if(!box.overlaps(clip)) return;
		Texture t = ResourceManager.getImage("tiles/testwall.png");
		RenderUtil.drawImage(t, box.center.x - box.size.x, box.center.y - box.size.y + Math.sin(bob/100.0)*0.1,
				box.size.x*2, box.size.y*2, 1, 0, 0, 0.5, 0.5);
	}
}
