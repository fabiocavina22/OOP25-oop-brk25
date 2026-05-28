package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

public interface Brick extends Collidable{

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

    /** Returns the points of life of the brick */
    int getLife();

    /** Returns the specific row of bricks */
    int getRowId();

    //**Returns the specific type of the block */
    int getType();

    void setX(double x);

    void setWidth(int width);

    void setY(double y);

    void setHeight(int height);
}