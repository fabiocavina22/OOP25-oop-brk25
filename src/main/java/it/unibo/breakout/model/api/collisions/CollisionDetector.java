package it.unibo.breakout.model.api.collisions;

public interface CollisionDetector {
    public boolean isColliding(Collidable a, Collidable b);
}
