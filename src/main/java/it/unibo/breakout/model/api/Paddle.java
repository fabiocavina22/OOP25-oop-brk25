package it.unibo.breakout.model.api;

public interface Paddle {

    void moveLeft() ;
    void moveRight() ;
    void clamp(int screenWidth) ;

}