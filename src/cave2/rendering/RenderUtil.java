package cave2.rendering;

import cave2.structures.exceptions.RenderError;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import cave2.structures.AABB;
import cave2.structures.Vec2;
import java.awt.Canvas;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;

/**
 * Groups and abstracts a few OpenGL calls
 * @author Colonel32
 */
public final class RenderUtil {

	private RenderUtil() {
	}
	private static int width = 0, height = 0;
	private static boolean isFullscreen = false;

	public static void initialize(DisplayMode mode, boolean fullscreen) throws RenderError {
		width = mode.getWidth();
		height = mode.getHeight();
		isFullscreen = fullscreen;
		try {
			Display.setDisplayMode(mode);
			Display.setFullscreen(fullscreen);
			Display.setVSyncEnabled(fullscreen);
			Display.create();

			AL.create();

			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			throw new RenderError("Error initializing LWJGL", e);
		}

		initialize_gl();
	}

	public static void initialize_applet(Canvas c) throws RenderError {
		width = c.getWidth();
		height = c.getHeight();
		isFullscreen = false;
		try
		{
			Display.setParent(c);
			Display.create();

			AL.create();

			Mouse.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			throw new RenderError("Error initializing LWJGL", e);
		}

		initialize_gl();
	}

	private static void initialize_gl()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glClearColor(0, 0, 0, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);

		GL11.glViewport(0, 0, width, height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void destroy()
	{
		AL.destroy();
		Display.destroy();
	}

	public static void update() {
		Display.update();
		int err = GL11.glGetError();
		if (err != GL11.GL_NO_ERROR) {
			throw new RenderError("GL error: " + GLU.gluErrorString(err));
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static int width() {
		return width;
	}

	public static int height() {
		return height;
	}

	public static boolean isFullscreen() {
		return isFullscreen;
	}

	/**
	 * Draws a box, with one of its corners at (x,y) and dimensions w, h.
	 * Any OpenGL settings set before this method will pass into this context.
	 */
	public static void drawBox(double x, double y, double w, double h) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + w, y);
		GL11.glVertex2d(x + w, y + h);
		GL11.glVertex2d(x, y + h);
		GL11.glEnd();
	}

	/**
	 * Draws an image without lighting
	 */
	public static void drawImage(Texture image, double x, double y, double w, double h) {
		RenderUtil.drawImage(image, x, y, w, h, 1);
	}

	/**
	 * Draws an image with the specified brightness
	 */
	public static void drawImage(Texture image, double x, double y, double w, double h, double brightness) {
		RenderUtil.drawImage(image, x, y, w, h, brightness, 0, 0, 1, 1);
	}

	public static void drawImage(Texture image, double x, double y, double w, double h, double brightness,
			double u, double v, double texw, double texh) {
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//Color.white.bind();
		GL11.glColor3d(brightness, brightness, brightness);
		image.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u, v);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2d(u + texw, v);
		GL11.glVertex2d(x + w, y);
		GL11.glTexCoord2d(u + texw, v + texh);
		GL11.glVertex2d(x + w, y + h);
		GL11.glTexCoord2d(u, v + texh);
		GL11.glVertex2d(x, y + h);
		GL11.glEnd();
	}

	public static void drawImage(Texture image, AABB box, double brightness) {
		drawImage(image, box, brightness, 0, 0, 1, 1);
	}

	public static void drawImage(Texture image, AABB box, double brightness,
			double u, double v, double uw, double vh) {
		drawImage(image, box.center.x - box.size.x, box.center.y - box.size.y,
				box.size.x * 2, box.size.y * 2, brightness, u, v, uw, vh);
	}
	// Store some buffers to prevent reallocation.
	private static FloatBuffer winBuffer = FloatBuffer.allocate(3);
	private static FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer projBuffer = BufferUtils.createFloatBuffer(16);
	private static IntBuffer viewBuffer = BufferUtils.createIntBuffer(16);

	/**
	 * Projects world coordinates (x,y) to screen coordinates, using the
	 * existing matrix stack
	 */
	public static Vec2 project(float x, float y) {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelBuffer);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuffer);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);

		GLU.gluProject(x, y, 0, modelBuffer, projBuffer, viewBuffer, winBuffer);
		return new Vec2(winBuffer.get(0), winBuffer.get(1));
	}

	/**
	 * Unprojects screen coordinates (x,y) to world coordinates, using the
	 * existing matrix stack
	 */
	public static Vec2 unProject(float x, float y) {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelBuffer);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuffer);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewBuffer);
		GLU.gluUnProject(x, y, 0, modelBuffer, projBuffer, viewBuffer, winBuffer);
		return new Vec2(winBuffer.get(0), winBuffer.get(1));
	}

	public static void playSound(Audio audio)
	{
		playSound(audio, 0, 0);
	}

	public static void playSound(Audio audio, float x, float y)
	{
		audio.playAsSoundEffect(1,1,false,x,y,0);
	}
}
