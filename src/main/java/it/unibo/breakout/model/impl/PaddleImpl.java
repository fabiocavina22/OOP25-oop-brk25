package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Paddle;

/**
 * Implementation of the {@link Paddle} controlled by the player.
 * Handles horizontal movement, screen clamping, width bonuses/maluses
 * and rescaling relative to the central panel size.
 */
public final class PaddleImpl implements Paddle {

    private int x;
    private int y;
    private int width;
    private final int height;
    private final int speed;

    private static final int MAX_WIDTH = 240;
    private static final int MIN_WIDTH = 60;
    private static final int CHANGE = 30;

    private static final double W_PROPORTION = 0.25;
    private static final double Y_PROPORTION = 0.8;

    /**
     * Creates a paddle at the given position with the given size and speed.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     * @param width the initial width
     * @param height the height
     * @param speed the horizontal movement speed
     */
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
        if (this.width < MAX_WIDTH) {
            this.width += CHANGE;
        }
    }

    @Override
    public void paddleShort() {
        if (this.width > MIN_WIDTH) {
            this.width -= CHANGE;
        }
    }

    @Override
    public void updateDimensions(final int panelWidth, final int panelHeight) {
        /*
        *the pad occupies excactly the 25% of the central panel
        **/
        this.width = (int) (panelWidth * W_PROPORTION);

        /*
        * the pad gets positioned at the 80% of the height leaving the 20% below
        **/
        this.y = (int) (panelHeight * Y_PROPORTION);

        /*
        * fallback in case the pad went out of borders
        */
        if (this.x + this.width > panelWidth) {
            this.x = panelWidth - this.width;
        }
    }
}
