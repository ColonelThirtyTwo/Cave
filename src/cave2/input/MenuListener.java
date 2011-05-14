package cave2.input;

import cave2.entities.types.Player;
import cave2.rendering.hud.Inventory;
import cave2.rendering.hud.Shop;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Colonel32
 */
public class MenuListener extends InputCallback
{
	public static int KEY_INVENTORY = Keyboard.KEY_Q;
	public static int KEY_SHOP = Keyboard.KEY_T;

	private Inventory inventory;
	private boolean inventoryOpen;

	private Shop shop;
	private boolean shopOpen;

	public MenuListener(Player ply)
	{
		inventory = new Inventory(ply);
		inventoryOpen = false;

		shop = new Shop();
		shopOpen = false;
	}

	public void keyEvent(int key, boolean down)
	{
		if(!down) return;

		if(key == KEY_INVENTORY)
		{
			inventoryOpen = !inventoryOpen;
			if(inventoryOpen) inventory.enable();
			else inventory.disable();
		}
		else if(key == KEY_SHOP)
		{
			shopOpen = !shopOpen;
			if(shopOpen) shop.enable();
			else shop.disable();
		}
	}

	public void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel)
	{

	}

	public void mouseButtonEvent(int x, int y, int button, boolean down)
	{

	}
	
}
