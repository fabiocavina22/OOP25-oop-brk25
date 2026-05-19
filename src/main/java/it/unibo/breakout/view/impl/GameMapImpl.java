package it.unibo.breakout.view.impl;

import javax.swing.JFrame;

//import javax.swing.*;
import java.awt.*;

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

    setLayout(new GridBagLayout());

    GridBagConstraints grid = new GridBagConstraints();
    grid.fill = GridBagConstraints.BOTH;
    grid.gridy = 0;
    grid.weighty = 1.0;

    // LEFT
    grid.gridx = 0;
    grid.weightx = 0.3;
    add(new LeftPanel(), grid);

    // CENTER
    grid.gridx = 1;
    grid.weightx = 0.4;
    add(new MainPanel(paddle, levelManager, ball), grid);

    // RIGHT
    grid.gridx = 2;
    grid.weightx = 0.3;
    add(new RightPanel(), grid);

    setSize(1200, 700);
    setLocationRelativeTo(null);
    setResizable(false);
}







    @Override
    public void showWindow(){
        setVisible(true);
    }

}
