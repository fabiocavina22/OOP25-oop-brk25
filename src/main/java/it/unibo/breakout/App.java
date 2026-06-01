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

        // Il MainPanel occupa esattamente il 40% della larghezza
        int mainPanelWidth = (int) (screenWidth * 0.40);
        int mainPanelHeight = screenHeight; // Occupa il 100% dell'altezza

        int paddleWidth = (int) (screenWidth * 0.10);
        int paddleHeight = 20; // Un'altezza fissa va benissimo (es. 20 pixel)

        // Posizione iniziale: centrato in X, al 20% dal fondo in Y
        int paddleX = (mainPanelWidth - paddleWidth) / 2;
        int paddleY = (int) (mainPanelHeight * 0.80);
        int brickSide = mainPanelWidth /10;
        int ballDiameter = 15; // Diametro della palla (es. 20 pixel)

        // Formula per centrarla matematicamente sulla X del paddle
        int ballX = paddleX + (paddleWidth / 2) - (ballDiameter / 2);

        // La Y deve essere sopra il pad (Y del pad meno il diametro della palla)
        int ballY = paddleY - ballDiameter;

        final PaddleImpl paddle = new PaddleImpl(paddleX, paddleY, paddleWidth, paddleHeight, 12);
        final BallImpl ball = new BallImpl(ballX, ballY, ballDiameter, 0.0, 12.0);
        final LevelManagerImpl levelManager = new LevelManagerImpl(mainPanelWidth, brickSide, brickSide, mainPanelHeight);
        final LeaderboardImpl leaderboard = new LeaderboardImpl();
        final SoundManagerImpl soundManager = new SoundManagerImpl();
        int score = 0;

        final GameMapImpl view = new GameMapImpl(paddle, levelManager, ball, leaderboard);
        view.showWindow();

        final GameController controller = new GameController(paddle, ball, levelManager, view, screenWidth, screenHeight, score, App::startGame, soundManager);
        controller.start();
    }
}
