package it.unibo.breakout.model.impl.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.api.collisions.CollisionManager;


public class CollisionManagerImpl implements CollisionManager {

    private final CollisionDetector detector;
    private int score;
    private int lives = 3;
    private boolean lifeLost = false;

    public CollisionManagerImpl(CollisionDetector detector, int score) {
        this.detector = detector;
        this.score = score;
    }

    @Override
    public void handleCollisions(Ball ball,Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score){
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks);
        checkBorderCollision(ball, gameWidth, gameHeight, paddle);
    }

    @Override
    public int points(Brick brick){
        if(brick.isIndestructible()){
            return score;
        }
        if(brick.isDestroyed()){
            score += 300;
        }
        else{
            score += 150;
        }
        System.out.println(score);
        return score;
    }


    public int getlives(){
        return lives;
    }


    private void loselives(){
        lives--;
        lifeLost = true;
    }

    public boolean isLifeLost(){
        boolean result = lifeLost;
        lifeLost = false;
        return result;
    }

    public boolean isGameOver(){
        if(lives <= 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int getScore(){
        return score;
    }

    private void checkPaddleCollision(Ball ball, Paddle paddle){

        if(detector.isColliding(ball, paddle)){

            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + ball.getWidth() / 2.0;

            double offset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            // velocità totale attuale
            double speed = Math.sqrt( ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY() );

            // nuova direzione
            double maxBounceAngle = Math.toRadians(60);

            // angolo finale
            double bounceAngle = offset * maxBounceAngle;

            // nuove velocità
            double newVelocityX = speed * Math.sin(bounceAngle);
            double newVelocityY = -speed * Math.cos(bounceAngle);

            ball.setVelocityX(newVelocityX);
            ball.setVelocityY(newVelocityY);
        }

    }

    private void checkBorderCollision(Ball ball, int gameWidth, int gameHeight, Paddle paddle) {

        if (ball.getX() <= 0) { //SX
            ball.setPosition(0, ball.getY());
            ball.setVelocityX(Math.abs(ball.getVelocityX()));
        }

        else if (ball.getX() + ball.getWidth() >= gameWidth ) { //DX
            ball.setPosition(gameWidth - ball.getWidth(), ball.getY());
            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
        }

        if (ball.getY() <= 0) { //SUP
            ball.setPosition(ball.getX(), 0);
            ball.setVelocityY(Math.abs(ball.getVelocityY()));
        }

        if(ball.getY() > paddle.getY() + paddle.getHeight() + 22 && !lifeLost){
            loselives();
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            ball.setVelocityX(0);
            ball.setVelocityY(0);
        }
    }

    private void checkBrickCollisions(Ball ball, List<Brick> bricks) {

        for (Brick brick : bricks) {

            if (detector.isColliding(ball, brick)) {

                // 1. Calcoliamo di quanti pixel la palla è "entrata" nel blocco da ogni lato
                double overlapLeft = (ball.getX() + ball.getWidth()) - brick.getX();
                double overlapRight = (brick.getX() + brick.getWidth()) - ball.getX();
                double overlapTop = (ball.getY() + ball.getHeight()) - brick.getY();
                double overlapBottom = (brick.getY() + brick.getHeight()) - ball.getY();

                // 2. Troviamo la compenetrazione minima per l'asse X e l'asse Y
                double minOverlapX = Math.min(overlapLeft, overlapRight);
                double minOverlapY = Math.min(overlapTop, overlapBottom);

                // 3. Se la compenetrazione X è minore, la palla ha colpito un lato (sinistro o destro)
                if (minOverlapX < minOverlapY) {
                    if (overlapLeft < overlapRight){
                        ball.setPosition(brick.getX() - ball.getWidth(), ball.getY()); //bug fix
                        ball.setVelocityX(-ball.getVelocityX()); // Rimbalzo orizzontale
                    }
                    else {
                        ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                        ball.setVelocityY(-ball.getVelocityY()); // Rimbalzo verticale
                    }
                }
                else{
                    if (overlapTop < overlapBottom){
                        ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                        ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                    }
                    else {
                        ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                        ball.setVelocityY(Math.abs(ball.getVelocityY())); // Forza la direzione verso il basso
                    }
                }

                // distruzione brick
                brick.hit();
                points(brick);

                break; // evita multi-collisione nello stesso frame
            }
        }
    }
}
