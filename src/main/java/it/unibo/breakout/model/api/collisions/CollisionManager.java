package it.unibo.breakout.model.api.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;

/**
 * Rappresentation of the CollisionManager.
 */
public interface CollisionManager {

    /**
     * Manage the collisions between two collidables.
     * @param ball
     * @param paddle
     * @param bricks
     * @param gameWidth
     * @param gameHeight
     * @param score
     */
    void handleCollisions(Ball ball, Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score);

    /**
     * Keep the current player point.
     * @param brick
     * @return the new score
     */
    int points(Brick brick);

    /**
     * return the actual score.
     * @return the score
     */
    int getScore();

    /**
     * sets blockHit to true.
     * @param bricks
     */
    void blockHit(Brick bricks);

    /**
     * sets padHit to true.
     */
    void padHit();

    /**
     * sets border hit to true.
     */
    void borderHit();

    /**
     * return if the ball hit a block and then sets the value to false.
     * @return BlockHit
     */
    int typeOfBlockHit();

    /**
     * return if the ball hit a border and then sets the value to false.
     * @return BorderHit
     */
    boolean isBorderHit();

    /**
     * return if the ball hit the pad and then sets the value to false.
     * @return PadHit
     */
    boolean isPadHit();

    /**
     *  resumes the game after the ball goes under the paddle.
     * @param ball
     * @param gameWidth
     * @param paddle
     */
    void resume(Ball ball, int gameWidth, Paddle paddle);

    /**
     * checks if the ball goes under the paddle.
     * @param ball
     * @param paddle
     * @return true if the ball went under the paddle
     */
    boolean hasBallWentUnder(Ball ball, Paddle paddle);
}


