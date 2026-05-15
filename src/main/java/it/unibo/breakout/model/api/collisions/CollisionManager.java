package it.unibo.breakout.model.api.collisions;

import java.util.List;

import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.BrickImpl;
import it.unibo.breakout.model.impl.PaddleImpl;

public interface CollisionManager {

    public void handleCollisions(BallImpl ball, PaddleImpl paddle, List<BrickImpl> bricks);

}
