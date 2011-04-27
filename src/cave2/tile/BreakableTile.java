
package cave2.tile;

/**
 * A tile with a damage value.
 * @author Colonel 32
 */
public interface BreakableTile extends Tile
{
	/**
	 * Damages the tile.
	 * @param damagetype Type of damage (ex, normal, fire, electricity), or null to apply no modifier
	 */
	public void damage(String damagetype, double amount);

	/**
	 * Returns the health that the tile has
	 */
	public double getHealth();
}
