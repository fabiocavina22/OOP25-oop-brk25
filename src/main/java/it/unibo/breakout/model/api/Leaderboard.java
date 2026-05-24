package it.unibo.breakout.model.api;

import java.util.List;

public interface Leaderboard{
        
    /**Check is the final score can enter in the leaderboard, if the are less than 10 scores in the leaderboard, the newst score will enter 
    */
    boolean isHighScore(int result);

    /**Add the name and the score in the leaderboard, manage the scores in it
    */
    void add(String name, int result);

    /**Write the name and the score in the file.txt, create the file if it doesn't exist
    */
    void save();

    /**Return the list of names in the leaderboard
    */
    List<String> getNames();

    /**Return the list of scores in the leaderboard
    */
    List<Integer> getScores();
}
