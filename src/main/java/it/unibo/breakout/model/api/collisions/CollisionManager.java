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

    //** verifies if the ball hit the pad in this frame */
    void blockHit(Brick bricks);

    //** verifies if the ball hit the pad in this frame */
    void padHit();

    //** verifies if the ball hit the border in this frame */
    void borderHit();

    //** Return the type of block that got hit or 0*/
    int getBlockHit();

    //** Return true if the ball hit the border */
    boolean getBorderHit();

    //** Return true if the ball hit the border */
    boolean getPadHit();

}

