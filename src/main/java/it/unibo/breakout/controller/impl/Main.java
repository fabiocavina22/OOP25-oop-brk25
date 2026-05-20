package it.unibo.breakout.controller.impl;

import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LevelManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.view.impl.GameMapImpl;

public class Main {

    public static void main(String[] args) {
        // 1. Impostazioni di base del pannello di gioco (pannello centrale)
        int gameWidth = 600; // Larghezza del pannello di gioco
        int gameHeight = 700; // Altezza del pannello di gioco
        int score = 0;
        // 2. Creazione del Model
        // (x, y, width, height, speed)
        PaddleImpl paddle = new PaddleImpl(250, 600, 100, 15, 8);

        // (x, y, radius, velocityX, velocityY)
        BallImpl ball = new BallImpl(300, 500, 8, 4.0, -4.0);

        // (screenWidth, brickWidth, brickHeight, screenHeight)
        LevelManagerImpl levelManager = new LevelManagerImpl(gameWidth, 60, 60, gameHeight);

        // 3. Creazione della View
        GameMapImpl view = new GameMapImpl(paddle, levelManager, ball);
        view.showWindow();

        // 4. Creazione del Controller e Avvio
        GameController controller = new GameController(paddle, ball, levelManager, view, gameWidth, gameHeight, score);
        controller.start();
    }
}