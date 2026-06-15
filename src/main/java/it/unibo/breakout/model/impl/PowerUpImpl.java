package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.PowerUp;

/**
 * Power up capsule that falls from a destroyed brick towards the paddle.
 */
public final class PowerUpImpl implements PowerUp {

    private static final double FALL_SPEED = 3.0;

    private final double x;
    private double y;
    private final int type;

    /**
     *Creates a power up capsule at the given position with the given type.
     * @param x
     * @param y
     * @param type
     */
    public PowerUpImpl(final double x, final double y, final int type) {
        this.x = x;
        this.y = y;
        this.type = type;
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
    public int getType() {
        return type;
    }

    @Override
    public boolean isOutOfBounds(final double screenHeight) {
        return y > screenHeight;
    }

    @Override
    public void fall() {
        this.y += FALL_SPEED;
    }
}
