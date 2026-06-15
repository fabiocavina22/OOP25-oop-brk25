package it.unibo.breakout.controller.impl;

//import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
import it.unibo.breakout.model.impl.LeaderboardImpl;
import it.unibo.breakout.model.impl.LivesManagerImpl;
import it.unibo.breakout.model.impl.PowerUpManagerImpl;

public class GameController implements KeyListener {

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager;
    private final CollisionManagerImpl collisionManager;
    private final SoundManager soundManager;
    private final GameMapImpl view;
    private final LivesManagerImpl livesManager;
    private final PowerUpManagerImpl powerUpManager;

    private int score;

    private final LeaderboardImpl leaderboard = new LeaderboardImpl();

    private final Timer timer;
    private static final int DELAY_MS = 16; // ~60 FPS

    private MainPanel mainPanel;

    private LeftPanel leftPanel;

    private final int gameAreaWidth;

    private final int gameAreaHeight;


    // Variabili per il movimento fluido
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    //Variabile per gestire il respawn della pallina e il continuo del gioco
    private boolean ready = true;

    //Variabile per la gestione della pausa
    private boolean pause = false;

    private final Runnable onPlayAgain;

    public GameController(final Paddle paddle, final Ball ball, final LevelManager levelManager, final GameMapImpl view, final int gameAreaWidth, final int gameAreaHeight, final int score, final Runnable onPlayAgain, SoundManager soundManager) {
        this.paddle = paddle;
        this.ball = ball;
        this.levelManager = levelManager;
        this.view = view;
        this.gameAreaWidth = gameAreaWidth;
        this.gameAreaHeight = gameAreaHeight;
        this.score = score;
        this.onPlayAgain = onPlayAgain;
        this.soundManager = soundManager;

        this.livesManager = new LivesManagerImpl(3);
        this.powerUpManager = new PowerUpManagerImpl(this.livesManager);
        this.collisionManager = new CollisionManagerImpl(new CollisionDetectorImpl(), score, powerUpManager);

        /* view listeners */
        this.view.addKeyListener(this);
        this.view.setFocusable(true);
        this.view.requestFocusInWindow();

        // Game Loop
        this.timer = new Timer(DELAY_MS, e -> update());

        for (java.awt.Component comp : view.getContentPane().getComponents()) {
            if (comp instanceof MainPanel) {
                this.mainPanel = (MainPanel) comp;
            }else if (comp instanceof LeftPanel) {
                this.leftPanel = (LeftPanel) comp;
            }
        }
    }

    public void start() {
        this.timer.start();
    }

    private void gameOver() {
        timer.stop();
        final int finalScore = collisionManager.getScore();
        SwingUtilities.invokeLater(() -> {
            view.dispose();
            new GameOverView(finalScore, onPlayAgain, () -> System.exit(0), leaderboard).show();
        });
    }

    private void update() {

        int currentWidth = mainPanel != null ? mainPanel.getWidth() : gameAreaWidth;
        int currentHeight = mainPanel != null ? mainPanel.getHeight() : gameAreaHeight;

        // Se la grafica non è ancora pronta (width=0), salta il frame
        if (currentWidth == 0) return;
        if(pause) return;
        // 1. Gestione Input
        if (leftPressed) {
            paddle.moveLeft();
        }
        if (rightPressed) {
            paddle.moveRight();
        }
        paddle.clamp(currentWidth);

        if(ready){
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0 - ball.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            if(leftPanel != null){
                leftPanel.updateEffects();
            }
            view.repaint();
            return;
        }

        ball.move();
        if(!powerUpManager.isFrozen()){
           levelManager.update(DELAY_MS / 1000.0);
        }
        else{
            levelManager.removeDestroyedBricks();
        }

        collisionManager.handleCollisions(ball, paddle, levelManager.getActiveBricks(), currentWidth, currentHeight, score);
        if (collisionManager.hasBallWentUnder(ball, paddle)) {
            if(livesManager.getlives() > 1) {
                livesManager.loseLives();
                collisionManager.resume(ball, currentWidth, paddle);
            }
            else{
                livesManager.loseLives();
            }
        }
        powerUpManager.updateTimer(paddle, ball);
        powerUpManager.updatePowerUp(paddle, ball, currentHeight);

        if(leftPanel != null){
            if(livesManager.isLifeGained()) leftPanel.addEffect(1, 0);
            if (powerUpManager.getDoublePointsTimer() > 0) leftPanel.addEffect(3, powerUpManager.getDoublePointsTimer());
            else leftPanel.removeEffect(3);
            if (powerUpManager.getPaddleLargeTimer() > 0) leftPanel.addEffect(4, powerUpManager.getPaddleLargeTimer());
            else leftPanel.removeEffect(4);
            if (powerUpManager.getPaddleShortTimer() > 0) leftPanel.addEffect(2, powerUpManager.getPaddleShortTimer());
            else leftPanel.removeEffect(2);
            if (powerUpManager.getFreezeBlocksTimer() > 0) leftPanel.addEffect(5, powerUpManager.getFreezeBlocksTimer());
            else leftPanel.removeEffect(5);
            if (powerUpManager.getHalfPointsTimer() > 0) leftPanel.addEffect(6, powerUpManager.getHalfPointsTimer());
            else leftPanel.removeEffect(6);
            if (powerUpManager.getFastBallTimer() > 0) leftPanel.addEffect(7, powerUpManager.getFastBallTimer());
            else leftPanel.removeEffect(7);
            leftPanel.updateEffects();
        }

        mainPanel.setPowerUp(powerUpManager.getActivePowerUp());

        if(collisionManager.isBorderHit()){
            soundManager.playSound("ballHit.wav");
        }

        if(collisionManager.isPadHit()){
            soundManager.playSound("ballHit.wav");
        }

        int hitBlockType = collisionManager.isBlockHit();
        if(hitBlockType > 0){
            if (hitBlockType == 5 ){
                soundManager.playSound("explosion.wav");
            }
            else if (hitBlockType == 3){
                soundManager.playSound("metalHit.wav");
            }
            else{
                soundManager.playSound("brickBreaks.wav");
            }
        }

        if(livesManager.isLifeLost()){
            ready = true;
        }

        if(livesManager.isGameOver()){
            gameOver();;
            return;
        }

        if(levelManager.hasBricksReachedThreshold(paddle.getY())){
            gameOver();
            return;
        }

        view.repaint();

        leftPanel.updateHUD(collisionManager.getScore(), livesManager.getlives());
    }


    // --- KEYLISTENER ---

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
            if (leftPanel != null) leftPanel.setKeyPressed("A");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
            if (leftPanel != null) leftPanel.setKeyPressed("D");
        }
        if(e.getKeyCode() == KeyEvent.VK_W && ready || e.getKeyCode() == KeyEvent.VK_UP && ready){
            ready = false;
            ball.setVelocityX(0);
            ball.setVelocityY(12);
            if (leftPanel != null) {

                leftPanel.setKeyPressed("W");

                javax.swing.Timer wTimer = new javax.swing.Timer(500, event -> {
                        leftPanel.setKeyReleased("W");
                    });
                    wTimer.setRepeats(false); // IMPORTANTE: dice al timer di eseguirsi una volta sola e poi distruggersi
                    wTimer.start();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
            pause = !pause;
            if(pause){
                if (leftPanel != null) leftPanel.setKeyPressed("S");
            }

            }else{
                if (leftPanel != null) leftPanel.setKeyReleased("S");
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
            if (leftPanel != null) leftPanel.setKeyReleased("A");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
            if (leftPanel != null) leftPanel.setKeyReleased("D");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non necessario
    }
}