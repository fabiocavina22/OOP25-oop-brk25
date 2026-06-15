package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.LivesManagerImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.model.impl.PowerUpImpl;
import it.unibo.breakout.model.impl.PowerUpManagerImpl;

/**
 * Tests for the power up capsules and the power up manager.
 */
class TestPowerUp {

    private static final double DELTA = 1e-9;
    private static final int EFFECT_FRAMES = 500;
    private static final int SCREEN_HEIGHT = 600;

    private static final int PADDLE_X = 100;
    private static final int PADDLE_Y = 300;
    private static final int PADDLE_WIDTH = 80;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_SPEED = 12;

    private static final double BALL_SIZE = 5;
    private static final double BALL_VX = 4;
    private static final double BALL_VY = 8;
    private static final double FAST_BALL_VX = 6;
    private static final double FAST_BALL_VY = 12;

    private static final double CAPSULE_X = 130;

    private static final double BASE_MULTIPLIER = 1.0;
    private static final double DOUBLE_MULTIPLIER = 2.0;
    private static final double HALF_MULTIPLIER = 0.5;

    private static final int INITIAL_LIVES = 3;
    private static final int LIVES_AFTER_BONUS = 4;

    private static final int TYPE_EXTRA_LIFE = 1;
    private static final int TYPE_SHORT_PADDLE = 2;
    private static final int TYPE_DOUBLE_POINTS = 3;
    private static final int TYPE_LARGE_PADDLE = 4;
    private static final int TYPE_FREEZE = 5;
    private static final int TYPE_HALF_POINTS = 6;
    private static final int TYPE_FAST_BALL = 7;

    private static final double POS_X = 10;
    private static final double POS_Y = 20;
    private static final double FALL_SPEED = 3.0;
    private static final int TEST_BOUND = 100;
    private static final double Y_ABOVE_BOUND = 101;
    private static final double Y_BELOW_BOUND = 50;
    private static final double CAPSULE_NEAR_BOTTOM_Y = 598;
    private static final double FAR_X = 400;

    private PowerUpManagerImpl manager;
    private LivesManagerImpl lives;

    @BeforeEach
    void setUp() {
        lives = new LivesManagerImpl(INITIAL_LIVES);
        manager = new PowerUpManagerImpl();
    }

    private void collect(final int type, final Paddle paddle, final Ball ball) {
        manager.spawnPowerUp(CAPSULE_X, PADDLE_Y, type);
        manager.updatePowerUp(paddle, ball, SCREEN_HEIGHT, lives);
    }

    private Paddle newPaddle() {
        return new PaddleImpl(PADDLE_X, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED);
    }

    private Ball newBall() {
        return new BallImpl(0, 0, BALL_SIZE, BALL_VX, BALL_VY);
    }

    // --- getType() ---

