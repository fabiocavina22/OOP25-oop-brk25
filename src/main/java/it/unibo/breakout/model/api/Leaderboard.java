package it.unibo.breakout.model.api;

import java.util.List;

public interface Leaderboard{
        
    /**Check if the final score can enter in the leaderboard, if the are fewer than 10 scores in the leaderboard, the newst score always     enter 
    */
    boolean isHighScore(int result);

    /**Adds the name and the score to the leaderboard, keeps the scores sorted in descending order, and removes the lowest one if there are more than 10 entries
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
