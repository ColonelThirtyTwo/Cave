
package cave2.entities.types.items;

import cave2.entities.CollisionEntity;
import cave2.entities.Item;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import cave2.structures.exceptions.QuitException;
import cave2.structures.pathfinding.PathConfig;
import cave2.structures.pathfinding.PathFinder;
import cave2.tile.BreakableTile;
import cave2.tile.Tile;
import cave2.tile.World;
import cave2.tile.types.DebugTile;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Debug pathfinding item
 * @author Colonel 32
 */
public class PathFinderItem extends Item
{
	private class Cfg implements PathConfig
	{

		Tile start, end;
		World w;
		public Tile getStart()
		{
			return start;
		}

		public Tile getEnd()
		{
			return end;
		}

		public World getWorld()
		{
			return w;
		}

		public int wrap(int i, int max)
		{
			i %= max;
			if(i<0) i += max;
			return i;
		}
		public double getMovementCost(Tile origin, Tile[] adj, int i)
		{
			Tile t = adj[i];

			if(t == null)
				return Double.POSITIVE_INFINITY;

			if(i % 2 == 1)
			{
				// Corner
				Tile c1 = adj[wrap(i+1,8)];
				Tile c2 = adj[wrap(i-1,8)];
				if(c1 == null || c2 == null || c1.isSolid() || c2.isSolid())
					return Double.POSITIVE_INFINITY; // Prevent trying to go through walls

				if(t instanceof BreakableTile)
					return 1.4142135623730950488016887242097 * ((BreakableTile)t).getHealth() * 20;
				else if(t.isSolid())
					return Double.POSITIVE_INFINITY;
				else
					return 1.4142135623730950488016887242097;
			}
			else
			{
				if(t instanceof BreakableTile)
					return ((BreakableTile)t).getHealth() * 20;
				else if(t.isSolid())
					return Double.POSITIVE_INFINITY;
				else
					return 1;
			}
		}

		public double getHeuristic(Tile origin, Tile[] adj, int adjindex)
		{
			Tile t = adj[adjindex];
			return Math.abs(t.getX() - getStart().getX()) + Math.abs(t.getY() - getStart().getY());
		}

	}

	private Future<List<Tile>> list;
	public PathFinderItem(double x, double y)
	{
		super(x,y);
	}

	public void equip()
	{
	}

	public void unequip()
	{
	}

	public void mainUse(Player ply, double x, double y, boolean isdown)
	{
		Cfg cfg = new Cfg();
		cfg.start = ply.getWorld().getTileAt(ply.getX(), ply.getY());
		cfg.end = ply.getWorld().getTileAt(x, y);
		cfg.w = ply.getWorld();
		list = PathFinder.queuePath(cfg);
	}

	public void altUse(Player ply, double x, double y, boolean isdown)
	{
		if(list != null && list.isDone())
		{
			if(list.isCancelled()) return;
			List<Tile> l;
			try
			{
				l = list.get();
			}
			catch(InterruptedException ex)
			{
				throw new QuitException("Some douchebag interrupted the main thread.");
			}
			catch(ExecutionException ex)
			{
				throw new RuntimeException(ex);
			}

			for(Tile t : l)
			{
				Tile newt = new DebugTile();
				world.setTile(t.getX(), t.getY(), newt);
			}
			list = null;
		}
	}

	public void drawIcon()
	{
		RenderUtil.drawImage(ResourceManager.getImage("colors/red.png"), 0, 0, 1, 1);
	}

	public void collidedWith(CollisionEntity e)
	{
	}

	public void draw(AABB clip)
	{
		RenderUtil.drawImage(ResourceManager.getImage("colors/red.png"), box, 1);
	}

}
