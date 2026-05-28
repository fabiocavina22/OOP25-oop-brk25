package it.unibo.breakout.model.impl;

//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import javax.swing.JPanel;

import it.unibo.breakout.model.api.Paddle;


    public class PaddleImpl implements Paddle {

        private int x ;
        private int y;
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

    @Override
    public void updateDimensions( int panelWidth, int panelHeight){
        // 1. Il pad occupa sempre esattamente il 10% della larghezza del pannello centrale
        this.width = (int) (panelWidth * 0.25);

        // 2. Il pad si posiziona sempre all'80% dell'altezza (lasciando il 20% di spazio sotto)
        this.y = (int) (panelHeight * 0.80);

        // 3. Controllo di sicurezza sull'asse X
        // Se restringendo la finestra il pad finisce fuori dal bordo destro, lo spingiamo dentro
        if (this.x + this.width > panelWidth) {
            this.x = panelWidth - this.width;
        }
    }
}