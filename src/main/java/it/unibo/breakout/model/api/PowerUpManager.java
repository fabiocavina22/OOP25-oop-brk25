package it.unibo.breakout.model.api;

import java.util.List;

import it.unibo.breakout.model.impl.PowerUpImpl;

/**
 * Interface representing the manager for all power-ups.
 */
public interface PowerUpManager extends PowerUpView {

    /**
     * Return the double points timer.
     *
     * @return the double points timer value
     */
    long getDoublePointsTimer();

    /**
     * Return the paddle large timer.
     *
     * @return the paddle large timer value
     */
    long getPaddleLargeTimer();

    /**
     * Return the paddle short timer.
     *
     * @return the paddle short timer value
     */
    long getPaddleShortTimer();

    /**
     * Return the freeze blocks timer.
     *
     * @return the freeze blocks timer value
     */
    long getFreezeBlocksTimer();

    /**
     * Return the half points timer.
     *
     * @return the half points timer value
     */
    long getHalfPointsTimer();

    /**
     * Return the fast ball timer.
     *
     * @return the fast ball timer value
     */
    long getFastBallTimer();

    /**
     * Return whether the blocks are currently frozen.
     *
     * @return true if the blocks are frozen, false otherwise
     */
    boolean isFrozen();

    /**
     * Update the position of the power up.
     *
     * @param paddle the paddle of the game
     * @param ball the ball of the game
     * @param screenHeight the height of the screen
     * @param livesManager the manager for player lives
     */
    void updatePowerUp(Paddle paddle, Ball ball, int screenHeight, LivesManager livesManager);

    /**
     * Update the power up timers.
     *
     * @param paddle the paddle of the game
     * @param ball the ball of the game
     */
    void updateTimer(Paddle paddle, Ball ball);

    /**
     * Return the power up list.
     *
     * @return the list of active power ups
     */
    @Override
    List<PowerUpImpl> getActivePowerUp();

    /**
     * Return the current score multiplier.
     *
     * @return the current score multiplier value
     */
    double getScoreMultiplier();

    /**
     * Return the frames of the fast ball effect.
     *
     * @return the number of frames of the fast ball effect
     */
    int getFastBallFrames();

    /**
     * Reset the fast ball effect frames.
     */
    void resetFastBallFrames();

    /**
     * Spawn the power up.
     *
     * @param x the x coordinate where the power up spawns
     * @param y the y coordinate where the power up spawns
     */
    void spawnPowerUp(double x, double y);
}
