
package cave2.rendering.hud;

import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class Inventory extends HudElement
{

	private AABB box;
	private int selectedx, selectedy;
	private Player ply;

	public Inventory(Player ply)
	{
		Texture t = ResourceManager.getInstance().getImage("hud/inventory.png");
		box = new AABB(RenderUtil.width() / 2 - t.getImageWidth(), RenderUtil.height() / 2 - t.getImageHeight(),
				t.getImageWidth(), t.getImageHeight());
		selectedx = selectedy = -1;
		this.ply = ply;
	}

	public void draw()
	{
		Texture t = ResourceManager.getInstance().getImage("hud/inventory.png");
		RenderUtil.drawImage(t, box, 1);

		int mouseOverCol = getInvCol((int)(Mouse.getX() - box.center.x + box.size.x));
		int mouseOverRow = getInvRow((int)(Mouse.getY() - box.center.y + box.size.y));
		if(mouseOverCol == -1) mouseOverRow = -1;
		if(mouseOverRow == -1) mouseOverCol = -1;

		int imgh = (int)(box.center.y + box.size.y);
		int imgw = (int)(box.center.x - box.size.x) + 2;

		for(int r=0; r<ply.inventory.length; r++)
		{
			int y = r == 0 ? imgh - 20 : imgh - 25 - 20 * r;
			for(int c=0; c<ply.inventory[r].length; c++)
			{
				int x = imgw + 20 * c;

				GL11.glPushMatrix();
				GL11.glTranslated(x, y, 0);
				GL11.glScaled(16, 16, 1);
				ply.inventory[r][c].drawIcon();
				GL11.glPopMatrix();
			}
		}
	}

	protected int getInvCol(int screenx)
	{
		screenx -= 2;
		if(screenx < 0 || screenx > 164) return -1; // Mouse is on border
		if(screenx % 18 <= 2) return -1; // Mouse is in middle space
		return screenx / 18;
	}

	protected int getInvRow(int screeny)
	{
		if(screeny > 86 && screeny < 102)
		{
			return 0;
		}
		else if(screeny > 25 && screeny < 79)
		{
			// On row
			screeny -= 25;
			if(screeny % 18 <= 2) return -1;
			return 4 - screeny / 18;
		}
		else
		{
			return -1;
		}
	}

	public void think(int timedelta)
	{

	}

	public boolean mouseClicked(int screenx, int screeny, boolean isDown)
	{
		if(!box.contains(screenx, screeny)) return false;
		return true;
	}

	public boolean keyPressed(int key, boolean isDown)
	{
		return false;
	}
}
