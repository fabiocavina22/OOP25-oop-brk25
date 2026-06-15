package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

/**
 * Represents the paddle controlled by the player in the game.
 */
public interface Paddle extends Collidable {

    /**
     *  moves the paddle to its left.
     */
    void moveLeft();

    /**
     * moves the paddle to its right.
     **/
    void moveRight();

    /**
     *Gets the speed of the paddle.
     *
     * @return the paddle's speed
     */
    int getSpeed();

    /**
     *Denies the paddle to go out of the chosen limits.
     * @param screenWidth the width of the screen boundaries
     */
    void clamp(int screenWidth);

    /**
     *Increases the paddle's size.
     */
    void paddleLarge();

    /**
     *Decreases the paddle's size.
     */
    void paddleShort();

    /**
     *Updates the paddle's dimensions if the screen's dimensions change.
     *
     * @param newWidth  the new width of the screen
     * @param newHeight the new height of the screen
     */
    void updateDimensions(int newWidth,  int newHeight);

}
