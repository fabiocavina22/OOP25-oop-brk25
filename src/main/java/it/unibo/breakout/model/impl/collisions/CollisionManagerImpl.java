package it.unibo.breakout.model.impl.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;
import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.api.collisions.CollisionManager;
import it.unibo.breakout.model.api.LivesManager;


public class CollisionManagerImpl implements CollisionManager {

    private final CollisionDetector detector;
    private int score;

    private PowerUpManager powerUpManager;
    private LivesManager livesManager;

    private int blockHit;
    private boolean padHit;
    private boolean borderHit;

    public CollisionManagerImpl(CollisionDetector detector, int score, PowerUpManager powerUpManager, LivesManager livesManager) {
        this.detector = detector;
        this.score = score;
        this.livesManager = livesManager;
        this.powerUpManager = powerUpManager;
    }

    @Override
    public void handleCollisions(Ball ball,Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score){
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks, powerUpManager.isFrozen());
        checkBorderCollision(ball, gameWidth, gameHeight, paddle);
    }

    @Override
    public int points(Brick brick){
        if(brick.isIndestructible()){
            return score;
        }
        if(brick.isDestroyed()){
            score += (int)(300 * powerUpManager.getScoreMultiplier());
        }
        else{
            score += (int)(150 * powerUpManager.getScoreMultiplier());
        }
        return score;
    }

    @Override
    public int getScore(){
        return score;
    }


    @Override
    public void blockHit(Brick bricks){
        this.blockHit = bricks.getType();
    }

    @Override
    public void padHit(){
        this.padHit = true ;
    }

    @Override
    public void borderHit(){
        this.borderHit = true ;
    }

    @Override
    public int getBlockHit() {
        int result = this.blockHit;
        this.blockHit = 0;
        return result;
    }

    @Override
    public boolean getPadHit() {
        boolean result = this.padHit;
        this.padHit = false;
        return result;
    }

    @Override
    public boolean getBorderHit() {
        boolean result = this.borderHit;
        this.borderHit = false;
        return result;
    }

    /**
     * Checks if a collision happens between the ball and the paddle
     * @param ball
     * @param paddle
     */
    private void checkPaddleCollision(Ball ball, Paddle paddle){

        if(detector.isColliding(ball, paddle)){

            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + ball.getWidth() / 2.0;

            double offset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            /**
             * total current speed
             */
            double speed = Math.sqrt( ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY() );

            /**
             * new direction
             */
            double maxBounceAngle = Math.toRadians(60);

            /**
             * final angle
             */
            double bounceAngle = offset * maxBounceAngle;

            /**
             * new speed
             */
            double newVelocityX = speed * Math.sin(bounceAngle);
            double newVelocityY = -speed * Math.cos(bounceAngle);

            padHit();

            ball.setVelocityX(newVelocityX);
            ball.setVelocityY(newVelocityY);
        }

    }
    /**
     * Checks if the ball hit the borders or goes under the paddle
     * manages the logic of the life loss
     * @param ball
     * @param gameWidth
     * @param gameHeight
     * @param paddle
     */
    private void checkBorderCollision(Ball ball, int gameWidth, int gameHeight, Paddle paddle) {

        if (ball.getX() <= 0) {
            borderHit();
            ball.setPosition(0, ball.getY());
            ball.setVelocityX(Math.abs(ball.getVelocityX()));
        }

        else if (ball.getX() + ball.getWidth() >= gameWidth ) {
            borderHit();
            ball.setPosition(gameWidth - ball.getWidth(), ball.getY());
            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
        }

        if (ball.getY() <= 0) {
            borderHit();
            ball.setPosition(ball.getX(), 0);
            ball.setVelocityY(Math.abs(ball.getVelocityY()));
        }

        if(ball.getY() > paddle.getY() + paddle.getHeight() + 22 && !livesManager.isLifeLost()){
            livesManager.loseLives();
            if(powerUpManager.getFastBallFrames() > 0){
                ball.setVelocityX(ball.getVelocityX() / 1.5);
                ball.setVelocityY(ball.getVelocityY() / 1.5);
                powerUpManager.resetFastBallFrames();
            }
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            ball.setVelocityX(0);
            ball.setVelocityY(0);
        }
    }

    private boolean isAdjacent(Brick brick, Brick adjacentBrick){
        double dx = Math.abs(brick.getX() - adjacentBrick.getX());
        double dy = Math.abs(brick.getY() - adjacentBrick.getY());
        boolean adjacentX = dx < brick.getWidth() * 1.5;
        boolean adjacentY = dy < brick.getHeight() * 1.5;
        boolean notSame = dx > 0 || dy > 0;
        return adjacentX && adjacentY && notSame;
    }

    /**
     * Checks if a collision happens between any block and the ball
     * Manages the brick destruction, power ups releas
     * @param ball
     * @param bricks
     * @param frozen
     */
    private void checkBrickCollisions(Ball ball, List<Brick> bricks, boolean frozen) {

        for (Brick brick : bricks) {

            if (detector.isColliding(ball, brick)) {

                /**
                * gets the ball's position before it hit the block
                */
                double prevBallX = ball.getX() - ball.getVelocityX();
                double prevBallY = ball.getY() - ball.getVelocityY();

                /**
                * checks where the ball is coming from
                */
                boolean comingFromLeft = (prevBallX + ball.getWidth() <= brick.getX());
                boolean comingFromRight   = (prevBallX >= brick.getX() + brick.getWidth());
                boolean comingFromAbove   = (prevBallY + ball.getHeight() <= brick.getY());
                boolean comingFromBottom   = (prevBallY >= brick.getY() + brick.getHeight());

                /*
                * calculate the overlap in order to avoid bugs with the collision
                */
                double overlapLeft   = (ball.getX() + ball.getWidth()) - brick.getX();
                double overlapRight  = (brick.getX() + brick.getWidth()) - ball.getX();
                double overlapTop    = (ball.getY() + ball.getHeight()) - brick.getY();
                double overlapBottom = (brick.getY() + brick.getHeight()) - ball.getY();

                /*
                * changes the ball's new directrion based on its previous position
                */
                if (comingFromLeft ) {
                    ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                    ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                }
                else if (comingFromRight) {
                    ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                    ball.setVelocityX(Math.abs(ball.getVelocityX()));
                }
                else if (comingFromAbove) {
                    ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                    ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                }
                else if (comingFromBottom) {
                    ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                    ball.setVelocityY(Math.abs(ball.getVelocityY()));
                }
                else {
                    /*
                    * avoid the possibility that the ballo gets stuck in a perfect angle
                    */
                    double minOverlapX = Math.min(overlapLeft, overlapRight);
                    double minOverlapY = Math.min(overlapTop, overlapBottom);

                    if (minOverlapX < minOverlapY) {
                        if (overlapLeft < overlapRight) {
                            ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                        } else {
                            ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                            ball.setVelocityX(Math.abs(ball.getVelocityX()));
                        }
                    } else {
                        if (overlapTop < overlapBottom) {
                            ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                            ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                        } else {
                            ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                            ball.setVelocityY(Math.abs(ball.getVelocityY()));
                        }
                    }
                }

                /*brick's destruction*/
                brick.hit();
                points(brick);

                /* bomb brick */
                if(brick.getType() == 5){
                    for(int i = 0; i < bricks.size(); i++){
                        Brick adjacentBrick = bricks.get(i);
                        if(adjacentBrick != brick && isAdjacent(brick, adjacentBrick)){
                            adjacentBrick.hit();
                            points(adjacentBrick);
                            if(adjacentBrick.isDestroyed() && adjacentBrick.getType() == 4){
                                powerUpManager.spawnPowerUp(adjacentBrick.getX() + adjacentBrick.getWidth() / 2.0, adjacentBrick.getY());
                                }
                            }
                        }
                    }

                    /* power up block */
                    if(brick.isDestroyed() && brick.getType() == 4){
                        powerUpManager.spawnPowerUp (brick.getX() + brick.getWidth() / 2.0, brick.getY());
                    }
                blockHit(brick);


                break; /* avoid multiple collisions in the same frame */
            }
        }
    }
}
