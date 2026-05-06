package it.unibo.breakout.view.impl;

import it.unibo.breakout.controller.api.Controller;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.GameState;
import it.unibo.breakout.view.api.GameView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Swing-based implementation of {@link GameView}.
 *
 * <p>The window contains a single {@link CardLayout} root panel with three
 * named cards: the main menu, the game canvas, and the game-over screen.
 * Switching between them is done via the {@link GameView} methods.
 *
 * <p>Keyboard input captured while the game card is active is forwarded to
 * the registered {@link Controller} via {@link #setController(Controller)}.
 * The controller must be set before {@link #showGame()} is called.
 */
public final class SwingGameView extends JFrame implements GameView {

    private static final long serialVersionUID = 1L;

    private static final String CARD_MENU     = "MENU";
    private static final String CARD_GAME     = "GAME";
    private static final String CARD_GAMEOVER = "GAMEOVER";

    private static final int WINDOW_WIDTH  = 600;
    private static final int WINDOW_HEIGHT = 700;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     rootPanel  = new JPanel(cardLayout);
    private final GamePanel  gamePanel  = new GamePanel();
    private final JLabel     scoreLabel = new JLabel("", SwingConstants.CENTER);

    private Controller controller;

    /**
     * Constructs the window and builds all three screens.
     *
     * <p>The window is not yet visible after construction; call
     * {@link #showMenu()} to display it for the first time.
     */
    public SwingGameView() {
        super("Dido's Breakout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        rootPanel.add(buildMenuPanel(),     CARD_MENU);
        rootPanel.add(gamePanel,            CARD_GAME);
        rootPanel.add(buildGameOverPanel(), CARD_GAMEOVER);
        add(rootPanel);
    }

    /**
     * Registers the controller that will receive input events from this view.
     *
     * <p>Must be called before the game screen is shown so that keyboard events
     * can be forwarded correctly.
     *
     * @param controller the controller to notify; must not be {@code null}
     */
    public void setController(final Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Also makes the window visible if it is not already.
     */
    @Override
    public void showMenu() {
        cardLayout.show(rootPanel, CARD_MENU);
        setVisible(true);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Requests keyboard focus for the game canvas so that key events are
     * delivered to the registered {@link Controller}.
     */
    @Override
    public void showGame() {
        cardLayout.show(rootPanel, CARD_GAME);
        gamePanel.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method is thread-safe: it may be called from the game-loop thread.
     * The actual repaint is scheduled on the Event Dispatch Thread.
     */
    @Override
    public void render(final GameState state) {
        gamePanel.update(state);
    }

    /** {@inheritDoc} */
    @Override
    public void showGameOver(final int finalScore) {
        scoreLabel.setText("Final Score: " + finalScore);
        cardLayout.show(rootPanel, CARD_GAMEOVER);
    }

    // -------------------------------------------------------------------------
    // Private screen builders
    // -------------------------------------------------------------------------

    /**
     * Builds the main menu panel with a title and a "Start" button.
     *
     * @return the assembled menu {@link JPanel}
     */
    private JPanel buildMenuPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(16, 0, 16, 0);

        final JLabel title = new JLabel("DIDO'S BREAKOUT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(title, gbc);

        final JButton startBtn = new JButton("Start");
        startBtn.setFont(new Font("Arial", Font.PLAIN, 26));
        startBtn.addActionListener(e -> {
            if (controller != null) {
                controller.onNewGame();
            }
        });
        gbc.gridy = 1;
        panel.add(startBtn, gbc);

        return panel;
    }

    /**
     * Builds the game-over panel with a "GAME OVER" title, the final score
     * label (populated dynamically), and a "Play Again" button.
     *
     * @return the assembled game-over {@link JPanel}
     */
    private JPanel buildGameOverPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(16, 0, 16, 0);

        final JLabel title = new JLabel("GAME OVER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setForeground(Color.RED);
        gbc.gridy = 0;
        panel.add(title, gbc);

        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        panel.add(scoreLabel, gbc);

        final JButton playAgainBtn = new JButton("Play Again");
        playAgainBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        playAgainBtn.addActionListener(e -> {
            if (controller != null) {
                controller.onNewGame();
            }
        });
        gbc.gridy = 2;
        panel.add(playAgainBtn, gbc);

        return panel;
    }

    // -------------------------------------------------------------------------
    // Inner game panel
    // -------------------------------------------------------------------------

    /**
     * The canvas on which the live game world is painted each tick.
     *
     * <p>It holds a {@code volatile} reference to the latest {@link GameState}
     * snapshot so that writes from the game-loop thread and reads on the EDT
     * are safely published without synchronisation overhead.
     *
     * <p><strong>Paddle rendering:</strong> The {@code Paddle} interface does
     * not yet expose position or dimension getters. Paddle drawing will be
     * added once the teammate extends the interface.
     */
    private final class GamePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        /** Pixel height reserved for the HUD strip at the top of the panel. */
        private static final int HUD_HEIGHT = 32;

        /** Fallback brick dimensions used until Brick exposes its own size. */
        private static final int BRICK_W = 58;
        private static final int BRICK_H = 18;

        private volatile GameState state;

        /**
         * Constructs the game panel and attaches the keyboard listener that
         * forwards events to the outer class's {@link Controller}.
         */
        private GamePanel() {
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(final KeyEvent e) {
                    if (controller == null) {
                        return;
                    }
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            controller.onLeftPressed();
                            break;
                        case KeyEvent.VK_RIGHT:
                            controller.onRightPressed();
                            break;
                        case KeyEvent.VK_P:
                        case KeyEvent.VK_ESCAPE:
                            controller.onPauseToggle();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void keyReleased(final KeyEvent e) {
                    if (controller == null) {
                        return;
                    }
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            controller.onLeftReleased();
                            break;
                        case KeyEvent.VK_RIGHT:
                            controller.onRightReleased();
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        /**
         * Stores a new game-state snapshot and schedules a repaint on the EDT.
         *
         * <p>Safe to call from any thread.
         *
         * @param newState the latest snapshot; must not be {@code null}
         */
        void update(final GameState newState) {
            this.state = newState;
            SwingUtilities.invokeLater(this::repaint);
        }

        /**
         * Paints the current game state: HUD strip, bricks, ball, and — when
         * paused — a translucent overlay with "PAUSED".
         *
         * @param g the graphics context provided by Swing
         */
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final GameState snapshot = this.state;
            if (snapshot == null) {
                return;
            }
            drawHud(g, snapshot);
            drawBricks(g, snapshot);
            drawBall(g, snapshot);
            if (snapshot.isPaused()) {
                drawPauseOverlay(g);
            }
        }

        /**
         * Draws the HUD strip showing score, lives, and current level.
         *
         * @param g the graphics context
         * @param s the current game state snapshot
         */
        private void drawHud(final Graphics g, final GameState s) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("Score: " + s.getScore(),           8,                 22);
            g.drawString("Lives: " + s.getLives(),           getWidth() / 2 - 30, 22);
            g.drawString("Level: " + s.getCurrentLevel(),    getWidth() - 88,   22);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(0, HUD_HEIGHT, getWidth(), HUD_HEIGHT);
        }

        /**
         * Draws all active bricks. Indestructible bricks are rendered in grey;
         * normal bricks in cyan, with a black 1-pixel border.
         *
         * @param g the graphics context
         * @param s the current game state snapshot
         */
        private void drawBricks(final Graphics g, final GameState s) {
            final List<Brick> bricks = s.getBricks();
            for (final Brick brick : bricks) {
                final int bx = (int) brick.getX();
                final int by = (int) brick.getY() + HUD_HEIGHT;
                g.setColor(brick.isIndestructible() ? Color.GRAY : Color.CYAN);
                g.fillRect(bx, by, BRICK_W, BRICK_H);
                g.setColor(Color.BLACK);
                g.drawRect(bx, by, BRICK_W, BRICK_H);
            }
        }

        /**
         * Draws the ball as a filled white circle.
         *
         * @param g the graphics context
         * @param s the current game state snapshot
         */
        private void drawBall(final Graphics g, final GameState s) {
            final int r  = (int) s.getBall().getRadius();
            final int bx = (int) (s.getBall().getX() - r);
            final int by = (int) (s.getBall().getY() - r) + HUD_HEIGHT;
            g.setColor(Color.WHITE);
            g.fillOval(bx, by, r * 2, r * 2);
        }

        /**
         * Draws a semi-transparent black overlay with centred "PAUSED" text.
         *
         * @param g the graphics context
         */
        private void drawPauseOverlay(final Graphics g) {
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 44));
            final String text = "PAUSED";
            final FontMetrics fm = g.getFontMetrics();
            final int tx = (getWidth()  - fm.stringWidth(text)) / 2;
            final int ty = (getHeight() + fm.getAscent())        / 2;
            g.drawString(text, tx, ty);
        }
    }
}
