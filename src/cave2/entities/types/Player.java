
package cave2.entities.types;

import cave2.entities.CollisionEntity;
import cave2.entities.Creature;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.opengl.Texture;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;

/**
 * Player. Moves with keyboard input.
 * @author Colonel32
 */
public class Player extends Creature
{

	private double markerX, markerY;

	public Player(double x, double y)
	{
		super(x,y,.5,.5,100.0);
	}

	public void collidedWith(CollisionEntity e) {}

	public void think(int delta)
	{
		move(delta);
		doTileCollisions();
		doTileCallbacks();
	}

	public void draw(AABB abox)
	{
		if(!abox.overlaps(box)) return;
		
		Texture img = ResourceManager.getInstance().getImage("entities/player.png");
		RenderUtil.drawImage(img, box, 1.0);

		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		Font f = ResourceManager.getInstance().getFont("monospace");
		f.drawString(0, 50, String.format("Clicked: %.2f,%.2f", markerX, markerY), Color.yellow);

		GL11.glPopMatrix();
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
		if(isdown)
		{
			markerX = x;
			markerY = y;
		}
	}
	/**
	 * Same as mainUse, except triggers the weapon/tool's alt fire.
	 * @param x X position in world coordinates
	 * @param y Y position in world coordinates
	 * @param isdown true = start use, false = end use.
	 */
	public void altUse(double x, double y, boolean isdown)
	{}
}
