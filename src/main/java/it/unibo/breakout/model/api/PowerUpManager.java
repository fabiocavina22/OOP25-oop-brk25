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
    void updatePowerUp(Paddle paddle, Ball ball, int screenHeight);

    /*Update the power up timers
    */
   void updateTimer(Paddle paddle, Ball ball);

    /**Return the power up list
    */
    List<PowerUpImpl> getActivePowerUp();

    double getScoreMultiplier();

    int getFastBallFrames();

    void resetFastBallFrames();

    void spawnPowerUp(double x, double y);
    }
