package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.PowerUp;

public class PowerUpImpl implements PowerUp {
    private double x;
    private double y;
    private final int type;
    private static final double FALL_SPEED = 3.0;

    /**
     * Creates a power up capsule at the given position with the given type
     * @param x
     * @param y
     * @param type
     */
    public PowerUpImpl(double x, double y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public double getX(){
        return x;
    }

    @Override
    public double getY(){
        return y;
    }

    @Override
    public int getType(){
        return type;
    }

    @Override
    public boolean isOutOfBounds(double screenHeight){
        return y > screenHeight;
    }

    @Override
    public void fall(){
        this.y += FALL_SPEED;
    }
}
