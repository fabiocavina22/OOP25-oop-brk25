package it.unibo.breakout.model.api.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.PowerUpImpl;

public interface CollisionManager {

    public void handleCollisions(Ball ball, Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score);

    /**Keep the current player point
    */
    int points(Brick brick);
    
    /**Get the player's number of lives 
    */
    int getlives();

    /**manage the game over
    */
    boolean isGameOver();

    /**Is a "signal" for the life lost
    */
    boolean isLifeLost();

    /**Manage the score that will appear in the leaderboard
    */
    int getScore();

    long getDoublePointsTimer();
    long getPaddleLargeTimer();
    long getPaddleShortTimer();
    long getFreezeBlocksTimer();
    long getHalfPointsTimer();
    long getFastBallTimer();

    /**Uptade the position of the power up
    */
    void updatePowerUp(Paddle paddle, Ball ball, int screenHeight);

    /*Update the power up timers
    */
   void updateTimer(Paddle paddle, Ball ball);

   /*Stops the power up timer
   */
   void pauseTimer();

   /**Resume the power up timer
   */
   void resumeTimer();

    /**Return the power up list
    */
   List<PowerUpImpl> getActivePowerUp();
    //** */
    void blockHit(Brick bricks);

    //** */
    void padHit();

    //** */
    void borderHit();

    //** */
    int getBlockHit();

    //** */
    boolean getBorderHit();

    //** */
    boolean getPadHit();

}

