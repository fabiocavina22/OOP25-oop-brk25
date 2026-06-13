package it.unibo.breakout.model.api.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;

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
    public void handleCollisions(Ball ball, Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score);

    /**Keep the current player point
    */
    int points(Brick brick);

    /**Manage the score that will appear in the leaderboard
    */
    int getScore();

    /**
     * sets blockHit to true
     */
    void blockHit(Brick bricks);

    /**
     * sets padHit to true
     */
    void padHit();

    /**
     * sets border hit to true
     */
    void borderHit();

    /**
     * return if the ball hit a block and then sets the value to fals
     * @return BlockHit
     */
    int getBlockHit();

    /**
     * return if the ball hit a border and then sets the value to fals
     * @return BorderHit
     */
    boolean getBorderHit();

    /**
     * return if the ball hit the pad and then sets the value to fals
     * @return PadHit
     */
    boolean getPadHit();

}

