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
import it.unibo.breakout.model.impl.LeaderboardImpl;
import it.unibo.breakout.view.api.GameMap;

public final class GameMapImpl extends JFrame implements GameMap{

    @SuppressWarnings("unused")
    private final Paddle paddle;
    @SuppressWarnings("unused")
    private final LevelManager levelManager;
    @SuppressWarnings("unused")
    private final Ball ball;

    private boolean fullScreen;

    private final double SIDEDIMENSION = 0.3;
    private final double CENTRALDIMENSION = 0.4;

    private final int SCREENWIDTH = 1200;
    private final int SCREENHEIGHT = 700;

    public GameMapImpl(final Paddle paddle, final LevelManager levelManager, final Ball ball, final LeaderboardImpl leaderboard) {

        this.paddle = paddle;
        this.levelManager = levelManager;
        this.ball = ball;


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
        grid.weightx = SIDEDIMENSION;
        this.getContentPane().add(lp, grid);

        /* Main Panel = 40% */
        final MainPanel mp = new MainPanel(paddle, levelManager, ball);
        mp.setPreferredSize(fluidSize);
        grid.gridx = 1;
        grid.weightx = CENTRALDIMENSION;
        this.getContentPane().add(mp, grid);
        final RightPanel rp = new RightPanel(leaderboard);
        rp.setPreferredSize(fluidSize);
        grid.gridx = 2;
        grid.weightx = SIDEDIMENSION;
        this.getContentPane().add(rp, grid);

        /* screen dimension */
        setSize(SCREENWIDTH, SCREENHEIGHT);
        setLocationRelativeTo(null);
        setResizable(true);

        /* screen resize */
        final KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f11, "toggleFullscreen");
        this.getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                if (!isFullScreen()) {
                    /* if it's not Full screen goes to full screen */
                    setUndecorated(true);
                    setExtendedState(MAXIMIZED_BOTH);
                    fullScreen = true;
                } else {
                    /**
                    * goes back to the window dimension
                    **/
                    setUndecorated(false);
                    setExtendedState(NORMAL);
                    setSize(1200, 700);
                    setLocationRelativeTo(null);
                    fullScreen = false;
                    }

                setVisible(true);
            }
        });

        mp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
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
    public boolean isFullScreen(){
        return this.fullScreen;
    }


    @Override
    public void showWindow(){
        setVisible(true);
    }

}
