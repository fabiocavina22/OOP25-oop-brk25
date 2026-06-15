package it.unibo.breakout.model.api;

import java.util.List;

import it.unibo.breakout.model.impl.PowerUpImpl;

public interface PowerUpManager {

    long getDoublePointsTimer();
    long getPaddleLargeTimer();
    long getPaddleShortTimer();
    long getFreezeBlocksTimer();
    long getHalfPointsTimer();
    long getFastBallTimer();

    boolean isFrozen();

    /**Uptade the position of the power up
    */
    void updatePowerUp(Paddle paddle, Ball ball, int screenHeight, LivesManager livesManager);

    /**Update the power up timers
    */
   void updateTimer(Paddle paddle, Ball ball);

    /**Return the power up list
    */
    List<PowerUpImpl> getActivePowerUp();

    /**Return the current score multiplier
    */
    double getScoreMultiplier();

    /**Return the frames of the fast ball effect
    */
    int getFastBallFrames();

    /**Reset the fast ball effect frames
    */
    void resetFastBallFrames();

    /**Spawn the power up
    */
    void spawnPowerUp(double x, double y);
    }
