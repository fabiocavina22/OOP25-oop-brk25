package it.unibo.breakout.model.api.collisions;

public interface Collidable {

    /**
     * Get the x position of the collidable.
     */
    double getX();

    /**
     * Get the y position of the collidable.
     */
    double getY();

    /**
     * Get the width of the collidable.
     */
    int getWidth();

    /**
     * Get the height of the collidable.
     */
    int getHeight();

}
