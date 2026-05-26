package it.unibo.breakout.controller.impl;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.collisions.CollisionManagerImpl;
import it.unibo.breakout.model.impl.collisions.CollisionDetectorImpl;
import it.unibo.breakout.view.impl.GameMapImpl;
import it.unibo.breakout.view.impl.GameOverView;
import it.unibo.breakout.view.impl.MainPanel;

public class GameController implements KeyListener {

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager;
    private final CollisionManagerImpl collisionManager;
    private final GameMapImpl view;

    private int score;

    private final Timer timer;
    private static final int DELAY_MS = 16; // ~60 FPS

    private JPanel mainPanel;

    private final int gameAreaWidth;
    @SuppressWarnings("unused")
    private final int gameAreaHeight;


    // Variabili per il movimento fluido
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    //Variabile per gestire il respawn della pallina e il continuo del gioco
    private boolean ready = true;

    //Variabile per la gestione della pausa
    private boolean pause = false;

    private final Runnable onPlayAgain;

    public GameController(final Paddle paddle, final Ball ball, final LevelManager levelManager, final GameMapImpl view, final int gameAreaWidth, final int gameAreaHeight, final int score, final Runnable onPlayAgain) {
        this.paddle = paddle;
        this.ball = ball;
        this.levelManager = levelManager;
        this.view = view;
        this.gameAreaWidth = gameAreaWidth;
        this.gameAreaHeight = gameAreaHeight;
        this.score = score;
        this.onPlayAgain = onPlayAgain;

        // Inizializza il manager delle collisioni (MVC rispettato: passiamo solo le dimensioni)
        this.collisionManager = new CollisionManagerImpl(new CollisionDetectorImpl(), score);

        // Aggiungiamo l'ascoltatore della tastiera alla View
        this.view.addKeyListener(this);
        this.view.setFocusable(true);
        this.view.requestFocusInWindow();

        // Game Loop
        this.timer = new Timer(DELAY_MS, e -> update());

        for (java.awt.Component comp : view.getContentPane().getComponents()) {
            if (comp instanceof MainPanel) {
                this.mainPanel = (JPanel) comp;
                break;
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
            new GameOverView(finalScore, onPlayAgain, () -> System.exit(0)).show();
        });
    }

    private void update() {

        int currentWidth = mainPanel != null ? mainPanel.getWidth() : gameAreaWidth;
        int currentHeight = view.getHeight();

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
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            view.repaint();
            return;
        }

        ball.move();
        levelManager.update(DELAY_MS / 1000.0);

        collisionManager.handleCollisions(ball, paddle, levelManager.getActiveBricks(), currentWidth, currentHeight, score);

        if(collisionManager.isLifeLost()){
            ready = true;
        }

        if(collisionManager.isGameOver()){
            gameOver();;
            return;
        }

        if(levelManager.hasBricksReachedThreshold(paddle.getY())){
            gameOver();
            return;
        }

        view.repaint();
    }


    // --- KEYLISTENER ---

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_W && ready || e.getKeyCode() == KeyEvent.VK_UP && ready){
            ready = false;
            ball.setVelocityX(0);
            ball.setVelocityY(12);
        }
        if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
            pause = !pause;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non necessario
    }
}