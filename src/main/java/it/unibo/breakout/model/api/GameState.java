package it.unibo.breakout.model.api;

import java.util.List;

/**
 * Read-only snapshot of the game world at a single point in time.
 *
 * <p>The view consumes this interface every tick to redraw the screen;
 * the controller and model produce it. No mutation is exposed here so
 * the view cannot accidentally change game state.
 */
public interface GameState {

    /**
     * Returns the ball currently in play.
     *
     * @return the {@link Ball}, never {@code null}
     */
    Ball getBall();

    /**
     * Returns the player-controlled paddle.
     *
     * @return the {@link Paddle}, never {@code null}
     */
    Paddle getPaddle();

    /**
     * Returns the live bricks remaining on the field.
     *
     * <p>Destroyed bricks must not appear in this list.
     *
     * @return an unmodifiable list of active {@link Brick} instances
     */
    List<Brick> getBricks();

    /**
     * Returns the player's current score.
     *
     * @return the score, always &ge; 0
     */
    int getScore();

    /**
     * Returns the number of lives the player still has.
     *
     * @return remaining lives, always &ge; 0
     */
    int getLives();

    /**
     * Returns the index (1-based) of the level currently being played.
     *
     * @return the current level number
     */
    int getCurrentLevel();

    /**
     * Returns whether the game is currently paused.
     *
     * @return {@code true} if the game loop is paused
     */
    boolean isPaused();
}
