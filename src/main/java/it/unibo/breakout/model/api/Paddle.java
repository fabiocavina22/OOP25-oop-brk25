package it.unibo.breakout.model.api;

import java.awt.Graphics;

public interface Paddle {

    void moveLeft() ;
    void moveRight() ;
    void clamp(int screenWidth) ;
    void draw(Graphics g);

}