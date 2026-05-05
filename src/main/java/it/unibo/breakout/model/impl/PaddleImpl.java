package it.unibo.breakout.model.impl;

//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import javax.swing.JPanel;
import java.awt.Graphics;


    public class PaddleImpl {

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

    // Movimento
    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    // Limiti schermo
    public void clamp(int screenWidth) {
        if (x < 0) x = 0;
        if (x + width > screenWidth) x = screenWidth - width;
    }

    // Disegno (Swing)
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height);
    }

    // Getter utili
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}