package it.unibo.breakout.model.api;

public interface LivesManager {

    /**Get the player's number of lives
    */
    int getlives();

    /**manage the game over
    */
    boolean isGameOver();

    /**Is a "signal" for the life lost
    */
    boolean isLifeLost();

    boolean isLifeGained();

    void loseLives();

    void addLife();
}
