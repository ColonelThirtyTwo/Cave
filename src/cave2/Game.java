
package cave2;

/**
 * A glue class for worlds and cameras.
 * @author Colonel32
 */
public class Game
{
    private Game() {}

    public static Game getInstance() {
        return GameHolder.INSTANCE;
    }

    private static class GameHolder {
        private static final Game INSTANCE = new Game();

		private GameHolder() {}
    }
 }
