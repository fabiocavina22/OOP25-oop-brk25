package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.LivesManager;

public class LivesManagerImpl implements LivesManager {

    private int lives;
    private boolean lifeLost;
    private boolean lifeGained;

    public LivesManagerImpl(int lives){

        this.lives = lives;
        this.lifeLost = false;
        this.lifeGained = false;

    }

        @Override
    public int getlives(){
        return lives;
    }

    @Override
    public void loseLives(){
        lives--;
        lifeLost = true;
    }

    @Override
    public boolean isLifeLost(){
        boolean result = this.lifeLost;
        lifeLost = false;
        return result;
    }

    @Override
    public boolean isLifeGained(){
        boolean result = this.lifeGained;
        lifeGained = false;
        return result;
    }

    @Override
    public boolean isGameOver(){
        if(lives <= 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void addLife(){
        this.lives ++;
        this.lifeGained = true;
    }
}
