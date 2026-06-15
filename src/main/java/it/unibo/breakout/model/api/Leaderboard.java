package it.unibo.breakout.model.api;

/**
 * The leaderboard of the game. Extends LeaderboardView with the
 * methods that modify the data and persist it to disk.
 */
public interface Leaderboard extends LeaderboardView {

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
    void add(String name, int result);

    /**
     * Saves the leaderboard to the file.
     */
    void save();
}
