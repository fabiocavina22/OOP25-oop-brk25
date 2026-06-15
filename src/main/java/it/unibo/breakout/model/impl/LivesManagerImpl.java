package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.LivesManager;

/**
 * Implementation of the LivesManager interface to manage player lives.
 */
public final class LivesManagerImpl implements LivesManager {

    private int lives;
    private boolean lifeLost;
    private boolean lifeGained;

    /**
     * Constructor for LivesManagerImpl.
     *
     * @param lives the initial number of lives
     */
    public LivesManagerImpl(final int lives) {
        this.lives = lives;
        this.lifeLost = false;
        this.lifeGained = false;
    }

    @Override
    public int getlives() {
        return this.lives;
    }

    @Override
    public void loseLives() {
        this.lives--;
        this.lifeLost = true;
    }

    @Override
    public boolean isLifeLost() {
        final boolean result = this.lifeLost;
        this.lifeLost = false;
        return result;
    }

    @Override
    public boolean isLifeGained() {
        final boolean result = this.lifeGained;
        this.lifeGained = false;
        return result;
    }

    @Override
    public boolean isGameOver() {
        return this.lives <= 0;
    }

    @Override
    public void addLife() {
        this.lives++;
        this.lifeGained = true;
    }
}
