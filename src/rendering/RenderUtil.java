package rendering;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

/**
 * Groups and abstracts a few OpenGL calls
 * @author Colonel32
 */
public final class RenderUtil
{
	private RenderUtil() {}

	private static int width = 0, height = 0;
	private static boolean isFullscreen = false;

	public static void initialize(DisplayMode mode, boolean fullscreen) throws RenderError
	{
		width = mode.getWidth();
		height = mode.getHeight();
		isFullscreen = fullscreen;
		try
		{
			Display.setDisplayMode(mode);
			Display.setFullscreen(fullscreen);
			Display.setVSyncEnabled(fullscreen);
			Display.create();
		}
		catch(LWJGLException e)
		{
			throw new RenderError("Error initializing LWJGL: "+e.getLocalizedMessage());
		}

//		if(!GLContext.getCapabilities().GL_ARB_vertex_buffer_object)
//		{
//			throw new RenderError("Error initializing LWJGL: Graphics card does not support VBO's.");
//		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMaterial (GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glViewport(0,0,width,height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void destroy()
	{
		Display.destroy();
	}

	public static void update()
	{
		Display.update();
		int err = GL11.glGetError();
		if(err != GL11.GL_NO_ERROR)
			throw new RenderError("GL error: "+GLU.gluErrorString(err));
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static int width() { return width; }
	public static int height() { return height; }
	public static boolean isFullscreen() { return isFullscreen; }


	/**
	 * Draws a box, with one of its corners at (x,y) and dimensions w, h.
	 * Any OpenGL settings set before this method will pass into this context.
	 */
	public static void drawBox(double x, double y, double w, double h)
	{
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x,y);
			GL11.glVertex2d(x+w,y);
			GL11.glVertex2d(x+w,y+h);
			GL11.glVertex2d(x,y+h);
		GL11.glEnd();
	}

	/**
	 * Draws an image without lighting
	 */
	public static void drawImage(Texture image, double x, double y, double w, double h)
	{
		RenderUtil.drawImage(image, x, y, w, h, 1);
	}

	/**
	 * Draws an image with the specified brightness
	 */
	public static void drawImage(Texture image, double x, double y, double w, double h, double brightness)
	{
		RenderUtil.drawImage(image, x, y, w, h, brightness, 0, 0, 1, 1);
	}

	public static void drawImage(Texture image, double x, double y, double w, double h, double brightness,
			double u, double v, double texw, double texh)
	{
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//Color.white.bind();
		GL11.glColor3d(brightness, brightness, brightness);
		image.bind();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(u,v);
			GL11.glVertex2d(x,y);
			GL11.glTexCoord2d(u+texw,v);
			GL11.glVertex2d(x+w,y);
			GL11.glTexCoord2d(u+texw,v+texh);
			GL11.glVertex2d(x+w,y+h);
			GL11.glTexCoord2d(u,v+texh);
			GL11.glVertex2d(x,y+h);
		GL11.glEnd();
	}
}
