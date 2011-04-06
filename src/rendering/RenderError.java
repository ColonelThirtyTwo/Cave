
package rendering;

/**
 * Generic rendering error exception, usually thrown during initialization.
 * @author Colonel32
 */
public class RenderError extends RuntimeException
{

    /**
     * Creates a new instance of <code>RenderError</code> without detail message.
     */
    public RenderError() {
    }


    /**
     * Constructs an instance of <code>RenderError</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RenderError(String msg) {
        super(msg);
    }
}
