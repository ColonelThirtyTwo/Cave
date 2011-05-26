
package cave2.entities.types;

import cave2.entities.CollisionEntity;
import cave2.entities.Creature;
import cave2.entities.Item;
import org.newdawn.slick.opengl.Texture;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;

/**
 * The player. Has an inventory and everything.
 * @author Colonel32
 */
public class Player extends Creature
{
	public Item[][] inventory;

	private int recovertime;
	private int blinktimer;
	
	/**
	 * Column of the currently equipped item. Item must be on the first row.
	 */
	protected int equippedItem;

	public Player(double x, double y)
	{
		super(x,y,0.4,0.4,100.0);
		inventory = new Item[4][9];
		equippedItem = 0;
		recovertime = 0;
		blinktimer = 0;
	}

	public void collidedWith(CollisionEntity e)
	{
		if(e instanceof Item)
		{
			Item it = (Item) e;
			it.insertIntoInventory(this);
		}
	}

	public void think(int delta)
	{
		if(world == null) return;
		move(delta);
		doTileCollisions();
		doTileCallbacks();

		if(recovertime > 0) recovertime -= delta;
		else blinktimer = recovertime = 0;

		if(health < 100) health += delta * 0.001;

		if(inventory[0][equippedItem] != null)
			inventory[0][equippedItem].think(delta);
	}

	public void draw(AABB abox)
	{
		if(world == null) return;
		if(!abox.overlaps(box)) return;

		Texture img = ResourceManager.getImage("entities/player.png");

		if(recovertime > 0)
		{
			blinktimer++;
			if((blinktimer / 2) % 2 == 0)
				RenderUtil.drawImage(img, box, 1.0);
		}
		else
			RenderUtil.drawImage(img, box, 1.0);
	}

	protected double getSpeed() { return 5.0; }

	/**
	 * Main weapon/tool fire
	 * @param x X position in world coordinates
	 * @param y Y position in world coordinates
	 * @param isdown true = start use, false = end use.
	 */
	public void mainUse(double x, double y, boolean isdown)
	{
		if(inventory[0][equippedItem] != null)
			inventory[0][equippedItem].mainUse(this, x, y, isdown);
	}
	/**
	 * Same as mainUse, except triggers the weapon/tool's alt fire.
	 * @param x X position in world coordinates
	 * @param y Y position in world coordinates
	 * @param isdown true = start use, false = end use.
	 */
	public void altUse(double x, double y, boolean isdown)
	{
		if(inventory[0][equippedItem] != null)
			inventory[0][equippedItem].altUse(this, x, y, isdown);
	}

	public int getEquippedSlot()
	{
		return equippedItem;
	}

	/**
	 * Returns the currently equipped item
	 */
	public Item getEquippedItem()
	{
		return inventory[0][equippedItem];
	}

	public void setItem(int row, int column, Item item)
	{
		Item olditem = inventory[row][column];
		inventory[row][column] = item;

		if(row == 0 && column == equippedItem)
		{
			if(olditem != null) olditem.unequip();
			if(item != null) item.equip();
		}
	}

	public void setEquippedSlot(int slot)
	{
		int oldslot = equippedItem;
		equippedItem = slot % inventory[0].length;
		if(equippedItem < 0) equippedItem += inventory[0].length;

		if(inventory[0][oldslot] != null) inventory[0][oldslot].unequip();
		if(inventory[0][equippedItem] != null) inventory[0][equippedItem].equip();
	}

	public void damage(String type, double damage)
	{
		if(world == null) return;
		if(recovertime > 0) return;
		super.damage(type,damage);
		if(world != null)
			recovertime = 1000;
	}
}
