package it.unibo.breakout.model.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.breakout.model.api.Leaderboard;

/**
 * Implementation of the leaderboard backed by a plain text file.
 * Keeps the top scores in memory and loads/saves them from disk.
 */
public final class LeaderboardImpl implements Leaderboard {

    private static final int MAX = 10;
    private static final String FILE_PATH = "leaderboard.txt";

    private final List<String> names;
    private final List<Integer> scores;

    /**
     * Loads the leaderboard from the file at startup;
     * if the file does not exist, the leaderboard starts empty.
     */
    public LeaderboardImpl() {
        this.names = new ArrayList<>();
        this.scores = new ArrayList<>();

        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
                String line = r.readLine();
                while (line != null) {
                    final String[] parts = line.split(",");
                    if (parts.length == 2) {
                        names.add(parts[0]);
                        scores.add(Integer.parseInt(parts[1]));
                    }
                    line = r.readLine();
                }
            } catch (IOException ignored) {
            }
        }

    @Override
    public boolean isHighScore(final int result) {
        return scores.size() < MAX || result > scores.get(scores.size() - 1);
    }

    @Override
    public void add(final String name, final int result) {
        names.add(name.toUpperCase(java.util.Locale.ROOT));
        scores.add(result);
        for (int i = 0; i < scores.size() - 1; i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(j) > scores.get(i)) {
                    Collections.swap(scores, i, j);
                    Collections.swap(names, i, j);
                }
            }
        }
        if (scores.size() > MAX) {
            names.remove(MAX);
            scores.remove(MAX);
        }
    }

    @Override
    public void save() {
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(
                new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (int i = 0; i < names.size(); i++) {
                w.write(names.get(i) + "," + scores.get(i));
                w.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    @Override
    public List<Integer> getScores() {
        return new ArrayList<>(scores);
    }
}
