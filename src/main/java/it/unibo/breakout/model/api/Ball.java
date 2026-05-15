package it.unibo.breakout.model.api;

/**
 * Represents the ball.
 * The ball moves across the field, bouncing off walls, the paddle, and bricks.
 */
public interface Ball {

    /**
     * Returns the current X position of the ball's center.
     *
     * @return the X coordinate
     */
    double getX();

    /**
     * Returns the current Y position of the ball's center.
     *
     * @return the Y coordinate
     */
    double getY();

    /**
     * Returns the radius of the ball.
     *
     * @return the radius
     */
    double getRadius();

    /**
     * Returns the current horizontal velocity component.
     *
     * @return the velocity along the X axis (positive = rightward)
     */
    double getVelocityX();

    /**
     * Returns the current vertical velocity component.
     *
     * @return the velocity along the Y axis (positive = downward)
     */
    double getVelocityY();

    /**
     * Sets the horizontal velocity component.
     *
     * @param vx the new X velocity
     */
    void setVelocityX(double vx);

    /**
     * Sets the vertical velocity component.
     *
     * @param vy the new Y velocity
     */
    void setVelocityY(double vy);

    /**
     * Moves the ball by one step according to its current velocity.
     */
    void move();

    /**
     * Reverses the horizontal direction of the ball (bounce off vertical wall or paddle side).
     */
    void bounceX();

    /**
     * Reverses the vertical direction of the ball (bounce off horizontal wall, paddle top, or brick).
     */
    void bounceY();

    /**
     * Sets the position of the ball.
     *
     * @param x the new X coordinate of the center
     * @param y the new Y coordinate of the center
     */
    void setPosition(double x, double y);

    /**
     * Returns whether the ball has fallen below the bottom boundary (lost).
     *
     * @param fieldHeight the total height of the playing field
     * @return {@code true} if the ball is out of bounds below the field
     */
    boolean isOutOfBounds(double fieldHeight);
}
