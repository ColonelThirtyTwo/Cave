
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
import java.io.File;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;

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
	}

	private static Map<String,Texture> images;
	private static Map<String,Font> fonts;
	private static Map<String,Audio> audio;
	static
	{
		images = new HashMap<String,Texture>();
		fonts = new HashMap<String,Font>();
		audio = new HashMap<String,Audio>();
	}

	/**
	 * Loads an image and caches it for future calls to getImage.
	 * @param path Path from res/images/ to load.
	 */
	public static Texture getImage(String path)
	{
		return getImage(path, GL11.GL_NEAREST);
	}

	public static Texture getImage(String path, int filter)
	{
		Texture t = images.get(path);
		if(t != null) return t;

		try
		{
			log.log(Level.FINE, "Loading resource /res/images/{0}", path);
			t = TextureLoader.getTexture("PNG", new FileInputStream("res/images/"+path),filter);
			log.log(Level.FINER, "Width:{0}, Height:{1}, Image Width:{2}, Image Height:{3}, Texture Width:{4}, Texture Height:{5}",
					new Object[] {t.getWidth(), t.getHeight(), t.getImageWidth(), t.getImageHeight(),
					t.getTextureWidth(), t.getTextureHeight()});
		}
		catch(IOException err)
		{
			throw new RenderError("Could not load image from path: images/"+path,err);
		}
		images.put(path, t);
		return t;
	}

	/**
	 * Loads a font and caches it for future calls to getFont.
	 * @param name Name of font
	 * @see java.awt.Font
	 */
	public static Font getFont(String name)
	{
		Font f = fonts.get(name);
		if(f != null) return f;

		// Depreciation method tells me to use "UnicodeFont"
		// What the heck is that?
		f = new TrueTypeFont(new java.awt.Font(name,java.awt.Font.PLAIN,fontRenderSize),true);
		fonts.put(name, f);
		return f;
	}

	public static Audio getAudio(String name, String format)
	{
		Audio f = audio.get(name);
		if(f != null) return f;

		try
		{
			if(format.equals("OGG"))
			{
				log.log(Level.FINE, "Streaming audio /res/sounds/{0}", name);
				f = AudioLoader.getStreamingAudio(format, new File("res/sounds/"+name).toURI().toURL());
			}
			else
			{
				log.log(Level.FINE, "Loading audio /res/sounds/{0}", name);
				f = AudioLoader.getAudio(format, new FileInputStream("res/sounds/"+name));
			}
		}
		catch(IOException io)
		{
			throw new RenderError("IO Error while loading /res/sound/"+name, io);
		}
		audio.put(name, f);
		return f;
	}

	/**
	 * Releases an image.
	 */
	public static void unloadImage(String name)
	{
		images.remove(name).release();
	}

	public static void unloadAll()
	{
		for(Map.Entry<String,Texture> entry : images.entrySet())
			entry.getValue().release();
		images.clear();

		// TODO: Do we need to release?
		fonts.clear();

		for(Map.Entry<String,Audio> e : audio.entrySet())
		{
			Audio a = e.getValue();
			if(a.isPlaying()) a.stop();
		}
		audio.clear();
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
