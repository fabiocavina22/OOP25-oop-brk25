package it.unibo.breakout;

import it.unibo.breakout.controller.impl.GameController;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LevelManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.MenuView;
import it.unibo.breakout.view.impl.SoundManagerImpl;
import it.unibo.breakout.model.impl.LeaderboardImpl;

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

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int mainPanelWidth = (int) (screenWidth * MAIN_PANEL_RATIO);
        int mainPanelHeight = screenHeight;

        int paddleWidth = (int) (screenWidth * PADDLE_WIDTH_RATIO);
        int paddleHeight = PADDLE_HEIGHT_PX;

        int paddleX = (mainPanelWidth - paddleWidth) / 2;
        int paddleY = (int) (mainPanelHeight * PADDLE_Y_RATIO);
        int brickSide = mainPanelWidth / 10;
        int ballDiameter = BALL_DIAMETER_PX;

        int ballX = paddleX + (paddleWidth / 2) - (ballDiameter / 2);

        int ballY = paddleY - ballDiameter;

        final PaddleImpl paddle = new PaddleImpl(paddleX, paddleY, paddleWidth, paddleHeight, PADDLE_SPEED_PX);
        final BallImpl ball = new BallImpl(ballX, ballY, ballDiameter, 0.0, BALL_INIT_VY);
        final LevelManagerImpl levelManager = new LevelManagerImpl(mainPanelWidth, brickSide, brickSide, mainPanelHeight);
        final LeaderboardImpl leaderboard = new LeaderboardImpl();
        final SoundManagerImpl soundManager = new SoundManagerImpl();

        int score = 0;

        final GameMapImpl view = new GameMapImpl(paddle, levelManager, ball, leaderboard);
        view.showWindow();

        final GameController controller = new GameController(
                paddle, ball, levelManager, view,
                screenWidth, screenHeight, score, App::startGame, soundManager);
        controller.start();
    }
}
