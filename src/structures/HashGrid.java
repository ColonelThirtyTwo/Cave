
package structures;

import java.util.*;

/**
 * A grid, backed with a hash map. Infinite size.
 * @author Colonel32
 */
public class HashGrid<T>
{
	private static class Spot
	{
		int x, y;
		public Spot(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public boolean equals(Object o)
		{
			if(!(o instanceof Spot)) return false;
			Spot s = (Spot)o;
			return s.x == this.x && s.y == this.y;
		}
		public int hashCode()
		{
			return x<<16 ^ y;
		}
		public String toString()
		{
			return "("+x+","+y+")";
		}
	}

	private Map<Spot,T> gridmap;
	private Spot readcache;

	public HashGrid()
	{
		gridmap = new HashMap<Spot,T>();
		readcache = new Spot(0,0);
	}

	public T get(int x, int y)
	{
		readcache.x=x;
		readcache.y=y;
		return gridmap.get(readcache);
	}

	public T set(int x, int y, T value)
	{
		Spot s = new Spot(x,y);
		return gridmap.put(s, value);
	}
}
