package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Brick;

public class BrickImpl implements Brick {
    private int life;
    private final boolean indestructible;
    private final double x, y;

    public BrickImpl(double x, double y, int type) {
            this.x = x;
            this.y = y;
            if (type == 3) { // 3 = indistruttibile
                this.indestructible = true;
                this.life = 1;
            } else {
                this.indestructible = false;
                this.life = type; // vita = 1 o 2 colpi
            }
        }

        @Override
        public void hit() {
            if (!indestructible) life--;
        }

        @Override
        public boolean isDestroyed() {
            return !indestructible && life <= 0;
        }

    // Funzioni per Riccardo
        public double getX() { return x; }
        public double getY() { return y; }


    // Funzioni per Fabio
        public int getLife() { return life; }
        public boolean isIndestructible() { return indestructible; }
    }
