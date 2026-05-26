package it.unibo.breakout.view.impl;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Game over screen shown when the player loses all lives or bricks reach the paddle.
 *
 * <p>Displays the final score and two buttons: "Play Again" and "Quit".
 */
public final class GameOverView {

    private final JFrame frame;

    /**
     * Constructs the game over window.
     *
     * @param finalScore  the score achieved during the game session
     * @param onPlayAgain callback invoked when the player clicks "Play Again"
     * @param onQuit      callback invoked when the player clicks "Quit"
     */
    public GameOverView(final int finalScore, final Runnable onPlayAgain, final Runnable onQuit) {
        frame = new JFrame("Dido's Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        final JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.BLACK);

        // --- centre: title + score + buttons ---
        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(18, 0, 18, 0);

        final JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 56));
        titleLabel.setForeground(new Color(220, 50, 50));
        gbc.gridy = 0;
        centerPanel.add(titleLabel, gbc);

        final JLabel scoreLabel = new JLabel("Score: " + finalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        centerPanel.add(scoreLabel, gbc);

        final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonsPanel.setBackground(Color.BLACK);
        buttonsPanel.add(buildButton("Play Again", new Color(220, 100, 0), () -> {
            frame.dispose();
            onPlayAgain.run();
        }));
        buttonsPanel.add(buildButton("Quit", new Color(180, 30, 30), () -> {
            frame.dispose();
            onQuit.run();
        }));

        gbc.gridy = 2;
        centerPanel.add(buttonsPanel, gbc);

        rootPanel.add(centerPanel, BorderLayout.CENTER);
        frame.add(rootPanel);
    }

    private JButton buildButton(final String text, final Color hoverColor, final Runnable action) {
        final Color normalColor = new Color(30, 30, 30);
        final JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 26));
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                btn.setBackground(normalColor);
            }
        });
        btn.addActionListener(e -> action.run());
        return btn;
    }

    /**
     * Makes the game over window visible.
     */
    public void show() {
        frame.setVisible(true);
    }
}
