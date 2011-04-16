
package tile;

import java.util.*;
import entities.Entity;
import rendering.camera.Camera;
import structures.AABB;
import structures.BufferedSet;
import structures.HashGrid;

/**
 *
 * @author Colonel32
 */
public class World
{
	//protected Map<Long,Chunk> spatialMap;
	protected HashGrid<Chunk> grid;
	protected BufferedSet<Entity> ents;
	protected Generator generator;
	protected Camera camera;

	public World(Generator generator, Camera camera)
	{
		//spatialMap = new HashMap<Long,Chunk>();
		grid = new HashGrid<Chunk>();
		ents = new BufferedSet<Entity>();
		this.generator = generator;
		this.camera = camera;
		if(camera != null) camera.setWorld(this);
	}

	/**
	 * Returns the tile at x, y
	 */
	public Tile getTileAt(int x, int y)
	{
		Chunk c = getContainingChunk(x,y);
		if(c == null) return null;
		int ox = wrap(x,Chunk.CHUNK_SIZE);
		int oy = wrap(y,Chunk.CHUNK_SIZE);
		return c.tiles[ox][oy];
	}

	/**
	 * Writes the tiles adjacent to (x,y) to the first 8 spots of the passed array.
	 * The first item will be set to (x,y+1) and will continue clockwise.
	 * @throws ArrayIndexOutOfBoundsException If arr.length < 8
	 */
	public void getAdjacent(Tile[] arr, int x, int y)
	{
		if(arr.length < 8) throw new ArrayIndexOutOfBoundsException("Array is not large enough to hold 8 tiles.");
		arr[0] = getTileAt(x,y+1);
		arr[1] = getTileAt(x+1,y+1);
		arr[2] = getTileAt(x+1,y);
		arr[3] = getTileAt(x+1,y-1);
		arr[4] = getTileAt(x,y-1);
		arr[5] = getTileAt(x-1,y-1);
		arr[6] = getTileAt(x-1,y);
		arr[7] = getTileAt(x-1,y+1);
	}

	/**
	 * Returns the chunk at (cx,cy), in chunk coordinates
	 */
	public Chunk getChunk(int cx, int cy)
	{
		//long hash = (long)cx << 32 | cy;
		//Chunk c = spatialMap.get(hash);
		Chunk c = grid.get(cx, cy);
		assert(c == null || (c.x == cx && c.y == cy));
		return c;
	}

	/**
	 * Returns the chunk that tile (x,y) lies in.
	 */
	public Chunk getContainingChunk(double x, double y)
	{
		int cx = (int)Math.floor(x / Chunk.CHUNK_SIZE);
		int cy = (int)Math.floor(y / Chunk.CHUNK_SIZE);
		return getChunk(cx,cy);
	}

	public void setGenerator(Generator g) { generator = g; }

	protected void createChunk(int cx, int cy)
	{
		Chunk c = generator.generate(this, cx, cy);
		assert(c.x == cx && c.y == cy);
		//spatialMap.put((long)cx << 32 | cy, c);
		grid.set(cx, cy, c);
	}

	public void queryChunks(List<Chunk> list, AABB box)
	{
		int xl = (int)Math.floor((box.center.x-box.size.x) / Chunk.CHUNK_SIZE);
		int xh = (int)Math.floor((box.center.x+box.size.x) / Chunk.CHUNK_SIZE);
		int yl = (int)Math.floor((box.center.y-box.size.y) / Chunk.CHUNK_SIZE);
		int yh = (int)Math.floor((box.center.y+box.size.y) / Chunk.CHUNK_SIZE);
		for(int x = xl; x <= xh; x++)
			for(int y = yl; y <= yh; y++)
			{
				Chunk c = getChunk(x,y);
				if(c == null)
				{
					createChunk(x,y);
					c = getChunk(x,y);
				}
				list.add(c);
			}
	}

	public void addEntity(Entity e)
	{
		ents.add(e);
		e.addedToWorld(this);
	}
	public void removeEntity(Entity e)
	{
		ents.remove(e);
		e.removedFromWorld(this);
	}

	public void think(int timeDelta)
	{
		ents.lock();
		for(Entity e : ents)
		{
			e.think(timeDelta);
		}
		ents.unlock();
	}
	public void draw(AABB clip)
	{
		ents.lock();
		drawTiles(clip);
		for(Entity e : ents) e.draw(clip);
		ents.unlock();
	}

	protected void drawTiles(AABB clip)
	{
		int minx = (int)(Math.floor(clip.center.x - clip.size.x)) - 1;
		int miny = (int)(Math.floor(clip.center.y - clip.size.y)) - 1;
		int maxx = (int)(Math.floor(clip.center.x + clip.size.x));
		int maxy = (int)(Math.floor(clip.center.y + clip.size.y));

		// Compute area to find number of chunks to allocate.
		int size = (int)Math.ceil(clip.size.x*clip.size.y*4/Chunk.CHUNK_SIZE/Chunk.CHUNK_SIZE);
		ArrayList<Chunk> renderingchunks = new ArrayList<Chunk>(size);
		//ArrayList<Chunk> renderingchunks = new ArrayList<Chunk>();
		queryChunks(renderingchunks,clip);

		for(Chunk c : renderingchunks)
		{
			if(c == null) continue;
			int cxoffset = c.x * Chunk.CHUNK_SIZE;
			int cyoffset = c.y * Chunk.CHUNK_SIZE;
			for(int x=0; x<Chunk.CHUNK_SIZE; x++)
			{
				int realx = x + cxoffset;

				if(realx < minx)
				{
					x = minx - cxoffset;
					continue;
				}
				else if(realx > maxx) { break; }


				for(int y=0; y<Chunk.CHUNK_SIZE; y++)
				{
					int realy = y+cyoffset;

					if(realy < miny)
					{
						y = miny - cyoffset;
						continue;
					}
					else if(realy > maxy) { break; }

					if(c.tiles[x][y] != null) c.tiles[x][y].draw(realx,realy,1,1);
				}
			}
		}
	}

	public void setCamera(Camera cam)
	{
		this.camera = cam;
		cam.setWorld(this);
	}
	public Camera getCamera()
	{
		return camera;
	}

	private static int wrap(int n, int max)
	{
		n = n % max;
		if(n < 0) n += max;
		return n;
	}
}
