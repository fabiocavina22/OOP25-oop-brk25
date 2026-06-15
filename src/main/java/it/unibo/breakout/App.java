package it.unibo.breakout;

import it.unibo.breakout.controller.impl.GameController;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LevelManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.model.impl.PowerUpManagerImpl;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.MenuView;
import it.unibo.breakout.view.impl.SoundManagerImpl;
import it.unibo.breakout.model.impl.LeaderboardImpl;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 *
 * <p>Shows the main menu; when the player clicks "Start" the game objects
 * are created and the game window is opened.
 */
public final class App {

    private static final double MAIN_PANEL_RATIO  = 0.40;
    private static final double PADDLE_WIDTH_RATIO = 0.10;
    private static final int    PADDLE_HEIGHT_PX   = 20;
    private static final double PADDLE_Y_RATIO     = 0.80;
    private static final int    BALL_DIAMETER_PX   = 15;
    private static final int    PADDLE_SPEED_PX    = 12;
    private static final double BALL_INIT_VY       = 12.0;

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

        final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;

        final int mainPanelWidth = (int) (screenWidth * MAIN_PANEL_RATIO);
        final int mainPanelHeight = screenHeight;

        final int paddleWidth = (int) (screenWidth * PADDLE_WIDTH_RATIO);
        final int paddleHeight = PADDLE_HEIGHT_PX;

        final int paddleX = (mainPanelWidth - paddleWidth) / 2;
        final int paddleY = (int) (mainPanelHeight * PADDLE_Y_RATIO);
        final int brickSide = mainPanelWidth / 10;
        final int ballDiameter = BALL_DIAMETER_PX;

        final int ballX = paddleX + (paddleWidth / 2) - (ballDiameter / 2);

        final int ballY = paddleY - ballDiameter;

        final PaddleImpl paddle = new PaddleImpl(paddleX, paddleY, paddleWidth, paddleHeight, PADDLE_SPEED_PX);
        final BallImpl ball = new BallImpl(ballX, ballY, ballDiameter, 0.0, BALL_INIT_VY);
        final LevelManagerImpl levelManager = new LevelManagerImpl(mainPanelWidth, brickSide, mainPanelHeight);
        final LeaderboardImpl leaderboard = new LeaderboardImpl();
        final SoundManagerImpl soundManager = new SoundManagerImpl();
        final PowerUpManagerImpl powerUpManager = new PowerUpManagerImpl();

        final int score = 0;

        final GameMapImpl view = new GameMapImpl(paddle, levelManager, ball, powerUpManager, leaderboard);
        view.showWindow();

        final GameController controller = new GameController(
                paddle, ball, levelManager, powerUpManager, view,
                screenWidth, screenHeight, score, App::startGame, soundManager);
        controller.start();
    }
}
