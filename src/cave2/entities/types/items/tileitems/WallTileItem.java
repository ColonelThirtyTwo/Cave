
package cave2.entities.types.items.tileitems;

import cave2.entities.types.items.TileItem;
import cave2.tile.Tile;
import cave2.tile.types.BreakableWall;

/**
 *
 * @author Colonel 32
 */
public class WallTileItem extends TileItem
{
	public WallTileItem(double x, double y)
	{
		super(x,y);
	}

	public Class<? extends Tile> getTileClass()
	{
		return BreakableWall.class;
	}

}
