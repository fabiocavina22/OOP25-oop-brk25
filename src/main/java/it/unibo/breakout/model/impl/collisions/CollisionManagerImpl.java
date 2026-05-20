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

    public CollisionManagerImpl(CollisionDetector detector, int score) {
        this.detector = detector;
        this.score = score;
    }

    @Override
    public void handleCollisions(Ball ball, Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score){
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks);
        checkBorderCollision(ball, gameWidth, gameHeight);
    }

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

    private void checkBorderCollision(Ball ball, int gameWidth, int gameHeight) {

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

    }

    private void checkBrickCollisions(Ball ball, List<Brick> bricks) {

        for (Brick brick : bricks) {

        if (detector.isColliding(ball, brick)) {

            // rimbalzo semplice
            ball.setVelocityY(-ball.getVelocityY());

            // distruzione brick
            brick.hit();
            points(brick);


            break; // evita multi-collisione nello stesso frame
        }

        }
    }
}
