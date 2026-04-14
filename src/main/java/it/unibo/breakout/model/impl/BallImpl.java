package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.Ball;

/**
 * Default implementation of {@link Ball}.
 */

public class BallImpl implements Ball {

    private double x;
    private double y;
    private final double radius;
    private double velocityX;
    private double velocityY;

    /**
     * Creates a new ball with the given position, radius, and initial velocity.
     *
     * @param x         initial X coordinate of the center
     * @param y         initial Y coordinate of the center
     * @param radius    radius of the ball (must be positive)
     * @param velocityX initial horizontal velocity
     * @param velocityY initial vertical velocity
     */

    public BallImpl(final double x, final double y, final double radius, final double velocityX, final double velocityY) {

        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive, got: " + radius);
        }

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
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
    public double getRadius() {
        return radius;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityX(final double vx) {
        this.velocityX = vx;
    }

    @Override
    public void setVelocityY(final double vy) {
        this.velocityY = vy;
    }

    @Override
    public void move() {
        x += velocityX;
        y += velocityY;
    }

    @Override
    public void bounceX() {
        velocityX = -velocityX;
    }

    @Override
    public void bounceY() {
        velocityY = -velocityY;
    }

    @Override
    public void setPosition(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isOutOfBounds(final double fieldHeight) {
        return y + radius > fieldHeight;
    }
}
