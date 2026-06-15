package it.unibo.breakout.model.api;

/**
 * A power up capsule that falls from a destroyed brick and grants
 * a timed effect when the paddle catches it.
 */
public interface PowerUp {

    /**
     * Returns the current x coordinate of the capsule.
     *
     * @return the x coordinate of the capsule
     */
    double getX();

    /**
     * Returns the current y coordinate of the capsule.
     *
     * @return the y coordinate of the capsule
     */
    double getY();

    /**
     * Returns the type of effect granted by this capsule.
     *
     * @return the type of the power up
     */
    int getType();

    /**
     * Makes the capsule fall by one step.
     */
    void fall();

    /**
     * Checks whether the capsule has fallen below the bottom of the screen.
     *
     * @param screenHeight the height of the screen, used as the lower bound
     * @return true if the capsule is below the screen, false otherwise
     */
    boolean isOutOfBounds(double screenHeight);
}
