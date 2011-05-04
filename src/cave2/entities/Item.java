
package cave2.entities;

import cave2.entities.types.Player;

/**
 * Abstract item class.
 * @author Colonel32
 */
public abstract class Item extends CollisionEntity
{
	protected Player ply;

	public Item(double x, double y)
	{
		super(x,y,0.3,0.3);
	}

	/**
	 * Puts the item into the player's inventory. Also may remove the item.
	 * @param ply Player to give item to.
	 */
	public void insertIntoInventory(Player ply)
	{
		if(world == null) return;
		for(int r=0; r<ply.inventory.length; r++)
			for(int c=0; c<ply.inventory[r].length; c++)
				if(ply.inventory[r][c] == null)
				{
					//ply.inventory[r][c] = this;
					ply.setItem(r, c, this);
					getWorld().removeEntity(this);
					this.ply = ply;
					return;
				}
	}

	/**
	 * Does entity logic. This is called every frame that the item is dropped
	 * in the world and every frame when the item is equipped.
	 */
	public void think(int timedelta)
	{
		if(world == null) return;
		doTileCollisions();
		doTileCallbacks();
	}

	/**
	 * Called when the player equips the item in the inventory
	 */
	public abstract void equip();

	/**
	 * Called when the player equips another item in the inventory
	 */
	public abstract void unequip();

	public abstract void mainUse(Player ply, double x, double y, boolean isdown);
	public abstract void altUse(Player ply, double x, double y, boolean isdown);

	/**
	 * Draws an icon for the image. should draw from (0,0) to (1,1).
	 */
	public abstract void drawIcon();

	/**
	 * @return How much this item costs to sell, or -1 if it is not sellable
	 */
	public int getSellValue()
	{
		return -1;
	}
	
	public int getBuyValue()
	{
		return -1;
	}
}
