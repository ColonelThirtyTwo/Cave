
package cave2.structures.exceptions;

/**
 * Thrown when the game needs to quit, usually due to an error which should be
 * logged by whatever threw the error.
 * @author Colonel32
 */
public class QuitException extends RuntimeException
{
	public QuitException() {
    }
    public QuitException(String msg) {
        super(msg);
    }
}
