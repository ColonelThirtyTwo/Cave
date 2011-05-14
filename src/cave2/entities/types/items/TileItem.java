
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.StackableItem;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.structures.exceptions.QuitException;
import cave2.tile.Tile;
import cave2.tile.types.Ground;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;

/**
 * Item that spawns a tile
 * @author Colonel32
 */
public abstract class TileItem extends StackableItem
{
	private static final Logger log = LogU.getLogger();

	public TileItem(double x, double y)
	{
		super(x,y);
	}
	public TileItem(double x, double y, int count)
	{
		super(x,y,count);
	}
	
	public int getMaximum()
	{
		return 32;
	}

	public void equip()
	{
	}

	public void unequip()
	{
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
		int gx = (int)Math.floor(x);
		int gy = (int)Math.floor(y);
		if(ply.getWorld().getTileAt(gx, gy) instanceof Ground)
		{
			Tile t;
			try
			{
				t = getTileClass().newInstance();
			}
			catch(InstantiationException e)
			{
				log.log(Level.SEVERE, "Error while instantiating new tile: {0}", (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
				RuntimeException newerr = new QuitException("Instantiation error");
				newerr.initCause(e);
				throw newerr;
			}
			catch(IllegalAccessException e)
			{
				log.log(Level.SEVERE, "Illegal Access Exception while instantiating new tile: {0}", e.getMessage());
				RuntimeException newerr = new QuitException("Illegal Access error");
				newerr.initCause(e);
				throw newerr;
			}
			ply.getWorld().setTile(gx, gy, t);
			setCount(getCount() - 1);
		}
	}

	public void altUse(Player ply, double x, double y, boolean isdown)
	{
		
	}

	public void drawIcon()
	{
		// TODO: What to draw? :/
		Texture t = ResourceManager.getImage("entities/crate.png");
		RenderUtil.drawImage(t, 0, 0, 1, 1);
		drawItemCount();
	}

	public void collidedWith(CollisionEntity e)
	{
	}

	public void draw(AABB clip)
	{
		// TODO: What to draw? :/
		Texture t = ResourceManager.getImage("entities/crate.png");
		RenderUtil.drawImage(t, box, 1.0);
	}

	/**
	 * Returns the tile class that this item spawns. Must have a constructor
	 * that takes no arguments.
	 */
	public abstract Class<? extends Tile> getTileClass();
}
