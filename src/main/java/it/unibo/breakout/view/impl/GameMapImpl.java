package it.unibo.breakout.view.impl;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

//import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.view.api.GameMap;

public class GameMapImpl extends JFrame implements GameMap{

        @SuppressWarnings("unused")
        private final Paddle paddle;
        @SuppressWarnings("unused")
        private final LevelManager levelManager;
        @SuppressWarnings("unused")
        private final Ball ball;


        public GameMapImpl(Paddle paddle, LevelManager levelManager, Ball ball){

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

            // LEFT 30%
            LeftPanel lp = new LeftPanel();
            lp.setPreferredSize(fluidSize);
            grid.gridx = 0;
            grid.weightx = 0.3;
            this.getContentPane().add(lp, grid);

            // CENTER
            MainPanel mp = new MainPanel(paddle, levelManager, ball);
            mp.setPreferredSize(fluidSize);
            grid.gridx = 1;
            grid.weightx = 0.4;
            this.getContentPane().add(mp, grid);

            RightPanel rp = new RightPanel();
            rp.setPreferredSize(fluidSize);
            grid.gridx = 2;
            grid.weightx = 0.3;
            this.getContentPane().add(rp, grid);

            setSize(1200, 700);
            setLocationRelativeTo(null);
            setResizable(true);

            KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
            this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f11, "toggleFullscreen");
            this.getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {
                boolean isFullScreen = false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Rimuove temporaneamente la finestra per cambiare lo stato

                    if (!isFullScreen) {
                        // Passa a Schermo Intero Assoluto
                        setUndecorated(true);
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                        isFullScreen = true;
                    } else {
                        // Torna alla finestra 1200x700
                        setUndecorated(false);
                        setExtendedState(JFrame.NORMAL);
                        setSize(1200, 700);
                        setLocationRelativeTo(null);
                        isFullScreen = false;
                    }

                    setVisible(true); // Ridisegna la finestra
                }
            });

            mp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Leggiamo la NUOVA dimensione del MainPanel in questo esatto millisecondo
                int newWidth = mp.getWidth();
                int newHeight = mp.getHeight();

                // 1. Aggiorniamo il LevelManager (il metodo che abbiamo creato prima)
                if (levelManager != null) {
                    levelManager.updateDimensions(newWidth, newHeight);
                }

                if (paddle != null) {
                    paddle.updateDimensions(newWidth, newHeight);
                }

                // 3. Aggiorniamo la Palla
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
