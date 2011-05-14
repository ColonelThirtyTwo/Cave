
package cave2.entities;

import cave2.entities.types.Player;
import cave2.structures.ResourceManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

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
		this(x,y,1);
	}

	public StackableItem(double x, double y, int count)
	{
		super(x,y);
		this.count = count;
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
				if(ply.inventory[r][c] == null)
				{
					if(nullr == -1)
					{
						// Found an empty spot, record it just in case
						nullr = r;
						nullc = c;
					}
				}
				else if(ply.inventory[r][c].getClass().equals(this.getClass()))
				{
					// Found matching item
					StackableItem item = (StackableItem)ply.inventory[r][c];
					if(item.getCount() < item.getMaximum())
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
			}
		}
		// Didn't merge with other stacks
		if(nullr != -1)
		{
			ply.setItem(nullr, nullc, this);
			this.ply = ply;
			getWorld().removeEntity(this);
			world = null;
		}
	}

	/**
	 * Draws the item count onto an icon
	 */
	protected void drawItemCount()
	{
		Font f = ResourceManager.getFont("monospace");
		GL11.glPushMatrix();
		GL11.glScaled(0.02, 0.02, 1);
		f.drawString(0.0f, 0.0f, Integer.toString(getCount()), Color.red);
		GL11.glPopMatrix();
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
		if(count <= 0)
		{
			if(world != null)
				world.removeEntity(this);
			else if(ply != null && ply.getEquippedItem() == this)
				ply.inventory[0][ply.getEquippedSlot()] = null;
		}
	}

	public abstract int getMaximum();
}
