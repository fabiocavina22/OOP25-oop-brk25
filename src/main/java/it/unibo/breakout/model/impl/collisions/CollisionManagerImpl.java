package it.unibo.breakout.model.impl.collisions;

import java.util.List;


import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.api.collisions.CollisionManager;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.BrickImpl;
import it.unibo.breakout.model.impl.PaddleImpl;

public class CollisionManagerImpl implements CollisionManager {

    private final CollisionDetector detector;

    public CollisionManagerImpl(CollisionDetector detector) {

        this.detector = detector;

    }

    @Override
    public void handleCollisions(BallImpl ball, PaddleImpl paddle, List<BrickImpl> bricks){
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks);
        checkBorderCollision(ball);
    }



    private void checkPaddleCollision(BallImpl ball, PaddleImpl paddle){

        if(detector.isColliding(ball, paddle)){ //sostituire il primo paddle con ball

            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + ball.getWidth() / 2.0;

            double offset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
            ball.setVelocityY(-ball.getVelocityY());
            ball.setVelocityX(ball.getVelocityX () * offset);
        }

    }

    private void checkBorderCollision(BallImpl ball) {




    }

    private void checkBrickCollisions(BallImpl ball, List<BrickImpl> bricks) {

        for (BrickImpl brick : bricks) {

        if (detector.isColliding(ball, brick)) {

            // rimbalzo semplice
            ball.setVelocityY(-ball.getVelocityY());

            // distruzione brick
            brick.hit();

            break; // evita multi-collisione nello stesso frame
        }

        }
    }
}
