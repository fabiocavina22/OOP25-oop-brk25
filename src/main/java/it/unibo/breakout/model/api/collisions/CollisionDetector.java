package it.unibo.breakout.model.api.collisions;

public interface CollisionDetector {

    /**
     * this method check if a collision happens between 2 collidables.
     * @param ball
     * @param Collidable
     */
    boolean isColliding(Collidable a, Collidable b);
}

