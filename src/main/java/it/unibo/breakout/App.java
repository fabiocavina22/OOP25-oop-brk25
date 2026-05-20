package it.unibo.breakout;

import it.unibo.breakout.controller.impl.GameController;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LevelManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.MenuView;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 *
 * <p>Shows the main menu; when the player clicks "Start" the game objects
 * are created and the game window is opened.
 */
public final class App {

    private static final int GAME_WIDTH  = 600;
    private static final int GAME_HEIGHT = 700;

    private App() { }

    /**
     * Main entry point. All Swing work is dispatched onto the EDT.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new MenuView(App::startGame).show());
    }

    private static void startGame() {
        final PaddleImpl      paddle       = new PaddleImpl(250, 600, 100, 15, 8);
        final BallImpl        ball         = new BallImpl(300, 500, 8, 4.0, -4.0);
        final LevelManagerImpl levelManager = new LevelManagerImpl(GAME_WIDTH, 60, 20, GAME_HEIGHT);
        int score = 0;

        final GameMapImpl view = new GameMapImpl(paddle, levelManager, ball);
        view.showWindow();

        final GameController controller = new GameController(paddle, ball, levelManager, view, GAME_WIDTH, GAME_HEIGHT, score);
        controller.start();
    }
}
