package it.unibo.breakout.model.api;

public interface Brick {
    double getX();
    double getY();
    boolean isIndestructible();
    void hit();
    boolean isDestroyed();
}