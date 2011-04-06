
package input;

/**
 * Callback for input events (key + mouse)
 * @author Colonel32
 */
public interface InputCallback
{
	public void keyEvent(int key, boolean down);
	public void mouseMovedEvent(int newx, int newy, int dx, int dy, int dwheel);
	public void mouseButtonEvent(int x, int y, int button, boolean down);
}
