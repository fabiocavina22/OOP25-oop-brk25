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
    private final List<PowerUpImpl> activePowerUp = new ArrayList<>();
    private int doublePointsFrames;
    private int paddleLargeFrames;
    private int paddleShortFrames;
    private int freezeBlocksFrames;
    private int halfPointsFrames;
    private int fastBallFrames;
    private final LivesManager livesManager;
    private final Random rng = new Random();

    private double scoreMultiplier = 1.0;

    private static final int EFFECT_FRAMES = 500;

    /**
     * Creates a power up manager that uses the given lives manager to grant an extra life when the related power up is collected.
     * @param livesManager
    */
    public PowerUpManagerImpl(final LivesManager livesManager) {
        this.livesManager = livesManager;
    }

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
        spawnPowerUp(x, y, rng.nextInt(7) + 1);
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
                scoreMultiplier = 1.0;
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
                scoreMultiplier = 1.0;
            }
        }
        if (fastBallFrames > 0) {
            fastBallFrames--;
            if (fastBallFrames == 0) {
                ball.setVelocityX(ball.getVelocityX() / 1.5);
                ball.setVelocityY(ball.getVelocityY() / 1.5);
            }
        }
    }

    @Override
    public void updatePowerUp(final Paddle paddle, final Ball ball, final int screenHeight) {
        for (int i = 0; i < activePowerUp.size(); i++) {
            final PowerUpImpl powerUp = activePowerUp.get(i);
            powerUp.fall();
            if (powerUp.isOutOfBounds(screenHeight)) {
                activePowerUp.remove(i);
                i--;
            } else if (powerUp.getX() + 20 > paddle.getX()
            && powerUp.getX() < paddle.getX() + paddle.getWidth()
            && powerUp.getY() + 10 >  paddle.getY()
            && powerUp.getY() < paddle.getY() + paddle.getHeight()) {
                switch (powerUp.getType()) {
                    //extra life
                    case 1:
                        livesManager.addLife();
                        break;
                    //short paddle
                    case 2:
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
                    case 3:
                        if (doublePointsFrames > 0) {
                            doublePointsFrames = EFFECT_FRAMES;
                        } else {
                            if (halfPointsFrames > 0) {
                                halfPointsFrames = 0;
                            }
                            scoreMultiplier = 2.0;
                            doublePointsFrames = EFFECT_FRAMES;
                        }
                        break;

                    //large paddle
                    case 4:
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
                    case 5:
                        freezeBlocksFrames = EFFECT_FRAMES;
                        break;

                    //half points
                    case 6:
                        if (halfPointsFrames > 0) {
                            halfPointsFrames = EFFECT_FRAMES;
                        } else {
                            if (doublePointsFrames > 0) {
                                doublePointsFrames = 0;
                            }
                            scoreMultiplier = 0.5;
                            halfPointsFrames = EFFECT_FRAMES;
                        }
                        break;

                    //fast ball
                    case 7:
                        ball.setVelocityX(ball.getVelocityX() * 1.5);
                        ball.setVelocityY(ball.getVelocityY() * 1.5);
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
