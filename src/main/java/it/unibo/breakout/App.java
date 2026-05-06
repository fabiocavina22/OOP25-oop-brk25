package it.unibo.breakout;

import it.unibo.breakout.controller.api.Controller;
import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.GameState;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.view.impl.SwingGameView;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.Collections;
import java.util.List;

/**
 * Application entry point.
 *
 * <p>Wires the view with a demo controller so the UI can be exercised
 * before the real model and controller are implemented. The demo runs a
 * bouncing ball to prove the render path works end-to-end.
 *
 * <p>Navigation during the demo:
 * <ul>
 *   <li><b>Start</b> button — switches to the game screen and starts the ball.</li>
 *   <li><b>P / Escape</b> — stops the ball and shows the game-over screen.</li>
 *   <li><b>Play Again</b> button — resets the ball and returns to the game screen.</li>
 * </ul>
 */
public final class App {

    private App() { }

    /**
     * Main entry point. All Swing work is dispatched onto the EDT.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(App::launch);
    }

    private static void launch() {
        final SwingGameView view = new SwingGameView();

        // --- mutable demo state kept in single-element arrays (lambda-friendly) ---
        final double   radius   = 10.0;
        final double[] ballX    = {300};
        final double[] ballY    = {350};
        final double[] velX     = {3.5};
        final double[] velY     = {-4.0};
        final int[]    score    = {0};
        final int      lives    = 3;
        final int      level    = 1;

        // ~60 fps game loop (stopped while on menu / game-over)
        final Timer loop = new Timer(16, null);

        final Controller demo = new Controller() {

            @Override public void onLeftPressed()  { /* paddle not wired in demo */ }
            @Override public void onRightPressed() { /* paddle not wired in demo */ }
            @Override public void onLeftReleased() { }
            @Override public void onRightReleased() { }

            /** P / Escape → freeze ball, show game-over with current score. */
            @Override
            public void onPauseToggle() {
                loop.stop();
                view.showGameOver(score[0]);
            }

            /** Start / Play Again → reset state, restart loop, show game screen. */
            @Override
            public void onNewGame() {
                ballX[0] = 300;  ballY[0] = 350;
                velX[0]  = 3.5;  velY[0]  = -4.0;
                score[0] = 0;
                loop.start();
                view.showGame();
            }
        };

        view.setController(demo);

        // Each tick: move ball, bounce off walls, build a GameState, call render.
        loop.addActionListener(tick -> {
            ballX[0] += velX[0];
            ballY[0] += velY[0];

            // Bounce off left / right walls
            if (ballX[0] - radius < 0 || ballX[0] + radius > 600) {
                velX[0] = -velX[0];
                score[0]++;
            }
            // Bounce off top / bottom walls
            if (ballY[0] - radius < 32 || ballY[0] + radius > 668) {
                velY[0] = -velY[0];
                score[0]++;
            }

            final double snapX = ballX[0];
            final double snapY = ballY[0];
            final double snapVx = velX[0];
            final double snapVy = velY[0];
            final int    snapScore = score[0];

            final GameState state = new GameState() {
                @Override public Ball getBall() {
                    return new Ball() {
                        @Override public double  getX()                        { return snapX; }
                        @Override public double  getY()                        { return snapY; }
                        @Override public double  getRadius()                   { return radius; }
                        @Override public double  getVelocityX()                { return snapVx; }
                        @Override public double  getVelocityY()                { return snapVy; }
                        @Override public void    setVelocityX(final double v)  { }
                        @Override public void    setVelocityY(final double v)  { }
                        @Override public void    move()                        { }
                        @Override public void    bounceX()                     { }
                        @Override public void    bounceY()                     { }
                        @Override public void    setPosition(final double x, final double y) { }
                        @Override public boolean isOutOfBounds(final double h) { return false; }
                    };
                }
                @Override public Paddle      getPaddle()       { return new Paddle() { }; }
                @Override public List<Brick> getBricks()       { return Collections.emptyList(); }
                @Override public int         getScore()        { return snapScore; }
                @Override public int         getLives()        { return lives; }
                @Override public int         getCurrentLevel() { return level; }
                @Override public boolean     isPaused()        { return false; }
            };

            view.render(state);
        });

        view.showMenu();
    }
}
