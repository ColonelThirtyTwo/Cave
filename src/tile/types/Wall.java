
package tile.types;

import entities.Entity;
import org.newdawn.slick.opengl.Texture;
import structures.ResourceManager;
import tile.Meshable;

/**
 * A wall, used as a Meshable test type.
 * @author Colonel32
 */
public class Wall extends Meshable
{
	public Wall()
	{
	}

	protected int getMeshGroup()
	{
		return Meshable.MESH_WALL;
	}
	protected Texture getTileset()
	{
		return ResourceManager.getInstance().getImage("tiles/wall.png");
	}

	public void entityEntered(Entity e) {}
	public void entityExited(Entity e) {}

	public boolean isAnimated() { return false; }
	public boolean isSolid() { return true; }
	public boolean isTransparent() { return false; }
}
