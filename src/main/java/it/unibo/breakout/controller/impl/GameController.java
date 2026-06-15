package it.unibo.breakout.controller.impl;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.collisions.CollisionManagerImpl;
import it.unibo.breakout.model.impl.collisions.CollisionDetectorImpl;
import it.unibo.breakout.view.api.SoundManager;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.GameOverView;
import it.unibo.breakout.view.impl.LeftPanel;
import it.unibo.breakout.view.impl.MainPanel;
// New import added for RightPanel
import it.unibo.breakout.view.impl.RightPanel;
import it.unibo.breakout.model.impl.LeaderboardImpl;
import it.unibo.breakout.model.impl.LivesManagerImpl;
import it.unibo.breakout.model.api.PowerUpManager;

/**
 * GameController: manages the game loop and the methods of written in the model and the view.
 */
public final class GameController implements KeyListener {

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager;
    private final CollisionManagerImpl collisionManager;
    private final SoundManager soundManager;
    private final GameMapImpl view;
    private final LivesManagerImpl livesManager;
    private final PowerUpManager powerUpManager;

    private final int score;

    private final LeaderboardImpl leaderboard = new LeaderboardImpl();

    private final Timer timer;
    private static final int DELAY_MS = 16; // ~60 FPS

    private static final int INITIAL_SPEED = 12;
    private static final int EXPLOSIVE_BLOCK = 5;
    private static final int FREEZE = 5;
    private static final int HALF_POINTS = 6;
    private static final int FASTER_BALL = 7;

    private MainPanel mainPanel;

    private LeftPanel leftPanel;

    private RightPanel rightPanel;

    private final int gameAreaWidth;

    private final int gameAreaHeight;

    private boolean leftPressed;
    private boolean rightPressed;

    private boolean ready = true;

    private boolean pause;

    private final Runnable onPlayAgain;
    /**
     * Constructor of the GameController.
     *
     * @param paddle
     * @param ball
     * @param levelManager
     * @param powerUpManager
     * @param view
     * @param gameAreaWidth
     * @param gameAreaHeight
     * @param score
     * @param onPlayAgain
     * @param soundManager
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification =
        "As the MVC controller, GameController coordinates the shared model and view, which it must hold and mutate."
    )
    public GameController(
        final Paddle paddle,
        final Ball ball,
        final LevelManager levelManager,
        final PowerUpManager powerUpManager,
        final GameMapImpl view,
        final int gameAreaWidth,
        final int gameAreaHeight,
        final int score,
        final Runnable onPlayAgain,
        final SoundManager soundManager
    ) {
        this.paddle = paddle;
        this.ball = ball;
        this.levelManager = levelManager;
        this.powerUpManager = powerUpManager;
        this.view = view;
        this.gameAreaWidth = gameAreaWidth;
        this.gameAreaHeight = gameAreaHeight;
        this.score = score;
        this.onPlayAgain = onPlayAgain;
        this.soundManager = soundManager;

        this.livesManager = new LivesManagerImpl(3);
        this.collisionManager = new CollisionManagerImpl(new CollisionDetectorImpl(), score, powerUpManager);

        /* view listeners */
        this.view.addKeyListener(this);
        this.view.setFocusable(true);
        this.view.requestFocusInWindow();

        this.timer = new Timer(DELAY_MS, e -> update());

