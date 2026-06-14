package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

public interface Paddle extends Collidable {

    /**
     * moves the paddle to its left
     */
    void moveLeft() ;

    /**
     * moves the paddle to its right
     */
    void moveRight() ;

    /**
     *
     * @return the pad's speed
     */
    int getSpeed();

    //*Denies the paddle to go out of the choosen limits */
    void clamp(int screenWidth) ;

    void paddleLarge();

    void paddleShort();

    /**
     *  Updates the paddle's dimensions if the screen's dimensions changes
     */
    void updateDimensions(int newWidth,  int newHeight);

}