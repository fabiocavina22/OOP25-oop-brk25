package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Brick;

public class BrickImpl implements Brick {
    private int life;
    private final boolean indestructible;
    private double x, y;

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
        public void moveDown(double amount) {
            this.y += amount;
        }

        @Override
        public void hit() {
            if (!indestructible) life--;
        }

        @Override
        public boolean isDestroyed() {
            return !indestructible && life <= 0;
        }

        @Override
        public double getX() {
            return this.x;
        }

        @Override
        public double getY() {
            return this.y;
        }

        @Override
        public boolean isIndestructible() {
            return this.indestructible;
        }
}
