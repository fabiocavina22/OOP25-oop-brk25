package it.unibo.breakout.model.impl.collisions;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;
import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.api.collisions.CollisionManager;

/**
 * Implementation of the CollisionManager interface to handle in-game collisions.
 */
public final class CollisionManagerImpl implements CollisionManager {

    private final CollisionDetector detector;
    private int score;

    private final PowerUpManager powerUpManager;

    private int blockHit;
    private boolean padHit;
    private boolean borderHit;

    private static final int EXPLOSIVE_BLOCK = 5;
    private static final int LUCKY_BLOCK = 4;
    private static final int DESTROYED_BRICK_POINTS = 300;
    private static final int HIT_BRICK_POINTS = 150;
    private static final double RESUME_SPEED_DIVIDER = 1.5;
    private static final double ADJACENCY_FACTOR = 1.5;

    /**
     * Constructs a new CollisionManagerImpl.
     *
     * @param detector       the collision detector utility
     * @param score          the initial game score
     * @param powerUpManager the manager handling power-up mechanics
     */
    public CollisionManagerImpl(final CollisionDetector detector, final int score, final PowerUpManager powerUpManager) {
        this.detector = detector;
        this.score = score;
        this.powerUpManager = powerUpManager;
    }

    @Override
    public void handleCollisions(final Ball ball, final Paddle paddle, final List<Brick> bricks,
    final int gameWidth, final int gameHeight, final int score) {
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks);
        checkBorderCollision(ball, gameWidth);
    }

    @Override
    public int points(final Brick brick) {
        if (brick.isIndestructible()) {
            return score;
        }
        if (brick.isDestroyed()) {
            score += (int) (DESTROYED_BRICK_POINTS * powerUpManager.getScoreMultiplier());
        } else {
            score += (int) (HIT_BRICK_POINTS * powerUpManager.getScoreMultiplier());
        }
        return score;
    }

    @Override
    public int getScore() {
        return score;
    }


    @Override
    public void blockHit(final Brick bricks) {
        this.blockHit = bricks.getType();
    }

    @Override
    public void padHit() {
        this.padHit = true;
    }

    @Override
    public void borderHit() {
        this.borderHit = true;
    }

    @Override
    public int typeOfBlockHit() {
        final int result = this.blockHit;
        this.blockHit = 0;
        return result;
    }

    @Override
    public boolean isPadHit() {
        final boolean result = this.padHit;
        this.padHit = false;
        return result;
    }

    @Override
    public boolean isBorderHit() {
        final boolean result = this.borderHit;
        this.borderHit = false;
        return result;
    }

    /**
     * Checks if a collision happens between the ball and the paddle.
     *
     * @param ball
     * @param paddle
     */
    private void checkPaddleCollision(final Ball ball, final Paddle paddle) {

        if (detector.isColliding(ball, paddle)) {

            final double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            final double ballCenter = ball.getX() + ball.getWidth() / 2.0;

            final double offset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            /**
             * total current speed
             */
            final double speed = Math.sqrt(
                ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY()
            );

            /**
             * new direction
             */
            final double maxBounceAngle = Math.toRadians(60);

            /**
             * final angle
             */
            final double bounceAngle = offset * maxBounceAngle;

            /**
             * new speed
             */
            final double newVelocityX = speed * Math.sin(bounceAngle);
            final double newVelocityY = -speed * Math.cos(bounceAngle);

            padHit();

            ball.setVelocityX(newVelocityX);
            ball.setVelocityY(newVelocityY);
        }

    }
    /**
     * Checks if the ball hit the borders or goes under the paddle.
     * manages the logic of the life loss.
     * @param ball
     * @param gameWidth
     */
    private void checkBorderCollision(final Ball ball, final int gameWidth) {

        if (ball.getX() <= 0) {
            borderHit();
            ball.setPosition(0, ball.getY());
            ball.setVelocityX(Math.abs(ball.getVelocityX()));
        } else if (ball.getX() + ball.getWidth() >= gameWidth) {
            borderHit();
            ball.setPosition(gameWidth - ball.getWidth(), ball.getY());
            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
        }

        if (ball.getY() <= 0) {
            borderHit();
            ball.setPosition(ball.getX(), 0);
            ball.setVelocityY(Math.abs(ball.getVelocityY()));
        }
    }

    @Override
    public boolean hasBallWentUnder(final Ball ball, final Paddle paddle) {
        return ball.getY() > paddle.getY() + paddle.getHeight() + ball.getHeight();

    }

    @Override
    public void resume(final Ball ball, final int gameWidth, final Paddle paddle) {

            ball.setVelocityX(ball.getVelocityX() / RESUME_SPEED_DIVIDER);
            ball.setVelocityY(ball.getVelocityY() / RESUME_SPEED_DIVIDER);
            powerUpManager.resetFastBallFrames();

            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            ball.setVelocityX(0);
            ball.setVelocityY(0);

    }

    private boolean isAdjacent(final Brick brick, final Brick adjacentBrick) {
        final double dx = Math.abs(brick.getX() - adjacentBrick.getX());
        final double dy = Math.abs(brick.getY() - adjacentBrick.getY());
        final boolean adjacentX = dx < brick.getWidth() * ADJACENCY_FACTOR;
        final boolean adjacentY = dy < brick.getHeight() * ADJACENCY_FACTOR;
        final boolean notSame = dx > 0 || dy > 0;
        return adjacentX && adjacentY && notSame;
    }

    /**
     *Checks if a collision happens between any block and the ball.
     *Manages the brick destruction, power ups releas.
     *
     * @param ball
     * @param bricks
     */
    private void checkBrickCollisions(final Ball ball, final List<Brick> bricks) {

        for (final Brick brick : bricks) {

            if (detector.isColliding(ball, brick)) {

                /**
                * gets the ball's position before it hit the block
                */
                final double prevBallX = ball.getX() - ball.getVelocityX();
                final double prevBallY = ball.getY() - ball.getVelocityY();

                /**
                * checks where the ball is coming from
                */
                final boolean comingFromLeft = prevBallX + ball.getWidth() <= brick.getX();
                final boolean comingFromRight   = prevBallX >= brick.getX() + brick.getWidth();
                final boolean comingFromAbove   = prevBallY + ball.getHeight() <= brick.getY();
                final boolean comingFromBottom   = prevBallY >= brick.getY() + brick.getHeight();

                /*
                * calculate the overlap in order to avoid bugs with the collision
                */
                final double overlapLeft   = ball.getX() + ball.getWidth() - brick.getX();
                final double overlapRight  = brick.getX() + brick.getWidth() - ball.getX();
                final double overlapTop    = ball.getY() + ball.getHeight() - brick.getY();
                final double overlapBottom = brick.getY() + brick.getHeight() - ball.getY();

                /*
                * changes the ball's new directrion based on its previous position
                */
                if (comingFromLeft) {
                    ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                    ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                } else if (comingFromRight) {
                    ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                    ball.setVelocityX(Math.abs(ball.getVelocityX()));
                } else if (comingFromAbove) {
                    ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                    ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                } else if (comingFromBottom) {
                    ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                    ball.setVelocityY(Math.abs(ball.getVelocityY()));
                } else {
                    /*
                    * avoid the possibility that the ballo gets stuck in a perfect angle
                    */
                    final double minOverlapX = Math.min(overlapLeft, overlapRight);
                    final double minOverlapY = Math.min(overlapTop, overlapBottom);

                    if (minOverlapX < minOverlapY) {
                        if (overlapLeft < overlapRight) {
                            ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                        } else {
                            ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                            ball.setVelocityX(Math.abs(ball.getVelocityX()));
                        }
                    } else {
                        if (overlapTop < overlapBottom) {
                            ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                            ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                        } else {
                            ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                            ball.setVelocityY(Math.abs(ball.getVelocityY()));
                        }
                    }
                }

                /*brick's destruction*/
                brick.hit();
                points(brick);

                /* bomb brick */
                if (brick.getType() == EXPLOSIVE_BLOCK) {
                    for (final Brick adjacentBrick : bricks) {
                        if (!brick.equals(adjacentBrick) && isAdjacent(brick, adjacentBrick)) {
                            adjacentBrick.hit();
                            points(adjacentBrick);
                            if (adjacentBrick.isDestroyed() && adjacentBrick.getType() == 4) {
                                powerUpManager.spawnPowerUp(
                                    adjacentBrick.getX() + adjacentBrick.getWidth() / 2.0,
                                    adjacentBrick.getY()
                                );
                            }
                        }
                    }
                    }

                    /* power up block */
                    if (brick.isDestroyed() && brick.getType() == LUCKY_BLOCK) {
                        powerUpManager.spawnPowerUp(brick.getX() + brick.getWidth() / 2.0, brick.getY());
                    }
                blockHit(brick);


                break; /* avoid multiple collisions in the same frame */
            }
        }
    }
}
