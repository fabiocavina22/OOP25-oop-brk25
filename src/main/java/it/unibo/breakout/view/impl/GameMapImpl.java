package it.unibo.breakout.view.impl;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.LeaderboardImpl;
import it.unibo.breakout.view.api.GameMap;

public class GameMapImpl extends JFrame implements GameMap{

        @SuppressWarnings("unused")
        private final Paddle paddle;
        @SuppressWarnings("unused")
        private final LevelManager levelManager;
        @SuppressWarnings("unused")
        private final Ball ball;


        public GameMapImpl(Paddle paddle, LevelManager levelManager, Ball ball, LeaderboardImpl leaderboard){

            this.paddle = paddle;
            this.levelManager = levelManager;
            this.ball = ball;

            setTitle("DiDo's Breakout");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);

            this.getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints grid = new GridBagConstraints();

            grid.fill = GridBagConstraints.BOTH;
            grid.weighty = 1.0;

            Dimension fluidSize = new Dimension(0, 0);

            /* Left panel 30% */
            LeftPanel lp = new LeftPanel();
            lp.setPreferredSize(fluidSize);
            grid.gridx = 0;
            grid.weightx = 0.3;
            this.getContentPane().add(lp, grid);

            /* Main Panel = 40% */
            MainPanel mp = new MainPanel(paddle, levelManager, ball);
            mp.setPreferredSize(fluidSize);
            grid.gridx = 1;
            grid.weightx = 0.4;
            this.getContentPane().add(mp, grid);

            RightPanel rp = new RightPanel(leaderboard);
            rp.setPreferredSize(fluidSize);
            grid.gridx = 2;
            grid.weightx = 0.3;
            this.getContentPane().add(rp, grid);

            /* screen dimension */
            setSize(1200, 700);
            setLocationRelativeTo(null);
            setResizable(true);

            /* screen resize */
            KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
            this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f11, "toggleFullscreen");
            this.getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {

                boolean isFullScreen = false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();

                    if (!isFullScreen) {
                        /* if it's not Full screen goes to full screen */
                        setUndecorated(true);
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                        isFullScreen = true;
                    } else {

                        /*
                        * goes back to the window dimension
                        **/

                        setUndecorated(false);
                        setExtendedState(JFrame.NORMAL);
                        setSize(1200, 700);
                        setLocationRelativeTo(null);
                        isFullScreen = false;
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
                int newWidth = mp.getWidth();
                int newHeight = mp.getHeight();

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
    public void showWindow(){
        setVisible(true);
    }

}
