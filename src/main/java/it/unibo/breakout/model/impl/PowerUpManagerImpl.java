package it.unibo.breakout.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LivesManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;

/**
 * Manages the power up capsules.
 */
public final class PowerUpManagerImpl implements PowerUpManager {

    private static final int EFFECT_FRAMES = 500;
    private static final int POWER_UP_TYPES = 7;
    private static final double DOUBLE_MULTIPLIER = 2.0;
    private static final double HALF_MULTIPLIER = 0.5;
    private static final double BASE_MULTIPLIER = 1.0;
    private static final double FAST_BALL_FACTOR = 1.5;
    private static final int CAPSULE_WIDTH = 20;
    private static final int CAPSULE_HEIGHT = 10;
    private static final int TYPE_EXTRA_LIFE = 1;
    private static final int TYPE_SHORT_PADDLE = 2;
    private static final int TYPE_DOUBLE_POINTS = 3;
    private static final int TYPE_LARGE_PADDLE = 4;
    private static final int TYPE_FREEZE = 5;
    private static final int TYPE_HALF_POINTS = 6;
    private static final int TYPE_FAST_BALL = 7;

    private final List<PowerUpImpl> activePowerUp = new ArrayList<>();
    private int doublePointsFrames;
    private int paddleLargeFrames;
    private int paddleShortFrames;
    private int freezeBlocksFrames;
    private int halfPointsFrames;
    private int fastBallFrames;
    private final Random rng = new Random();
    private double scoreMultiplier = BASE_MULTIPLIER;

    @Override
    public double getScoreMultiplier() {
        return this.scoreMultiplier;
    }

    @Override
    public int getFastBallFrames() {
        return this.fastBallFrames;
    }

    @Override
    public long getDoublePointsTimer() {
        return doublePointsFrames;
    }

    @Override
    public long getPaddleLargeTimer() {
        return paddleLargeFrames;
    }

    @Override
    public long getPaddleShortTimer() {
        return paddleShortFrames;
    }

    @Override
    public long getFreezeBlocksTimer() {
        return freezeBlocksFrames;
    }

    @Override
    public long getHalfPointsTimer() {
        return halfPointsFrames;
    }

    @Override
    public long getFastBallTimer() {
        return fastBallFrames;
    }

    @Override
    public boolean isFrozen() {
        return freezeBlocksFrames > 0;
    }

    @Override
    public void resetFastBallFrames() {
        this.fastBallFrames = 0;
    }

    @Override
    public List<PowerUpImpl> getActivePowerUp() {
        return new ArrayList<>(this.activePowerUp);
    }

    @Override
    public void spawnPowerUp(final double x, final double y) {
        spawnPowerUp(x, y, rng.nextInt(POWER_UP_TYPES) + 1);
    }

    /**
     * Spawns a power up capsule of the given type at the given position.
     * @param x
     * @param y
     * @param type
     */
    public void spawnPowerUp(final double x, final double y, final int type) {
        activePowerUp.add(new PowerUpImpl(x, y, type));
    }

    @Override
    public void updateTimer(final Paddle paddle, final Ball ball) {
        if (doublePointsFrames > 0) {
            doublePointsFrames--;
            if (doublePointsFrames == 0) {
                scoreMultiplier = BASE_MULTIPLIER;
            }
        }
        if (paddleShortFrames > 0) {
            paddleShortFrames--;
            if (paddleShortFrames == 0) {
                paddle.paddleLarge();
            }
        }
        if (paddleLargeFrames > 0) {
            paddleLargeFrames--;
            if (paddleLargeFrames == 0) {
                paddle.paddleShort();
            }
        }
        if (freezeBlocksFrames > 0) {
            freezeBlocksFrames--;
        }
        if (halfPointsFrames > 0) {
            halfPointsFrames--;
            if (halfPointsFrames == 0) {
                scoreMultiplier = BASE_MULTIPLIER;
            }
        }
        if (fastBallFrames > 0) {
            fastBallFrames--;
            if (fastBallFrames == 0) {
                ball.setVelocityX(ball.getVelocityX() / FAST_BALL_FACTOR);
                ball.setVelocityY(ball.getVelocityY() / FAST_BALL_FACTOR);
            }
        }
    }

    @Override
    public void updatePowerUp(final Paddle paddle, final Ball ball, final int screenHeight, final LivesManager livesManager) {
        for (int i = 0; i < activePowerUp.size(); i++) {
            final PowerUpImpl powerUp = activePowerUp.get(i);
            powerUp.fall();
            if (powerUp.isOutOfBounds(screenHeight)) {
                activePowerUp.remove(i);
                i--;
            } else if (powerUp.getX() + CAPSULE_WIDTH > paddle.getX()
            && powerUp.getX() < paddle.getX() + paddle.getWidth()
            && powerUp.getY() + CAPSULE_HEIGHT >  paddle.getY()
            && powerUp.getY() < paddle.getY() + paddle.getHeight()) {
                switch (powerUp.getType()) {
                    //extra life
                    case TYPE_EXTRA_LIFE:
                        livesManager.addLife();
                        break;
                    //short paddle
                    case TYPE_SHORT_PADDLE:
                        if (paddleShortFrames > 0) {
                            paddleShortFrames = EFFECT_FRAMES;
                        } else {
                            if (paddleLargeFrames > 0) {
                                paddle.paddleShort();
                                paddleLargeFrames = 0;
                            }
                            paddle.paddleShort();
                            paddleShortFrames = EFFECT_FRAMES;
                        }
                        break;
                    //double points
                    case TYPE_DOUBLE_POINTS:
                        if (doublePointsFrames > 0) {
                            doublePointsFrames = EFFECT_FRAMES;
                        } else {
                            if (halfPointsFrames > 0) {
                                halfPointsFrames = 0;
                            }
                            scoreMultiplier = DOUBLE_MULTIPLIER;
                            doublePointsFrames = EFFECT_FRAMES;
                        }
                        break;

                    //large paddle
                    case TYPE_LARGE_PADDLE:
                        if (paddleLargeFrames > 0) {
                            paddleLargeFrames = EFFECT_FRAMES;
                        } else {
                            if (paddleShortFrames > 0) {
                                paddle.paddleLarge();
                                paddleShortFrames = 0;
                            }
                            paddle.paddleLarge();
                            paddleLargeFrames = EFFECT_FRAMES;
                        }
                        break;

                    //frozen blocks
                    case TYPE_FREEZE:
                        freezeBlocksFrames = EFFECT_FRAMES;
                        break;

                    //half points
                    case TYPE_HALF_POINTS:
                        if (halfPointsFrames > 0) {
                            halfPointsFrames = EFFECT_FRAMES;
                        } else {
                            if (doublePointsFrames > 0) {
                                doublePointsFrames = 0;
                            }
                            scoreMultiplier = HALF_MULTIPLIER;
                            halfPointsFrames = EFFECT_FRAMES;
                        }
                        break;

                    //fast ball
                    case TYPE_FAST_BALL:
                        ball.setVelocityX(ball.getVelocityX() * FAST_BALL_FACTOR);
                        ball.setVelocityY(ball.getVelocityY() * FAST_BALL_FACTOR);
                        fastBallFrames = EFFECT_FRAMES;
                        break;
                    default:
                        break;
                }
                activePowerUp.remove(i);
            }
        }
    }

}
