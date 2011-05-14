
package cave2.rendering.hud;

import java.util.logging.Logger;
import cave2.entities.Item;
import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.LogU;
import cave2.structures.ResourceManager;
import cave2.structures.Vec2;
import java.util.logging.Level;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Colonel 32
 */
public class Inventory extends HudElement
{
	private static final Logger log = LogU.getLogger();

	private static final int SCALE = 2;
	
	private static final int INVBORDER_W = 2;
	private static final int INVBORDER_H = 2;
	private static final int SLOT_W = 16;
	private static final int SLOT_H = -16;
	private static final int SLOTBORDER_W = 3;
	private static final int SLOTBORDER_H = -3;
	private static final int INVOFFSET_W = 0;
	private static final int INVOFFSET_H = 98 - SLOT_W;


	private AABB box;
	//private AABB[][] grid;
	private int selectedr, selectedc;
	private Player ply;

	public Inventory(Player ply)
	{
		Texture t = ResourceManager.getImage("hud/inventory2.png");
		box = new AABB(RenderUtil.width() / 2, RenderUtil.height() / 2,
				t.getImageWidth()/2*SCALE, t.getImageHeight()/2*SCALE);
		selectedr = selectedc = -1;

//		grid = new AABB[ply.inventory.length][ply.inventory[0].length];
//
//		int sloth = Math.abs(SLOT_H) * SCALE / 2;
//		int slotw = Math.abs(SLOT_W) * SCALE / 2;
//
//		for(int r=0; r<grid.length; r++)
//		{
//			for(int c=0; c<grid[0].length; c++)
//			{
//				double x = box.center.x - box.size.x + SCALE*(INVBORDER_W + INVOFFSET_W + (c+1)*SLOTBORDER_W + (c+0.5)*SLOT_W);
//				double y = box.center.y - box.size.y + SCALE*(INVBORDER_H + INVOFFSET_H + (r+1)*SLOTBORDER_H + (r+0.5)*SLOT_H);
//				AABB b = new AABB(x,y,slotw,sloth);
//				grid[r][c] = b;
//			}
//		}

		this.ply = ply;
	}

	public int getColumn(int x)
	{
		double r = x - box.center.x + box.size.x;
		r += SCALE * (INVBORDER_W + INVOFFSET_W + SLOTBORDER_W - SLOT_W / 2.0);
		r /= (SLOTBORDER_W + SLOT_W) * SCALE;
		if(r % 1 <= SLOTBORDER_W / (double)(SLOTBORDER_W + SLOT_W)) return -1;
		return (int)r;
	}

	public int getRow(int y)
	{
		double r = y - box.center.y;
		r += SCALE * (INVBORDER_H + INVOFFSET_H + Math.abs(SLOTBORDER_H));
		r /= Math.abs(SLOTBORDER_H + SLOT_H) * SCALE;
		if(r % 1 <= SLOTBORDER_H / (double)(SLOTBORDER_H + SLOT_H)) return -1;
		return (int)r-2;
	}

	public void draw()
	{
		Texture t = ResourceManager.getImage("hud/inventory2.png");
		RenderUtil.drawImage(t, box, 1, 0, 0, t.getWidth(), t.getHeight());

		GL11.glPushMatrix();
		GL11.glTranslated(box.center.x - box.size.x, box.center.y - box.size.y, 0);

		int slot_imgw = Math.abs(SLOT_W)*SCALE;
		int slot_imgh = Math.abs(SLOT_H)*SCALE;

		int mousex = Mouse.getX();
		int mousey = Mouse.getY();

		int over_c = getColumn(mousex);
		int over_r = getRow(mousey);

		t = ResourceManager.getImage("colors/red.png");
		for(int r=0; r<ply.inventory.length; r++)
		{
			int y = (INVOFFSET_H + INVBORDER_H + (r+1) * SLOTBORDER_H + r * SLOT_H)*SCALE;
			for(int c=0; c<ply.inventory[r].length; c++)
			{
				int x = (INVOFFSET_W + INVBORDER_W + (c+1) * SLOTBORDER_W + c * SLOT_W)*SCALE;

				GL11.glPushMatrix();
				GL11.glTranslated(x, y, 0);
				GL11.glScaled(slot_imgw, slot_imgh, 1);
				if(ply.inventory[r][c] != null) ply.inventory[r][c].drawIcon();
				if((r == over_r && c == over_c) || (r == selectedr && c == selectedc))
				{
					Texture overimg = ResourceManager.getImage("hud/inventory_selected.png");
					RenderUtil.drawImage(overimg, 0, 0, 1, 1);
				}

				//RenderUtil.drawImage(t, grid[r][c], 1);

				GL11.glPopMatrix();
			}
		}

		GL11.glPopMatrix();
	}

	public void think(int timedelta)
	{

	}

	public boolean mouseClicked(int screenx, int screeny, boolean isDown)
	{
		if(!box.contains(screenx, screeny))
		{
			if(isDown)
				return false;
			else if(selectedr != -1)
			{
				Item i = ply.inventory[selectedr][selectedc];
				ply.setItem(selectedr, selectedc, null);
				//ply.inventory[selectedr][selectedc] = null;

				Vec2.buffer.set(ply.getX(), ply.getY());
				Vec2.buffer2.set(screenx, screeny).subInto(RenderUtil.width()/2, RenderUtil.height()/2);
				Vec2.buffer2.normalizeInto();
				Vec2.buffer.addInto(Vec2.buffer2.mulInto(1.5));
				i.setPos(Vec2.buffer.x, Vec2.buffer.y);
				ply.getWorld().addEntity(i);

				selectedr = selectedc = -1;

				log.log(Level.FINER, "Player {0} dropped item {1} to {2}", new Object[]{ply, i, Vec2.buffer});

				return true;
			}
			else
				return false;
		}
		else
		{
			if(isDown)
			{
				int r = getRow(screeny);
				int c = getColumn(screenx);

				if(r >= 0 && r < ply.inventory.length && c >= 0 && c < ply.inventory[r].length)
				{
					selectedr = r;
					selectedc = c;
				}
			}
			else if(selectedr >= 0)
			{
				int r = getRow(screeny);
				int c = getColumn(screenx);

				if(r >= 0 && r < ply.inventory.length && c >= 0 && c < ply.inventory[r].length)
				{
					Item t = ply.inventory[selectedr][selectedc];
					ply.inventory[selectedr][selectedc] = ply.inventory[r][c];
					ply.inventory[r][c] = t;
				}
				selectedr = selectedc = -1;
			}

			return true;
		}
	}

	public boolean keyPressed(int key, boolean isDown)
	{
		return false;
	}
}
