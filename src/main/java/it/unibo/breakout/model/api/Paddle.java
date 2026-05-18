package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

public interface Paddle extends Collidable {

    void moveLeft() ;
    void moveRight() ;
    void clamp(int screenWidth) ;

}