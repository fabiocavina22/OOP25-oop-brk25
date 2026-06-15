package it.unibo.breakout.model.api;

import it.unibo.breakout.model.api.collisions.Collidable;

/**
 * Read-only view of the ball, exposing only its position and radius.
 */
public interface BallView extends Collidable {

    /**
     * Returns the radius of the ball.
     *
     * @return the radius
     */
    double getRadius();
}
