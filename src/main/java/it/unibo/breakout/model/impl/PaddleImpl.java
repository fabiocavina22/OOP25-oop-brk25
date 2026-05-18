package it.unibo.breakout.model.impl;

//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import javax.swing.JPanel;

import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.collisions.Collidable;


    public class PaddleImpl implements Paddle, Collidable {

        private int x ;
        private final int y;
        private int width;
        private final int height;
        private int speed;



    public PaddleImpl (int x, int y, int width, int height, int speed){
        this.x = x ;
        this.y = y ;
        this.width = width ;
        this.height = height ;
        this.speed = speed ;
    }

    @Override
    public void moveLeft() {
        x -= speed;
    }
    @Override
    public void moveRight() {
        x += speed;
    }

    @Override
    public void clamp(int screenWidth) {
        if (x < 0) x = 0;
        if (x + width > screenWidth) x = screenWidth - width;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}