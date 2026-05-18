package it.unibo.breakout.model.api;

public interface Brick {

    /** Returns the brick's X position in pixels. */
    double getX();

    /** Returns the brick's Y position in pixels. */
    double getY();

    /** Returns true if the brick cannot be destroyed. */
    boolean isIndestructible();

    /** Registers a hit; decreases life if the brick is destructible. */
    void hit();

    /** Returns true if the brick has no life left and is not indestructible. */
    boolean isDestroyed();

    /** Moves the brick down by the given amount of pixels. */
    void moveDown(double amount);
}