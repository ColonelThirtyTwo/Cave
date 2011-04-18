package cave2.structures;

//import processing.core.PVector;
import java.util.Comparator;

/**
 * 2D Vector class. [operation]Into() functions modify the calling Vec2 and return it
 * (to reduce number of vectors allocated.)
 * @author Colonel32
 */
public class Vec2 implements Cloneable
{
    public double x, y;

    public Vec2() { this(0,0); }
    public Vec2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) { return new Vec2(x+other.x, y+other.y); }
    public Vec2 sub(Vec2 other) { return new Vec2(x-other.x, y-other.y); }
    public Vec2 mul(double scale) { return new Vec2(x*scale, y*scale); }
    public Vec2 div(double scale) { return mul(1.0/scale); }

	public Vec2 add(double x, double y) { return new Vec2(this.x+x, this.y+y); }
	public Vec2 sub(double x, double y) { return new Vec2(this.x-x, this.y-y); }

	public Vec2 addInto(Vec2 other) { this.x += other.x; this.y += other.y; return this; }
	public Vec2 subInto(Vec2 other) { this.x -= other.x; this.y -= other.y; return this; }
	public Vec2 mulInto(double scale) { x *= scale; y *= scale; return this; }
	public Vec2 divInto(double scale) { mulInto(1.0/scale); return this; }

	public Vec2 addInto(double x, double y) { this.x += x; this.y += y; return this; }
	public Vec2 subInto(double x, double y) { this.x -= x; this.y -= y; return this; }

    public double length2() { return x*x + y*y; }
    public double length() { return Math.sqrt(length2()); }

	public Vec2 normalized() { return this.div(this.length()); }
	public Vec2 normalizeInto() { return this.divInto(this.length()); }

	public double getDim(int d) { if(d == 0) return x; else return y; }

	public Vec2 rightNormal()
	{
		return new Vec2(-y, x);
	}
	public Vec2 leftNormal()
	{
		return new Vec2(y, -x);
	}

	public double dot(Vec2 other)
	{
		return x*other.x + y*other.y;
	}

//	public PVector toPVector()
//	{
//		PVector v = new PVector();
//		v.x = (float)x;
//		v.y = (float)y;
//		return v;
//	}

	public Vec2 clone()
	{
		return new Vec2(x, y);
	}

	public int hashCode()
	{
		return ((Double)x).hashCode() + ((Double)y).hashCode();
	}

	public boolean equals(Object other)
	{
		if(!(other instanceof Vec2))
		{
			return false;
		}
		Vec2 v = (Vec2)other;
		return v.x == x && v.y == y;
	}

	public boolean equals(Vec2 other, double epsilon)
	{
		return Math.abs(other.x-x) <= epsilon &&
				Math.abs(other.y-y) <= epsilon;
	}

	public Vec2 project(Vec2 axis)
	{
		return axis.mul(dot(axis));
	}

	public void rotateInto(double amnt)
	{
		double rad = Math.toRadians(amnt);
		double xc = x;
		double yc = y;
		this.x = xc*Math.cos(rad) + yc*-Math.sin(rad);
		this.y = xc*Math.sin(rad) + yc*Math.cos(rad);
	}

	public double dist(Vec2 other)
	{
		return Math.sqrt(dist2(other));
	}
	public double dist2(Vec2 other)
	{
		return (this.x-other.x)*(this.x-other.x) + (this.y-other.y)*(this.y-other.y);
	}
	public double dist(double x, double y)
	{
		return Math.sqrt(dist2(x,y));
	}
	public double dist2(double x, double y)
	{
		return (this.x-x)*(this.x-x) + (this.y-y)*(this.y-y);
	}

	public void copyInto(Vec2 target)
	{
		target.x = this.x;
		target.y = this.y;
	}
	
	public String toString()
	{
		return "("+x+","+y+")";
	}

	public static final Vec2 zero = new Vec2();
	public static final Vec2 right = new Vec2(1,0);
	public static final Vec2 left = new Vec2(-1,0);
	public static final Vec2 up = new Vec2(0,1);
	public static final Vec2 down = new Vec2(0,-1);

	/** Returns the vector with the least length, unless one is null, in which
	 * case returns the other vector.
	 * @return The vector, or null if both vectors are null.
	 */
	public static Vec2 min(Vec2 v1, Vec2 v2)
	{
		if(v1 == null) return v2;
		if(v2 == null) return v1;
		return v1.length2() < v2.length2() ? v1 : v2;
	}

	/** Returns the vector with the greatest length, unless one is null, in which
	 * case returns the other vector.
	 * @return The vector, or null if both vectors are null.
	 */
	public static Vec2 max(Vec2 v1, Vec2 v2)
	{
		if(v1 == null) return v2;
		if(v2 == null) return v1;
		return v1.length2() > v2.length2() ? v1 : v2;
	}

	public static Vec2 mix(double val, Vec2 v1, Vec2 v2)
	{
		Vec2 v = new Vec2();
		v.x = v1.x * val + v2.x * (1 - val);
		v.y = v1.y * val + v2.y * (1 - val);
		return v;
	}

	private static class VecXComparer implements Comparator<Vec2>
	{
		public int compare(Vec2 a, Vec2 b)
		{
			return Double.compare(a.x, b.x);
		}
	}
	private static class VecYComparer implements Comparator<Vec2>
	{
		public int compare(Vec2 a, Vec2 b)
		{
			return Double.compare(a.y, b.y);
		}
	}

	private final static VecXComparer xcomp = new VecXComparer();
	private final static VecYComparer ycomp = new VecYComparer();

	public static Comparator<Vec2> xComparator() { return xcomp; }
	public static Comparator<Vec2> yComparator() { return ycomp; }

	public static double dist(double x1, double y1, double x2, double y2)
	{
		double dx = x2-x1;
		double dy = y2-y1;
		return Math.sqrt(dx*dx + dy*dy);
	}

	public static final double EPSILON = Math.pow(2,-53);
}
