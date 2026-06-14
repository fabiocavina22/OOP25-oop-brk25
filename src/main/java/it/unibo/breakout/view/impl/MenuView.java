package it.unibo.breakout.view.impl;

import javax.swing.ImageIcon;
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
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image; // <-- AGGIUNTO
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

    private static final int FRAME_WIDTH      = 800;
    private static final int FRAME_HEIGHT     = 600;
    private static final int GBC_INSET        = 20;
    private static final int TITLE_FONT_SIZE  = 46;
    private static final int BUTTON_FONT_SIZE = 28;
    private static final int CREDITS_FONT    = 12;
    private static final int CREDITS_COLOR   = 150;
    private static final int DARK_COLOR      = 30;
    private static final int HOVER_RED       = 220;
    private static final int CREDITS_H_GAP  = 14;

    private final JFrame frame;

    /**
     * Constructs the menu window.
     *
     * @param onStart callback invoked (on the EDT) when the player clicks "Start"
     */
    public MenuView(final Runnable onStart) {
        frame = new JFrame("Dido's Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        final Image background = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/background.jpg")).getImage();

        // --- CUSTOM BACKGROUND ---
        final JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    /* adapt the image to the panel's dimensions */
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                } else {
                    /* in case the image can't be found */
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // --- centre: title + button ---
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(GBC_INSET, 0, GBC_INSET, 0);

        final JLabel title = new JLabel("DIDO'S BREAKOUT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(title, gbc);

        final Color normalColor = new Color(DARK_COLOR, DARK_COLOR, DARK_COLOR);
        final Color hoverColor  = new Color(HOVER_RED, 100, 0);

        final JButton startBtn = new JButton("Start");
        startBtn.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
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
        final JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, CREDITS_H_GAP, 10));
        creditsPanel.setBackground(Color.BLACK);

        final JLabel creditsLabel = new JLabel(
            "Developed by:  Fabio Cavina  ·  Riccardo Frega  ·  Achille Montefiori  ·  Lorenzo Di Domenico"
        );
        creditsLabel.setFont(new Font("Arial", Font.ITALIC, CREDITS_FONT));
        creditsLabel.setForeground(new Color(CREDITS_COLOR, CREDITS_COLOR, CREDITS_COLOR));
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
