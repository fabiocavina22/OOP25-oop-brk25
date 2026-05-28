package it.unibo.breakout;

import it.unibo.breakout.controller.impl.GameController;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LevelManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.MenuView;
import it.unibo.breakout.view.impl.SoundManagerImpl;

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
        final PaddleImpl paddle = new PaddleImpl(225, 600, 100, 15, 12);
        final BallImpl ball = new BallImpl(250, 400, 10, 0.0, 12.0);
        final LevelManagerImpl levelManager = new LevelManagerImpl(GAME_WIDTH, 60, 60, GAME_HEIGHT);
        final SoundManagerImpl soundManager = new SoundManagerImpl();
        int score = 0;

        final GameMapImpl view = new GameMapImpl(paddle, levelManager, ball);
        view.showWindow();

        final GameController controller = new GameController(paddle, ball, levelManager, view, GAME_WIDTH, GAME_HEIGHT, score, App::startGame, soundManager);
        controller.start();
    }
}
