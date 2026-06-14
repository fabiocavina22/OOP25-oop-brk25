package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Paddle;


public final class PaddleImpl implements Paddle {

    private int x;
    private int y;
    private int width;
    private final int height;
    private final int speed;

    private final int MAXWIDTH = 240;
    private final int MINWIDTH = 60;
    private final int CHANGE = 30;

    private final double WPROPORTION = 0.25;
    private final double YPROPORTION = 0.8;


    public PaddleImpl(final int x, final int y, final int width, final int height, final int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
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
    public void clamp(final int screenWidth) {
        if (x < 0) {
            x = 0;
        }
        if (x + width > screenWidth) {
        x = screenWidth - width;
        }
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
    public int getSpeed() {
        return this.speed;
    }

    @Override
    public void paddleLarge() {
        if (this.width < MAXWIDTH) {
            this.width += CHANGE;
        }
    }

    @Override
    public void paddleShort() {
        if (this.width > MINWIDTH) {
            this.width -= CHANGE;
        }
    }

    @Override
    public void updateDimensions(final int panelWidth, final int panelHeight) {
        /*
        *the pad occupies excactly the 10% of the central panel
        **/
        this.width = (int) (panelWidth * WPROPORTION);

        /*
        * the pad gets positioned at the 80% of the height leaving the 20% below
        **/
        this.y = (int) (panelHeight * YPROPORTION);

        /*
        * fallback in case the pad went out of borders
        */
        if (this.x + this.width > panelWidth) {
            this.x = panelWidth - this.width;
        }
    }
}