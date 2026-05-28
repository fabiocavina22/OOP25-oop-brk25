package it.unibo.breakout.model.api;

public interface PowerUp {

    
    
    /**Return the X position of the bonus/malus capsule
    */
    double getX();

    /**Return the X position of the bonus/malus capsule
    */
    double getY();

    /**Manage the fall of the capsule
    */
    void fall();

    /**Return the effect of the Power up
    */
    int getType();

    /**manage the disappearance of the capsule 
    */
    boolean isOutOfBounds(double screenHeight);
    }
