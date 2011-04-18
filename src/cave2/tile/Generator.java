
package cave2.tile;

/**
 * Interface for chunk generators
 * @author Colonel32
 */
public interface Generator
{
	public void generate(World w, Chunk c, int cx, int cy);
}
