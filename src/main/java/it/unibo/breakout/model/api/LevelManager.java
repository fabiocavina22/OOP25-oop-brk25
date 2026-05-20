package it.unibo.breakout.model.api;

import java.util.List;

/**
 * Manages continuous arcade-style level generation:
 * brick row spawning, downward scrolling, and progressive difficulty.
 */
public interface LevelManager {

    /**
     * Returns the list of currently active bricks on screen.
     * The list is unmodifiable; use {@link #removeBrick(Brick)} to remove entries.
     */
    List<Brick> getActiveBricks();

    /**
     * Updates brick positions and spawns new rows as needed.
     *
     * @param deltaTime time elapsed since the last frame, in seconds
     */
    void update(double deltaTime);

    /**
     * Removes a brick from the active list.
     * Call this when the ball hits and destroys a brick.
     */
    void removeBrick(Brick brick);

    /**
     * Fully resets the level state for a new game.
     * Clears all bricks, resets internal score, and regenerates initial rows.
     */
    void reset();

    /**
     * Current scroll speed in pixels per second.
     * Useful for the HUD (e.g. displaying difficulty) or debugging.
     */
    double getScrollSpeed();

    /**
     * Total number of rows generated since the start of the game.
     * Increases monotonically and drives difficulty progression.
     */
    int getRowsGenerated();

    /**
     * Checks whether any brick has reached or passed the given Y threshold.
     * Typically the paddle's Y coordinate; if true, the player loses a life.
     *
     * @param thresholdY the Y limit coordinate
     * @return true if any brick has crossed the threshold
     */
    boolean hasBricksReachedThreshold(double thresholdY);

}
