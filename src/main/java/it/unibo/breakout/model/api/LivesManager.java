package it.unibo.breakout.model.api;

/**
 * Interface representing the manager for the player's lives.
 */
public interface LivesManager {

    /**
     * Get the player's number of lives.
     *
     * @return the player's current number of lives
     */
    int getlives();

    /**
     * Manage the game over.
     *
     * @return true if the game is over, false otherwise
     */
    boolean isGameOver();

    /**
     * Is a "signal" for the life lost.
     *
     * @return true if a life was lost, false otherwise
     */
    boolean isLifeLost();

    /**
     * Is a "signal" for the life gained.
     *
     * @return true if a life was gained, false otherwise
     */
    boolean isLifeGained();

    /**
     * Decrease the player's lives by 1.
     */
    void loseLives();

    /**
     * Add a life to the player.
     */
    void addLife();
}
