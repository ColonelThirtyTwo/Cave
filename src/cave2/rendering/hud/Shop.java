
package cave2.rendering.hud;

import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class Shop extends HudElement
{

	public static interface ShopItem
	{
		public int buyPrice();
		public boolean purchace(Player ply);
	}

	private static final int SCALE = 2;

	private static final int INVBORDER_W = 2;
	private static final int INVBORDER_H = 2;
	private static final int SLOT_W = 16;
	private static final int SLOT_H = -16;
	private static final int SLOTBORDER_W = 10;
	private static final int SLOTBORDER_H = 10;
	private static final int INVOFFSET_W = 0;
	private static final int INVOFFSET_H = 0;

	private static ArrayList<ShopItem> items = new ArrayList<ShopItem>();

	private int page;
	private AABB panelBox;
	private AABB scrollRight;
	private AABB scrollLeft;

	public Shop()
	{
		page = 0;
		Texture t = ResourceManager.getImage("hud/shop.png");
		panelBox = new AABB(RenderUtil.width()/2, RenderUtil.height()/2,
				t.getImageWidth()/2*SCALE, t.getImageHeight()/2*SCALE);
		scrollLeft = new AABB(panelBox.center.x + 44.5 * SCALE, panelBox.center.y + 43.5*SCALE,
				3.5*SCALE, 3.5*SCALE);
		scrollRight = new AABB(panelBox.center.x + 52.5*SCALE, panelBox.center.y + 43.5*SCALE,
				3.5*SCALE, 3.5*SCALE);
		
	}

	public void draw()
	{
		Texture t = ResourceManager.getImage("hud/shop.png");
		RenderUtil.drawImage(t, panelBox, 1, 0, 0, t.getWidth(), t.getHeight());

//		t = ResourceManager.getImage("colors/red.png");
//		RenderUtil.drawImage(t, scrollLeft, 1);
//		t = ResourceManager.getImage("colors/blue.png");
//		RenderUtil.drawImage(t, scrollRight, 1);
	}

	public void think(int timedelta)
	{
		
	}

	public boolean mouseClicked(int screenx, int screeny, boolean isDown)
	{
		if(!panelBox.contains(screenx, screeny)) return false;

		return true;
	}

	public boolean keyPressed(int key, boolean isDown)
	{
		return false;
	}

}