    @Test
    void testGetType() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, POS_Y, TYPE_DOUBLE_POINTS);
        assertEquals(TYPE_DOUBLE_POINTS, p.getType());
    }

    // --- getX() and getY() ---
    @Test
    void testGetPosition() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, POS_Y, TYPE_EXTRA_LIFE);
        assertEquals(POS_X, p.getX(), DELTA);
        assertEquals(POS_Y, p.getY(), DELTA);
    }

    // --- fall() ---
    @Test
    void testFallIncreasesY() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, POS_Y, TYPE_EXTRA_LIFE);
        p.fall();
        assertEquals(POS_Y + FALL_SPEED, p.getY(), DELTA);
    }

    @Test
    void testFallAccumulates() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, POS_Y, TYPE_EXTRA_LIFE);
        p.fall();
        p.fall();
        p.fall();
        assertEquals(POS_Y + FALL_SPEED + FALL_SPEED + FALL_SPEED, p.getY(), DELTA);
    }

    @Test
    void testFallDoesNotChangeX() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, POS_Y, TYPE_EXTRA_LIFE);
        p.fall();
        assertEquals(POS_X, p.getX(), DELTA);
    }

    // --- isOutOfBounds() ---
    @Test
    void testIsOutOfBoundsTrue() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, Y_ABOVE_BOUND, TYPE_EXTRA_LIFE);
        assertTrue(p.isOutOfBounds(TEST_BOUND));
    }

    @Test
    void testIsOutOfBoundsFalse() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, Y_BELOW_BOUND, TYPE_EXTRA_LIFE);
        assertFalse(p.isOutOfBounds(TEST_BOUND));
    }

    @Test
    void testIsOutOfBoundsExactBoundary() {
        final PowerUpImpl p = new PowerUpImpl(POS_X, TEST_BOUND, TYPE_EXTRA_LIFE);
        assertFalse(p.isOutOfBounds(TEST_BOUND));
    }

    // -------------------------------------------------------------------------
    // PowerUpManagerImpl - start game
    // -------------------------------------------------------------------------

    @Test
    void testInitiallyNotFrozen() {
        assertFalse(manager.isFrozen());
    }

    @Test
    void testInitialScoreMultiplier() {
        assertEquals(BASE_MULTIPLIER, manager.getScoreMultiplier(), DELTA);
    }

    @Test
    void testInitialPowerUpListEmpty() {
        assertTrue(manager.getActivePowerUp().isEmpty());
    }

    @Test
    void testSpawnPowerUpAddsCapsule() {
        manager.spawnPowerUp(PADDLE_X, PADDLE_Y);
        assertEquals(1, manager.getActivePowerUp().size());
    }

    @Test
    void testSpawnPowerUpValidType() {
        manager.spawnPowerUp(PADDLE_X, PADDLE_Y);
        final int type = manager.getActivePowerUp().get(0).getType();
        assertTrue(type >= TYPE_EXTRA_LIFE && type <= TYPE_FAST_BALL, "Il tipo deve essere tra 1 e 7");
    }

    // -------------------------------------------------------------------------
    // effects taken
    // -------------------------------------------------------------------------

    @Test
    void testCollectShortPaddle() {
        collect(TYPE_SHORT_PADDLE, newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES, manager.getPaddleShortTimer());
    }

    @Test
    void testCollectDoublePoints() {
        collect(TYPE_DOUBLE_POINTS, newPaddle(), newBall());
        assertEquals(DOUBLE_MULTIPLIER, manager.getScoreMultiplier(), DELTA);
        assertEquals(EFFECT_FRAMES, manager.getDoublePointsTimer());
    }

    @Test
    void testCollectEnlargePaddle() {
        collect(TYPE_LARGE_PADDLE, newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES, manager.getPaddleLargeTimer());
    }

    @Test
    void testCollectFreezeBlocks() {
        collect(TYPE_FREEZE, newPaddle(), newBall());
        assertTrue(manager.isFrozen());
        assertEquals(EFFECT_FRAMES, manager.getFreezeBlocksTimer());
    }

    @Test
    void testCollectHalfPoints() {
        collect(TYPE_HALF_POINTS, newPaddle(), newBall());
        assertEquals(HALF_MULTIPLIER, manager.getScoreMultiplier(), DELTA);
        assertEquals(EFFECT_FRAMES, manager.getHalfPointsTimer());
    }

    @Test
    void testCollectFastBall() {
        final Ball ball = newBall();
        collect(TYPE_FAST_BALL, newPaddle(), ball);
        assertEquals(EFFECT_FRAMES, manager.getFastBallTimer());
        assertEquals(FAST_BALL_VX, ball.getVelocityX(), DELTA);
        assertEquals(FAST_BALL_VY, ball.getVelocityY(), DELTA);
    }

    @Test
    void testCollectExtraLife() {
        collect(TYPE_EXTRA_LIFE, newPaddle(), newBall());
        assertEquals(LIVES_AFTER_BONUS, lives.getlives());
    }

    @Test
    void testCollectExtraLifeSetsGainedFlag() {
        collect(TYPE_EXTRA_LIFE, newPaddle(), newBall());
        assertTrue(lives.isLifeGained());
    }

    // -------------------------------------------------------------------------
    // opposite effects and repeated effects
    // -------------------------------------------------------------------------

    @Test
    void testEnlargeCancelsShort() {
        final Paddle paddle = newPaddle();
        final Ball ball = newBall();
        collect(TYPE_SHORT_PADDLE, paddle, ball);
        collect(TYPE_LARGE_PADDLE, paddle, ball);
        assertEquals(0, manager.getPaddleShortTimer());
        assertEquals(EFFECT_FRAMES, manager.getPaddleLargeTimer());
    }

    @Test
    void testHalfCancelsDouble() {
        final Paddle paddle = newPaddle();
        final Ball ball = newBall();
        collect(TYPE_DOUBLE_POINTS, paddle, ball);
        collect(TYPE_HALF_POINTS, paddle, ball);
        assertEquals(0, manager.getDoublePointsTimer());
        assertEquals(HALF_MULTIPLIER, manager.getScoreMultiplier(), DELTA);
    }

    @Test
    void testSameEffectResetsTimer() {
        final Paddle paddle = newPaddle();
        final Ball ball = newBall();
        collect(TYPE_SHORT_PADDLE, paddle, ball);
        manager.updateTimer(paddle, ball);
        collect(TYPE_SHORT_PADDLE, paddle, ball);
        assertEquals(EFFECT_FRAMES, manager.getPaddleShortTimer());
    }

    // -------------------------------------------------------------------------
    // timers and power up
    // -------------------------------------------------------------------------

    @Test
    void testUpdateTimerDecreasesFrames() {
        collect(TYPE_FREEZE, newPaddle(), newBall());
        manager.updateTimer(newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES - 1, manager.getFreezeBlocksTimer());
    }

    @Test
    void testDoublePointsExpiresResetsMultiplier() {
        final Paddle paddle = newPaddle();
        final Ball ball = newBall();
        collect(TYPE_DOUBLE_POINTS, paddle, ball);
        for (int i = 0; i < EFFECT_FRAMES; i++) {
            manager.updateTimer(paddle, ball);
        }
        assertEquals(BASE_MULTIPLIER, manager.getScoreMultiplier(), DELTA);
        assertEquals(0, manager.getDoublePointsTimer());
    }

    @Test
    void testFastBallExpiresRestoresSpeed() {
        final Paddle paddle = newPaddle();
        final Ball ball = newBall();
        collect(TYPE_FAST_BALL, paddle, ball);
        for (int i = 0; i < EFFECT_FRAMES; i++) {
            manager.updateTimer(paddle, ball);
        }
        assertEquals(BALL_VX, ball.getVelocityX(), DELTA);
        assertEquals(BALL_VY, ball.getVelocityY(), DELTA);
        assertEquals(0, manager.getFastBallTimer());
    }

    @Test
    void testCapsuleOutOfBoundsIsRemoved() {
        manager.spawnPowerUp(CAPSULE_X, CAPSULE_NEAR_BOTTOM_Y, TYPE_DOUBLE_POINTS);
        manager.updatePowerUp(newPaddle(), newBall(), SCREEN_HEIGHT, lives);
        assertTrue(manager.getActivePowerUp().isEmpty());
    }

    @Test
    void testCapsuleNotCollectedKeepsFalling() {
        manager.spawnPowerUp(FAR_X, PADDLE_Y, TYPE_DOUBLE_POINTS);
        manager.updatePowerUp(newPaddle(), newBall(), SCREEN_HEIGHT, lives);
        final List<PowerUpImpl> list = manager.getActivePowerUp();
        assertEquals(1, list.size());
        assertEquals(PADDLE_Y + FALL_SPEED, list.get(0).getY(), DELTA);
    }

    @Test
    void testResetFastBallFrames() {
        collect(TYPE_FAST_BALL, newPaddle(), newBall());
        manager.resetFastBallFrames();
        assertEquals(0, manager.getFastBallTimer());
    }
}
