
package cave2.structures;

/**
 * Axis Aligned Bounding Box, based off of the one from JBox2D. Uses Vec2.
 * @author Colonel32
 */
public class AABB implements Cloneable
{
	public final Vec2 center, size;

	public AABB(Vec2 center, Vec2 size)
	{
		this.center = center;
		this.size = size;
	}

	public AABB(double x, double y, double w, double h)
	{
		this(new Vec2(x,y), new Vec2(w,h));
	}

	public Vec2 getMinCorner()
	{
		return center.sub(size);
	}
	public Vec2 getMaxCorner()
	{
		return center.add(size);
	}

	public boolean overlaps(AABB box)
	{
		return Math.abs(box.center.x - center.x) <= box.size.x + size.x &&
				Math.abs(box.center.y - center.y) <= box.size.y + size.y;
	}

	/** Returns the amount box was overlapping with this.
	    (points toward box) */
	public Vec2 getOverlap(AABB box)
	{
		double w = box.size.x + this.size.x;
		double h = box.size.y + this.size.y;
		double dx = box.center.x - center.x;
		double dy = box.center.y - center.y;

		if(Math.abs(dx) > w || Math.abs(dy) > h)
			return null; // No collision

		double penetrationx = w - Math.abs(dx);
		double penetrationy = h - Math.abs(dy);

		if(penetrationx < penetrationy)
		{
			return new Vec2(penetrationx * Math.signum(dx),0);
		}
		else
		{
			return new Vec2(0,penetrationy * Math.signum(dy));
		}
	}


	public boolean contains(Vec2 point)
	{
		return this.contains(point.x, point.y);
	}
	public boolean contains(double x, double y)
	{
		return Math.abs(x - center.x) <= size.x && Math.abs(y - center.y) <= size.y;
	}

//	private static double abs(double value)
//	{
//		return value < 0 ? -value : value;
//	}

	public boolean equals(Object o)
	{
		if(!(o instanceof AABB))
			return false;
		AABB aabb = (AABB)o;
		return aabb.center.equals(this.center) && aabb.size.equals(this.size);
	}

	public AABB clone()
	{
		return new AABB(center.clone(), size.clone());
	}

	public void copyInto(AABB target)
	{
		center.copyInto(target.center);
		size.copyInto(target.size);
	}

	public String toString()
	{
		return center.toString() + "@" + size.toString();
	}

	public int hashCode()
	{
		return center.hashCode() + size.hashCode();
	}

	public static AABB fromCorners(Vec2 min, Vec2 max)
	{
		return fromCorners(min.x, min.y, max.x, max.y);
	}
	public static AABB fromCorners(double minx, double miny, double maxx, double maxy)
	{
		return new AABB((minx+maxx)/2,(miny+maxy)/2,(maxx-minx)/2,(maxy-miny)/2);
	}

	public static AABB fromCornerDimensions(double minx, double miny, double w, double h)
	{
		return new AABB(minx+w/2,miny+h/2,w/2,h/2);
	}

	public static void main(String[] argz)
	{
		AABB test1 = new AABB(new Vec2(0,0), new Vec2(1,1));
		AABB test2 = new AABB(new Vec2(1.5,0), new Vec2(1,1));
		System.out.println(test1.overlaps(test2));
		System.out.println(test1.getOverlap(test2));
	}
}
