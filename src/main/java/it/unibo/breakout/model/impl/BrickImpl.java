package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.collisions.Collidable;

public class BrickImpl implements Brick, Collidable {
    private int life;
    private final boolean indestructible;
    private double x, y;
    private double width;
    private double height;

    public BrickImpl(double x, double y, int type, double width, double height) {
            this.x = x;
            this.y = y;
            if (type == 3) { // 3 = indistruttibile
                this.indestructible = true;
                this.life = 1;
            } else {
                this.indestructible = false;
                this.life = type; // vita = 1 o 2 colpi
            }
            this.width = width;
            this.height = height;
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

        @Override
        public int getWidth() {
            return (int) this.width;
        }
        @Override
        public int getHeight() {
             return (int) this.height;
        }
}
