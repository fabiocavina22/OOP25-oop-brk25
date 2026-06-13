package it.unibo.breakout.model.impl;
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
    public void paddleLarge(){
        if(this.width < 240){
            this.width += 30;
        }
    }

    @Override
    public void paddleShort(){
        if(this.width > 60){
            this.width -= 30;
        }
    }

    public void updateDimensions( int panelWidth, int panelHeight){
        /* the pad occupies excactly the 10% of the central panel */
        this.width = (int) (panelWidth * 0.25);

        /* the pad gets positioned at the 80% of the height leaving the 20% below  */
        this.y = (int) (panelHeight * 0.80);

        /* fallback in case the pad went out of borders */
        if (this.x + this.width > panelWidth) {
            this.x = panelWidth - this.width;
        }
    }
}