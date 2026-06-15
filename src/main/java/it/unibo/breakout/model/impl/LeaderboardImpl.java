package it.unibo.breakout.model.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import it.unibo.breakout.model.api.Leaderboard;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class LeaderboardImpl implements Leaderboard{

    private static final int MAX = 10;
    private static final String FILE_PATH = "leaderboard.txt";

    private final List<String> names;
    private final List<Integer> scores;

    /**
     * Loads the leaderboard from the file at startup; if the file doesn't exist the leaderboard starts empty
     */
    public LeaderboardImpl(){
        this.names = new ArrayList<>();
        this.scores = new ArrayList<>();

        try (final BufferedReader r = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(FILE_PATH), "UTF-8"))){
                    String line;
                    while((line = r.readLine()) != null){
                        String[] parts = line.split(",");
                        if(parts.length == 2){
                            names.add(parts[0]);
                            scores.add(Integer.parseInt(parts[1]));
                        }
                    }
                }
                catch(IOException e){}
    }

    @Override
    public boolean isHighScore(int result){
        if(scores.size() < MAX){
            return true;
        }
        if(result > scores.get(scores.size() - 1)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void add(String name, int result){
        names.add(name.toUpperCase());
        scores.add(result);
        for(int i = 0; i < scores.size() -1; i++){
            for( int j = i + 1; j < scores.size(); j++){
                if(scores.get(j) > scores.get(i)){
                    Collections.swap(scores, i, j);
                    Collections.swap(names, i, j);
                }
            }
        }
        if(scores.size() > MAX){
            names.remove(MAX);
            scores.remove(MAX);
        }
    }

    @Override
    public void save(){
        try(final BufferedWriter w = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(FILE_PATH),"UTF-8"))){
                    for(int i = 0; i < names.size(); i++){
                        w.write(names.get(i) + "," + scores.get(i));
                        w.newLine();
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
    }

    @Override
    public List<String> getNames(){
        return names;
    }

    @Override
    public List<Integer> getScores(){
        return scores;
    }
}
