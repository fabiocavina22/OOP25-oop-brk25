package it.unibo.breakout.view.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import it.unibo.breakout.model.impl.LeaderboardImpl;

/**
 * Side panel that displays the leaderboard with the top scores.
 */
public final class RightPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int LEFT_BORDER_THICKNESS = 10;
    private static final int PADDING = 10;
    private static final int TITLE_FONT_SIZE = 28;
    private static final int ENTRY_FONT_SIZE = 20;

    private static final int TITLE_VERTICAL_DIVISOR = 4;
    private static final int TITLE_GAP = 60;
    private static final int ROW_SPACING = 38;
    private static final int COL_LEFT_DIVISOR = 4;
    private static final int COL_RIGHT_NUMERATOR = 3;
    private static final int COL_RIGHT_DIVISOR = 4;

    private final LeaderboardImpl leaderboard;

    /**
     * Creates the right side panel showing the given leaderboard.
     *
     * @param leaderboard the leaderboard to display
     */
    public RightPanel(final LeaderboardImpl leaderboard) {
        super();
        this.leaderboard = leaderboard;

        super.setBackground(Color.WHITE);

        final Border leftBorder = BorderFactory.createMatteBorder(
                0,
                LEFT_BORDER_THICKNESS,
                0,
                0,
                Color.BLACK
        );

        final Border padding = BorderFactory.createEmptyBorder(
                PADDING,
                PADDING,
                PADDING,
                PADDING
        );

        super.setBorder(
                BorderFactory.createCompoundBorder(
                        leftBorder,
                        padding
                )
        );
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        FontMetrics fm = g.getFontMetrics();
        final String title = "LEADERBOARD";
        final int titleX = (panelWidth - fm.stringWidth(title)) / 2;
        final int titleY = panelHeight / TITLE_VERTICAL_DIVISOR;
        g.drawString(title, titleX, titleY);

        final List<String> names = leaderboard.getNames();
        final List<Integer> scores = leaderboard.getScores();

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, ENTRY_FONT_SIZE));
        fm = g.getFontMetrics();
        final int startY = titleY + TITLE_GAP;
        final int colLeft = panelWidth / COL_LEFT_DIVISOR;
        final int colRight = panelWidth * COL_RIGHT_NUMERATOR / COL_RIGHT_DIVISOR;

        for (int i = 0; i < names.size(); i++) {
            final String nameEntry = (i + 1) + ") " + names.get(i);
            final String scoreEntry = String.valueOf(scores.get(i));

            g.drawString(nameEntry, colLeft, startY + i * ROW_SPACING);
            g.drawString(scoreEntry, colRight - fm.stringWidth(scoreEntry), startY + i * ROW_SPACING);
        }
    }
}
