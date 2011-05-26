
package cave2.rendering.camera;

import cave2.entities.Entity;
import cave2.rendering.RenderUtil;
import cave2.structures.AABB;
import cave2.structures.ResourceManager;
import org.newdawn.slick.opengl.Texture;

/**
 * Camera that follows an entity
 * @author Colonel32
 */
public class EntFollowCamera extends AbstractCamera
{
	protected Entity ent;
	protected double w, h;
	protected double lastx, lasty;
	protected AABB box;

	public EntFollowCamera(Entity following, double w, double h)
	{
		super();
		ent = following;
		this.w = w/2;
		this.h = h/2;
		box = new AABB(ent.getX(), ent.getY(), this.w, this.h);
	}

	public void draw()
	{
		super.draw();
		if(ent.getWorld() == null)
		{
			Texture t = ResourceManager.getImage("hud/ded.png");
			RenderUtil.drawImage(t, (RenderUtil.width()-t.getImageWidth())/2,
					(RenderUtil.height()-t.getImageHeight())/2, t.getImageWidth(), t.getImageHeight());
		}
	}

	public AABB getCameraBounds()
	{
		box.center.x = ent.getX();
		box.center.y = ent.getY();
		return box;
	}
}
