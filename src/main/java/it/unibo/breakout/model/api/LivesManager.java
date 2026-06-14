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

    /**Is a "signal" for the life gained 
    */
    boolean isLifeGained();

    /**decrease the player's lives by 1
    */
    void loseLives();

    /**add a life to the player
    */
    void addLife();
}
