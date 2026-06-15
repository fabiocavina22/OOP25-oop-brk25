package it.unibo.breakout.model.api.collisions;

/**
 * Rappresentation of an object which can collide with an another object.
 */
public interface Collidable {

    /**
     * Get the x position of the collidable.
     * @return collidable's x coordinate
     */
    double getX();

    /**
     * Get the y position of the collidable.
     * @return collidable's y coordinate
     */
    double getY();

    /**
     * Get the width of the collidable.
     * @return collidable's width
     */
    int getWidth();

    /**
     * Get the height of the collidable.
     * @return collidable's Height
     */
    int getHeight();

}
