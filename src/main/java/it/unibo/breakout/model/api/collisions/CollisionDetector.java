package it.unibo.breakout.model.api.collisions;

/**
 * Rappresentation of the collision detector.
 */
public interface CollisionDetector {

    /**
     * this method check if a collision happens between 2 collidables.
     * @param a ball
     * @param b Collidable
     * @return true if a collision happened or false if it didn't
     */
    boolean isColliding(Collidable a, Collidable b);
}

