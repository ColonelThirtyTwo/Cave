
package cave2.entities;

import cave2.entities.types.Player;

/**
 * Stackable Item. Instead of inserting the item into another place in the inventory,
 * sub-implementations will "stack" together if other.getClass().equals(this.getClass())
 * @author Colonel32
 */
public abstract class StackableItem extends Item
{
	protected int count;

	public StackableItem(double x, double y)
	{
		super(x,y);
		count = 1;
	}

	public void insertIntoInventory(Player ply)
	{
		if(world == null) return;
		int nullr = -1, nullc = -1;

		// Find a matching item, and an empty space (just in case)
		for(int r=0; r<ply.inventory.length; r++)
		{
			for(int c=0; c<ply.inventory[r].length; c++)
			{
				if(ply.inventory[r][c].getClass().equals(this.getClass()))
				{
					// Found matching item
					StackableItem item = (StackableItem)ply.inventory[r][c];
					if(item.getCount() >= item.getMaximum())
					{
						// The other item is not full.
						int transfer = Math.min(count,item.getMaximum() - item.getCount());
						item.setCount(item.getCount() + transfer);
						count -= transfer;

						if(count == 0)
						{
							// Out of items to distribute, destory self.
							getWorld().removeEntity(this);
							return;
						}
						assert(count > 0);
					}
				}
				else if(nullr != -1 && ply.inventory[r][c] == null)
				{
					// Found an empty spot, record it just in case
					nullr = r;
					nullc = c;
				}
			}
		}
		// Didn't merge with other stacks
		ply.setItem(nullr, nullc, this);
		getWorld().removeEntity(this);
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public abstract int getMaximum();
}
