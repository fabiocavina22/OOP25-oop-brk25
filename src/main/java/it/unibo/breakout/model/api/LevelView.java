package it.unibo.breakout.model.api;

import java.util.List;

/**
 * Read-only view of the level, exposing only the active bricks for rendering.
 */
public interface LevelView {

    /**
     * Returns the list of currently active bricks on screen.
     *
     * @return the unmodifiable list of active bricks
     */
    List<Brick> getActiveBricks();
}
