package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Paddle;

/**
 * Concrete implementation of the ball entity.
 */
public final class BallImpl implements Ball {


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

    @Override
    public int getWidth() {
       return (int) (radius * 2);
    }

    @Override
    public int getHeight() {
        return (int) (radius * 2);
    }

    @Override
    public void updateDimensions(final int panelWidth, final int panelHeight, final Paddle paddle) {

        if (this.velocityX == 0 && this.velocityY == 0) {
            // Se il gioco non è partito, centra matematicamente la palla sul nuovo pad
            this.x = paddle.getX() + paddle.getWidth() / 2.0 - this.radius;
            this.y = paddle.getY() - this.radius;
        } else {
            // Se la palla sta volando e la finestra si stringe improvvisamente,
            // impediamo che la palla esca fuori dai muri di destra o dal fondo
            if (this.x + this.radius > panelWidth) {
                this.x = panelWidth - this.radius;
            }
            if (this.y + this.radius > panelHeight) {
                this.y = panelHeight - this.radius;
            }
        }
    }
}