        for (final Component comp : view.getContentPane().getComponents()) {
            if (comp instanceof MainPanel) {
                this.mainPanel = (MainPanel) comp;
            } else if (comp instanceof LeftPanel) {
                this.leftPanel = (LeftPanel) comp;
            // Detect and assign the RightPanel from the view components
            } else if (comp instanceof RightPanel) {
                this.rightPanel = (RightPanel) comp;
            }
        }
    }
    /**
     * starts the game.
     */
    public void start() {
        this.timer.start();
    }

    /**
     * check's if the player has finished the lives.
     */
    @SuppressFBWarnings(
        value = "DM_EXIT",
        justification = "User-initiated quit is meant to terminate the game application."
    )
    private void gameOver() {
        timer.stop();
        final int finalScore = collisionManager.getScore();
        SwingUtilities.invokeLater(() -> {
            view.dispose();
            new GameOverView(finalScore, onPlayAgain, this::quitApplication).show(leaderboard);
        });
    }

    @SuppressFBWarnings(
        value = "DM_EXIT",
        justification = "User-initiated quit is meant to terminate the game application."
    )
    private void quitApplication() {
        System.exit(0);
    }

    private void update() {

        final int currentWidth = mainPanel != null ? mainPanel.getWidth() : gameAreaWidth;

        if (currentWidth == 0) {
            return;
        }
        if (pause) {
           return;
        }

        if (leftPressed) {
            paddle.moveLeft();
        }
        if (rightPressed) {
            paddle.moveRight();
        }
        paddle.clamp(currentWidth);

        if (ready) {
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0 - ball.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            if (leftPanel != null) {
                leftPanel.updateEffects();
            }
            view.repaint();
            return;
        }

        ball.move();
        if (!powerUpManager.isFrozen()) {
           levelManager.update(DELAY_MS / 1000.0);
        } else {
            levelManager.removeDestroyedBricks();
        }

        final int currentHeight = mainPanel != null ? mainPanel.getHeight() : gameAreaHeight;
        collisionManager.handleCollisions(ball, paddle, levelManager.getActiveBricks(), currentWidth, currentHeight, score);
        if (collisionManager.hasBallWentUnder(ball, paddle)) {
            if (livesManager.getlives() > 1) {
                livesManager.loseLives();
                collisionManager.resume(ball, currentWidth, paddle);
            } else {
                livesManager.loseLives();
            }
        }
        powerUpManager.updateTimer(paddle, ball);
        powerUpManager.updatePowerUp(paddle, ball, currentHeight, livesManager);

        if (leftPanel != null) {
            if (livesManager.isLifeGained()) {
                leftPanel.addEffect(1, 0);
            }
            if (powerUpManager.getDoublePointsTimer() > 0) {
                leftPanel.addEffect(3, powerUpManager.getDoublePointsTimer());
            } else {
                leftPanel.removeEffect(3);
            }

            if (powerUpManager.getPaddleLargeTimer() > 0) {
                leftPanel.addEffect(4, powerUpManager.getPaddleLargeTimer());
            } else {
                leftPanel.removeEffect(4);
            }
            if (powerUpManager.getPaddleShortTimer() > 0) {
                leftPanel.addEffect(2, powerUpManager.getPaddleShortTimer());
            } else {
                leftPanel.removeEffect(2);
            }
            if (powerUpManager.getFreezeBlocksTimer() > 0) {
                leftPanel.addEffect(FREEZE, powerUpManager.getFreezeBlocksTimer());
            } else {
                leftPanel.removeEffect(FREEZE);
            }
            if (powerUpManager.getHalfPointsTimer() > 0) {
                leftPanel.addEffect(HALF_POINTS, powerUpManager.getHalfPointsTimer());
            } else {
                leftPanel.removeEffect(HALF_POINTS);
            }
            if (powerUpManager.getFastBallTimer() > 0) {
                leftPanel.addEffect(FASTER_BALL, powerUpManager.getFastBallTimer());
            } else {
                leftPanel.removeEffect(FASTER_BALL);
            }
            leftPanel.updateEffects();
        }


        if (collisionManager.isBorderHit()) {
            soundManager.playSound("ballHit.wav");
        }

        if (collisionManager.isPadHit()) {
            soundManager.playSound("ballHit.wav");
        }

        final int hitBlockType = collisionManager.typeOfBlockHit();
        if (hitBlockType > 0) {
            if (hitBlockType == EXPLOSIVE_BLOCK) {
                soundManager.playSound("explosion.wav");
            } else if (hitBlockType == 3) {
                soundManager.playSound("metalHit.wav");
            } else {
                soundManager.playSound("brickBreaks.wav");
            }
        }

        if (livesManager.isLifeLost()) {
            ready = true;
        }

        if (livesManager.isGameOver()) {
            gameOver();
            return;
        }

        if (levelManager.hasBricksReachedThreshold(paddle.getY())) {
            gameOver();
            return;
        }

        view.repaint();

        if (leftPanel != null) {
            leftPanel.updateHUD(collisionManager.getScore(), livesManager.getlives());
        }

        // Check if the current score qualifies for the leaderboard and tell RightPanel to update
        final int currentScore = collisionManager.getScore();
        if (this.rightPanel != null && isScoreQualified(currentScore)) {
            this.rightPanel.updateLeaderboard(this.leaderboard);
        }
    }

    /**
     * Checks if the current score qualifies to enter the leaderboard list.
     *
     * @param finalScore the score achieved by the player
     * @return true if it qualifies, false otherwise
     */
    private boolean isScoreQualified(final int finalScore) {
        final List<Integer> currentScores = this.leaderboard.getScores();
        // Assuming a standard maximum capacity of 7 elements for the leaderboard
        final int maxLeaderboardSize = 7;
        return currentScores.size() < maxLeaderboardSize
        || (!currentScores.isEmpty() && finalScore > currentScores.get(currentScores.size() - 1));
    }


    // --- KEYLISTENER ---

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
            if (leftPanel != null) {
                leftPanel.setKeyPressed("A");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
            if (leftPanel != null) {
                leftPanel.setKeyPressed("D");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W && ready || e.getKeyCode() == KeyEvent.VK_UP && ready) {
            ready = false;
            ball.setVelocityX(0);
            ball.setVelocityY(INITIAL_SPEED);
            if (leftPanel != null) {

                leftPanel.setKeyPressed("W");

                final Timer wTimer = new Timer(500, event -> {
                    leftPanel.setKeyReleased("W");
                    });
                    wTimer.setRepeats(false);
                    wTimer.start();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
            pause = !pause;
            if (pause) {
                leftPanel.setKeyPressed("S");
            }

            } else {
                if (leftPanel != null) {
                    leftPanel.setKeyReleased("S");
                }
            }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
            if (leftPanel != null) {
                leftPanel.setKeyReleased("A");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
            if (leftPanel != null) {
                leftPanel.setKeyReleased("D");
            }
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {

    }
}
