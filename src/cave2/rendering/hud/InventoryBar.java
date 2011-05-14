
package cave2.rendering.hud;

import cave2.entities.types.Player;
import cave2.rendering.RenderUtil;
import cave2.structures.ResourceManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 * Shows the first row of a player's inventory on the HUD.
 * @author Colonel 32
 */
public class InventoryBar extends HudElement
{
	private Player ply;
	public InventoryBar(Player p)
	{
		ply = p;
	}

	public void draw()
	{
		Texture image = ResourceManager.getImage("hud/inventory_bar.png");
		final int scale = 2;
		float posx = RenderUtil.width() - 164*scale;
		float posy = RenderUtil.height() - 20*scale;
		RenderUtil.drawImage(image,
				RenderUtil.width() - image.getTextureWidth()*scale, RenderUtil.height() - image.getTextureHeight()*scale,
				image.getTextureWidth()*scale, image.getTextureHeight()*scale);

		for(int i=0; i<ply.inventory[0].length; i++)
		{
			if(ply.inventory[0][i] == null) continue;
			GL11.glPushMatrix();
			GL11.glTranslated(posx+(2+18*i)*scale, posy+2*scale, 0);
			GL11.glScaled(16.0*scale, 16.0*scale, 0);
			ply.inventory[0][i].drawIcon();
			GL11.glPopMatrix();
		}
		
		image = ResourceManager.getImage("hud/inventory_selected.png");
		RenderUtil.drawImage(image, posx+(2+18*ply.getEquippedSlot())*scale,posy+2*scale,16*scale,16*scale);
	}

	public void think(int timedelta)
	{

	}

	public boolean mouseClicked(int screenx, int screeny, boolean isDown)
	{
		return false;
	}

	public boolean keyPressed(int key, boolean isDown)
	{
		return false;
	}

}
