
package cave2.entities;

/**
 * CollisionEntity with a damage and movement system.
 * @author Colonel32
 */
public abstract class Creature extends CollisionEntity
{
	protected double health;
	protected int dirx, diry;

	public Creature(double x, double y, double w, double h, double initialHealth)
	{
		super(x,y,w,h);
		dirx = diry = 0;
		health = initialHealth;
	}

	/**
	 * Deals damage to the entity. Overwriting classes may want to scale damage
	 * based on the type.
	 * @param type The type of damage (ex. normal, fire, electricity)
	 * @param damage Amount of damage to deal. Can be negative for healing.
	 */
	public void damage(String type, double damage)
	{
		health -= damage;
		if(health < 0) onDeath();
	}

	/**
	 * Called when the entity goes below 0 health. The default function is to
	 * remove itself from the world.
	 */
	protected void onDeath()
	{
		this.removedFromWorld(world);
	}

	public double getHealth() { return health; }

	/**
	 * @return Entity speed, in blocks per second
	 */
	protected abstract double getSpeed();

	/**
	 * Moves the entity based on the current values of dirx, diry, and getSpeed()
	 * Does not do collisions
	 * @param timeDeltaMS Change in time, in milliseconds
	 */
	protected void move(int timeDeltaMS)
	{
		double step = getSpeed() * timeDeltaMS / 1000.0;
		box.center.x += dirx * step;
		box.center.y += diry * step;
	}

	/**
	 * Sets the creature x movement velocity.
	 * 1  = move to right at normal speed
	 * 0  = stop
	 * -1 = move to left at normal speed
	 */
	public void setMovementX(int x) { dirx = x; }
	/**
	 * Sets the creature y movement velocity.
	 * 1  = move downward at normal speed
	 * 0  = stop
	 * -1 = move upward at normal speed
	 */
	public void setMovementY(int y) { diry = y; }
}
