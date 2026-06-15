package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

/**
 * Interface representing a Brick in the breakout game.
 * Bricks can have different coordinates, points of life, and types.
 */
public interface Brick extends Collidable {

    /**
     * Returns the brick's X position in pixels.
     *
     * @return the X coordinate in pixels
     */
    @Override
    double getX();

    /**
     * Returns the brick's Y position in pixels.
     *
     * @return the Y coordinate in pixels
     */
    @Override
    double getY();

    /**
     * Returns true if the brick cannot be destroyed.
     *
     * @return true if indestructible, false otherwise
     */
    boolean isIndestructible();

    /** Registers a hit; decreases life if the brick is destructible. */
    void hit();

    /**
     * Returns true if the brick has no life left and is not indestructible.
     *
     * @return true if destroyed, false otherwise
     */
    boolean isDestroyed();

    /**
     * Moves the brick down by the given amount of pixels.
     *
     * @param amount the vertical movement in pixels
     */
    void moveDown(double amount);

    /**
     * Returns the points of life of the brick.
     *
     * @return the remaining hits of the brick
     */
    int getLife();

    /**
     * Returns the specific row of bricks.
     *
     * @return the identifier of the row
     */
    int getRowId();

    /**
     * Returns the specific type of the block.
     *
     * @return the integer representing the type of the brick
     */
    int getType();

    /**
     * Sets the x position of the block.
     *
     * @param x the new horizontal position in pixels
     */
    void setX(double x);

    /**
     * Sets the width of the block.
     *
     * @param width the new width in pixels
     */
    void setWidth(int width);

    /**
     * Sets the y position of the block.
     *
     * @param y the new vertical position in pixels
     */
    void setY(double y);

    /**
     * Sets the height of the block.
     *
     * @param height the new height in pixels
     */
    void setHeight(int height);

    /**
     * Returns the column index.
     *
     * @return the horizontal column index
     */
    int getColIndex();

}
