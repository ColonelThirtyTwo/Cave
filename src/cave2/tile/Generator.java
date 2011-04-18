
package cave2.tile;

/**
 * Interface for chunk generators
 * @author Colonel32
 */
public interface Generator
{
	public Chunk generate(World w, int cx, int cy);
}
