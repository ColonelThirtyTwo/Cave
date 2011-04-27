
package cave2.structures;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import cave2.structures.exceptions.RenderError;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Colonel32
 */
public class ResourceManager
{
	private static final Logger log = LogU.getLogger();

	public static int fontRenderSize = 12;

    private ResourceManager()
	{
		images = new HashMap<String,Texture>();
		fonts = new HashMap<String,Font>();
	}

	private Map<String,Texture> images;
	private Map<String,Font> fonts; // "Use UnicodeFont" It doesn't exist...

	/**
	 * Loads an image and caches it for future calls to getImage.
	 * @param path Path from res/images/ to load.
	 */
	public Texture getImage(String path)
	{
		Texture t = images.get(path);
		if(t != null) return t;

		try
		{
			log.log(Level.FINE, "Loading resource /res/images/{0}", path);
			t = TextureLoader.getTexture("PNG", new FileInputStream("res/images/"+path),GL11.GL_NEAREST);
			log.log(Level.FINER, "Width:{0}, Height:{1}, Image Width:{2}, Image Height:{3}, Texture Width:{4}, Texture Height:{5}",
					new Object[] {t.getWidth(), t.getHeight(), t.getImageWidth(), t.getImageHeight(),
					t.getTextureWidth(), t.getTextureHeight()});
		}
		catch(IOException err)
		{
			throw new RenderError("Could not load image from path: images/"+path+" (error: "+err.getMessage()+")");
		}
		images.put(path, t);
		return t;
	}

	/**
	 * Loads a font and caches it for future calls to getFont.
	 * @param name Name of font
	 * @see java.awt.Font
	 */
	public Font getFont(String name)
	{
		Font f = fonts.get(name);
		if(f != null) return f;

		f = new TrueTypeFont(new java.awt.Font(name,java.awt.Font.PLAIN,fontRenderSize),true);
		fonts.put(name, f);
		return f;
	}

	/**
	 * Releases an image.
	 */
	public void unloadImage(String name)
	{
		images.remove(name).release();
	}

	public void unloadAll()
	{
		for(Map.Entry<String,Texture> entry : images.entrySet())
			entry.getValue().release();
		images.clear();

		// TODO: Do we need to release?
		fonts.clear();
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
