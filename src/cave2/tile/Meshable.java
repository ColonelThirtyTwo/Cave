
package cave2.tile;

import org.newdawn.slick.opengl.Texture;
import cave2.rendering.RenderUtil;
import cave2.structures.Vec2;

/**
 * Tile class that handles tiles that "mesh" together. Like the walls in most
 * tile-based games. Most similar to the early Legend of Zelda games.
 *
 * Implementing classes should load an image of quarter tiles. This class will
 * render four quads with the appropriate texture coordinates.
 * @author Colonel32
 */
public abstract class Meshable implements Tile
{
	private static Tile[] adj = new Tile[8]; // Buffer for adjacent tiles

	protected World world;
	protected int x, y;

	public int getX() { return x; }
	public int getY() { return y; }
	public void getPos(Vec2 vec) { vec.x = x; vec.y = y; }

	public void addedToWorld(World w, int x, int y)
	{
		this.world = w;
		this.x = x;
		this.y = y;
	}

	public void removedFromWorld(World w, int x, int y)
	{
		this.world = null;
	}

	public World getWorld() { return world; }

	protected abstract Texture getTileset();
	protected abstract int getMeshGroup();

	public void draw(double x, double y, double w, double h)
	{
		//if(this.x != 5 || this.y != -1) return;
		world.getAdjacent(adj, this.x, this.y);
		short mask = computeAdjData(adj,getMeshGroup());
		int se = getCornerType(mask,0);
		int ne = getCornerType(mask,1);
		int nw = getCornerType(mask,2);
		int sw = getCornerType(mask,3);

		Texture tileset = getTileset();
		double tilew = w/2;
		double tileh = h/2;

		double u, v;

		// NE Corner
		u = select(ne,1,1,3,2,3);
		v = select(ne,0,2,0,0,2);
		RenderUtil.drawImage(tileset, x+tilew, y, tilew, tileh, 1, u/4, v/4, 0.25, 0.25);

		// NW Corner
		u = select(nw,0,0,2,2,2);
		v = select(nw,0,2,1,0,2);
		RenderUtil.drawImage(tileset, x, y, tilew, tileh, 1, u/4, v/4, 0.25, 0.25);

		// SE Corner
		u = select(se,1,1,3,3,3);
		v = select(se,1,3,0,1,3);
		RenderUtil.drawImage(tileset, x+tilew, y+tileh, tilew, tileh, 1, u/4, v/4, 0.25, 0.25);

		// SW Corner
		u = select(sw,0,0,2,3,2);
		v = select(sw,1,3,1,1,3);
		RenderUtil.drawImage(tileset, x, y+tileh, tilew, tileh, 1, u/4, v/4, 0.25, 0.25);
	}

	private static short computeAdjData(Tile[] adj, int cat)
	{
		short mask = 0;
		for(int i=0; i<8; i++)
		{
			boolean ismeshable = adj[i] != null && adj[i] instanceof Meshable &&
					((Meshable)adj[i]).getMeshGroup() == cat;
			mask |= ismeshable ? 1<<i : 0;
		}
		return mask;
	}

	/**
	 * Returns corner type:
	 * 1 = corner
	 * 2 = inverted corner
	 * 3 = side 1
	 * 4 = side 2
	 * 5 = filled
	 * @param b Bitmask of meshable adjacent tiles, with the first bit representing north and then
	 * continuing clockwise
	 */
	private static short getCornerType(short b, int side)
	{
		assert(side >= 0 && side <= 4);
		short b2 = (short)( (b>>side*2) & 0x7 ); // Crop out the bits that we don't need
		if(side == 3 && (b & 0x1) != 0) b2 |= 0x4; // "Wrap" the north bit for side 4

		switch(b2)
		{
			case (0x0):
			case (0x2):
				// Corner
				return 1;
			case (0x1):
			case (0x3):
				// Flat
				return (short)(3 + side % 2);
			case (0x4):
			case (0x6):
				// Flat 2
				return (short)(4 - side % 2);
			case (0x5):
				// Inverted Corner
				return 2;
			case (0x7):
				// Full
				return 5;
			default:
				throw new RuntimeException("Reached impossible branch!");
		}
	}

	private static int select(int i, int corner, int inv, int vert, int horiz, int full)
	{
		switch(i)
		{
			case (1):
				return corner;
			case (2):
				return inv;
			case (3):
				return vert;
			case (4):
				return horiz;
			case (5):
				return full;
			default:
				assert(false);
				return -1;
		}
	}

	public String toString()
	{
		return this.getClass().getSimpleName() + "@("+x+","+y+")";
	}

	/**
	 * Wall mesh group
	 */
	protected static final int MESH_WALL = 1;
	/**
	 * Water mesh group
	 */
	protected static final int MESH_WATER = 2;
}
