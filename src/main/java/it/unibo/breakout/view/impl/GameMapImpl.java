package it.unibo.breakout.view.impl;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;
import it.unibo.breakout.model.api.Leaderboard;
import it.unibo.breakout.view.api.GameMap;

/**
 * Swing implementation of the game window, organised in three panels
 * (left, main and right) and supporting fullscreen toggling.
 */
public final class GameMapImpl extends JFrame implements GameMap {

    private static final long serialVersionUID = 1L;

    private boolean fullScreen;

    private static final double SIDE_DIMENSION = 0.3;
    private static final double CENTRAL_DIMENSION = 0.4;

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = 700;

  /**
     * Builds the game window and lays out its panels.
     *
     * @param paddle the paddle to display and update on resize
     * @param levelManager the level manager to update on resize
     * @param ball the ball to display and update on resize
     * @param powerUpManager the power up manager that provides the active capsules
     * @param leaderboard the leaderboard shown in the right panel
     */
    public GameMapImpl(final Paddle paddle, final LevelManager levelManager, final Ball ball,
        final PowerUpManager powerUpManager, final Leaderboard leaderboard) {

        setTitle("DiDo's Breakout");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);

        this.getContentPane().setLayout(new GridBagLayout());
        final GridBagConstraints grid = new GridBagConstraints();

        grid.fill = GridBagConstraints.BOTH;
        grid.weighty = 1.0;

        final Dimension fluidSize = new Dimension(0, 0);

        /* Left panel 30% */
        final LeftPanel lp = new LeftPanel();
        lp.setPreferredSize(fluidSize);
        grid.gridx = 0;
        grid.weightx = SIDE_DIMENSION;
        this.getContentPane().add(lp, grid);

        /* Main Panel = 40% */
        final MainPanel mp = new MainPanel(paddle, levelManager, ball, powerUpManager);
        mp.setPreferredSize(fluidSize);
        grid.gridx = 1;
        grid.weightx = CENTRAL_DIMENSION;
        this.getContentPane().add(mp, grid);
        final RightPanel rp = new RightPanel(leaderboard);
        rp.setPreferredSize(fluidSize);
        grid.gridx = 2;
        grid.weightx = SIDE_DIMENSION;
        this.getContentPane().add(rp, grid);

        /* screen dimension */
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        /* screen resize */
        final KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f11, "toggleFullscreen");
        this.getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();

                if (!isFullScreen()) {
                    /* if it's not Full screen goes to full screen */
                    setUndecorated(true);
                    setExtendedState(MAXIMIZED_BOTH);
                    fullScreen = true;
                } else {
                    /* goes back to the window dimension */
                    setUndecorated(false);
                    setExtendedState(NORMAL);
                    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
                    setLocationRelativeTo(null);
                    fullScreen = false;
                    }

                setVisible(true);
            }
        });

        mp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                /*
                * gets the dimension of the main panel in this precise moment
                **/
                final int newWidth = mp.getWidth();
                final int newHeight = mp.getHeight();
                /*
                * updates the level manager, paddle and ball
                **/
                if (levelManager != null) {
                    levelManager.updateDimensions(newWidth, newHeight);
                }

                if (paddle != null) {
                    paddle.updateDimensions(newWidth, newHeight);
                }

                if (ball != null && paddle != null) {
                    ball.updateDimensions(newWidth, newHeight, paddle);
                }
            }
        });
    }

    @Override
    public boolean isFullScreen() {
        return this.fullScreen;
    }


    @Override
    public void showWindow() {
        setVisible(true);
    }

}
