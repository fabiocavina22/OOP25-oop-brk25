package it.unibo.breakout.model.api.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;

public interface CollisionManager {

    public void handleCollisions(Ball ball, Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score);

    /**Keep the current player point 
    */
    int points(Brick brick);
    
    /**get the player's number of lives 
    */
    int getlives();
    
    /**when you have lost all the lives is game over
    */
    boolean isGameOver();
}

    