package it.unibo.breakout.model.impl.collisions;

import it.unibo.breakout.model.api.collisions.Collidable;
import it.unibo.breakout.model.api.collisions.CollisionDetector;


public class CollisionDetectorImpl implements CollisionDetector{

    @Override
    public boolean isColliding(Collidable a, Collidable b){
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }
}
