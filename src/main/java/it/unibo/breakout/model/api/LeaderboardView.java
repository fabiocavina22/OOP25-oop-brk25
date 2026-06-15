package it.unibo.breakout.model.api;

import java.util.List;

/**
 * Read only view of the leaderboard, exposed to the rendering side
 * so that the UI cannot accidentally modify the underlying data.
 */
public interface LeaderboardView {

    /**
     * Returns the names of the players currently in the leaderboard,
     * sorted from the highest score to the lowest.
     *
     * @return a list of the player names
     */
    List<String> getNames();

    /**
     * Returns the scores currently in the leaderboard,
     * sorted from the highest to the lowest, aligned with the names list.
     *
     * @return a list of the scores
     */
    List<Integer> getScores();
}
