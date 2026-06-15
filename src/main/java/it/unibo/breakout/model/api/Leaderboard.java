package it.unibo.breakout.model.api;

/**
 * The leaderboard of the game. Extends LeaderboardView with the
 * methods that modify the data and persist it to disk.
 */
public interface Leaderboard extends LeaderboardView {

<<<<<<< HEAD
public interface Leaderboard{

    /**Check if the final score can enter in the leaderboard, if the are fewer than 10 scores in the leaderboard, the newst score always     enter
    */
    boolean isHighScore(int result);

    /**Adds the name and the score to the leaderboard, keeps the scores sorted in descending order, and removes the lowest one if there are more than 10 entries
    */
=======
    /**
     * Checks if the given score is high enough to enter the leaderboard.
     *
     * @param result the score to check
     * @return true if it is a high score, false otherwise
     */
    boolean isHighScore(int result);

    /**
     * Adds a new entry to the leaderboard, keeping it sorted and limited.
     *
     * @param name the name of the player
     * @param result the score of the player
     */
>>>>>>> cad08518d7447abe16b57fd4ec323c385a395e45
    void add(String name, int result);

    /**
     * Saves the leaderboard to the file.
     */
    void save();
}