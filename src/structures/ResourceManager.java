
package structures;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import rendering.RenderError;

/**
 *
 * @author Colonel32
 */
public class ResourceManager
{
	private final static Logger log = Logger.getLogger("cave2");

    private ResourceManager()
	{
		images = new HashMap<String,Texture>();
	}

	private Map<String,Texture> images;

	public Texture getImage(String path)
	{
		Texture t = images.get(path);
		if(t != null) return t;

		try
		{
			t = TextureLoader.getTexture("PNG", new FileInputStream("res/images/"+path));
		}
		catch(IOException err)
		{
			throw new RenderError("Could not load image from path: images/"+path+" (error: "+err.getMessage()+")");
		}
		images.put(path, t);
		return t;
	}

	public void releaseAll()
	{
		for(Map.Entry<String,Texture> entry : images.entrySet())
		{
			entry.getValue().release();
		}
		images.clear();
	}

    public static ResourceManager getInstance()
	{
        return ResourceManagerHolder.INSTANCE;
    }

    private static class ResourceManagerHolder {
        private static final ResourceManager INSTANCE = new ResourceManager();
		private ResourceManagerHolder() {}
    }
 }
