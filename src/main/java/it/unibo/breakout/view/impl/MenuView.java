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
 * Main menu screen shown before the game starts.
 *
 * <p>Displays the game title and a "Start" button. When the button is clicked
 * the window is disposed and the {@code onStart} callback is invoked, which
 * should create the model objects and open the game window.
 */
public final class MenuView {

    private final JFrame frame;

    /**
     * Constructs the menu window.
     *
     * @param onStart callback invoked (on the EDT) when the player clicks "Start"
     */
    public MenuView(final Runnable onStart) {
        frame = new JFrame("Dido's Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        final JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.BLACK);

        // --- centre: title + button ---
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        final JLabel title = new JLabel("DIDO'S BREAKOUT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 46));
        title.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(title, gbc);

        final Color normalColor = new Color(30, 30, 30);
        final Color hoverColor  = new Color(220, 100, 0);

        final JButton startBtn = new JButton("Start");
        startBtn.setFont(new Font("Arial", Font.BOLD, 28));
        startBtn.setBackground(normalColor);
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setBorderPainted(false);
        startBtn.setOpaque(true);
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                startBtn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                startBtn.setBackground(normalColor);
            }
        });
        startBtn.addActionListener(e -> {
            frame.dispose();
            onStart.run();
        });
        gbc.gridy = 1;
        panel.add(startBtn, gbc);

        rootPanel.add(panel, BorderLayout.CENTER);

        // --- bottom-left: credits ---
        final JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        creditsPanel.setBackground(Color.BLACK);

        final JLabel creditsLabel = new JLabel(
            "Developed by:  Fabio Cavina  ·  Riccardo Frega  ·  Achille Montefiori  ·  Lorenzo Di Domenico"
        );
        creditsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        creditsLabel.setForeground(new Color(150, 150, 150));
        creditsPanel.add(creditsLabel);

        rootPanel.add(creditsPanel, BorderLayout.SOUTH);

        frame.add(rootPanel);
    }

    /**
     * Makes the menu window visible.
     */
    public void show() {
        frame.setVisible(true);
    }
}
