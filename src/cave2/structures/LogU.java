/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cave2.structures;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4462908
 * -_-
 * @author clarkSlater
 */
public class LogU
{
	private static final Logger logger;
    private static final ConsoleHandler ch;

    static
	{
        logger = Logger.getLogger("cave2");
        ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }
    public static Logger getLogger()
	{
        return logger;
    }
    public static void setLevel(Level level)
	{
        ch.setLevel(level);
        logger.setLevel(level);
    }

	private LogU() {}

}
