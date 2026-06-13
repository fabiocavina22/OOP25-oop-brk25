package it.unibo.breakout.view.impl;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.awt.*;

import it.unibo.breakout.model.impl.LeaderboardImpl;

public class RightPanel extends JPanel {

        private final LeaderboardImpl leaderboard;

    public RightPanel(LeaderboardImpl leaderboard) {
        this.leaderboard = leaderboard;

        setBackground(Color.WHITE);

        Border leftBorder = BorderFactory.createMatteBorder(
                0, // top
                10, // left
                0, // bottom
                0, // right
                Color.BLACK
        );

        Border padding = BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
        );

        setBorder(
                BorderFactory.createCompoundBorder(
                        leftBorder,
                        padding
                )
        );

    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        String title = "LEADERBOARD";
        int titleX = (panelWidth - fm.stringWidth(title)) / 2;
        int titleY = panelHeight / 4;
        g.drawString(title, titleX, titleY);

        List<String> names = leaderboard.getNames();
        List<Integer> scores = leaderboard.getScores();

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        fm = g.getFontMetrics();
        int startY = titleY + 60;
        int colLeft = panelWidth / 4;
        int colRight = panelWidth * 3 / 4;

        for(int i = 0; i < names.size(); i++){
                String nameEntry = (i + 1) + ") " + names.get(i);
                String scoreEntry = String.valueOf(scores.get(i));

                g.drawString(nameEntry, colLeft, startY + i * 38);
                g.drawString(scoreEntry, colRight - fm.stringWidth(scoreEntry), startY + i * 38);
        }
    }
}
