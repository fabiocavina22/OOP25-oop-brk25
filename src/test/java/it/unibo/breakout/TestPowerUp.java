package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.LivesManagerImpl;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.model.impl.PowerUpImpl;
import it.unibo.breakout.model.impl.PowerUpManagerImpl;

public class TestPowerUp {
    private static final double DELTA = 1e-9;
    private static final int EFFECT_FRAMES = 500;
    private static final int SCREEN_HEIGHT = 600;

    private PowerUpManagerImpl manager;

    @BeforeEach
    void setup(){
        manager = new PowerUpManagerImpl();
    }

    private void injectPowerUp(PowerUpImpl p) throws Exception {
        Field field = PowerUpManagerImpl.class.getDeclaredField("activePowerUp");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<PowerUpImpl> list = (List<PowerUpImpl>) field.get(manager);
        list.add(p);
    }

    private void collect(int type, Paddle paddle, Ball ball) throws Exception {
        injectPowerUp(new PowerUpImpl(130, 300, type));
        manager.updatePowerUp(paddle, ball, SCREEN_HEIGHT);
    }

    private Paddle newPaddle() {
        return new PaddleImpl(100, 300, 80, 15, 12);
    }

    private Ball newBall(){
        return new BallImpl(0, 0, 5, 4, 8);
    }
    
    // ---getType() ---

     @Test
    public void testGetType() {
        PowerUpImpl p = new PowerUpImpl(10, 20, 3);
        assertEquals(3, p.getType());
    }

    // ---getX() and getY()---
     @Test
    public void testGetPosition() {
        PowerUpImpl p = new PowerUpImpl(10, 20, 1);
        assertEquals(10, p.getX(), DELTA);
        assertEquals(20, p.getY(), DELTA);
    }

    // ---fall()---
    @Test
    public void testFallIncreasesY() {
        PowerUpImpl p = new PowerUpImpl(10, 20, 1);
        p.fall();
        assertEquals(23, p.getY(), DELTA);
    }

    @Test
    public void testFallAccumulates() {
        PowerUpImpl p = new PowerUpImpl(10, 20, 1);
        p.fall();
        p.fall();
        p.fall();
        assertEquals(29, p.getY(), DELTA);
    }

    @Test
    public void testFallDoesNotChangeX() {
        PowerUpImpl p = new PowerUpImpl(10, 20, 1);
        p.fall();
        assertEquals(10, p.getX(), DELTA);
    }

    // ---isOutOfBounds()---
    @Test
    public void testIsOutOfBoundsTrue() {
        PowerUpImpl p = new PowerUpImpl(10, 101, 1);
        assertTrue(p.isOutOfBounds(100));
    }

    @Test
    public void testIsOutOfBoundsFalse() {
        PowerUpImpl p = new PowerUpImpl(10, 50, 1);
        assertFalse(p.isOutOfBounds(100));
    }

    @Test
    public void testIsOutOfBoundsExactBoundary() {
        PowerUpImpl p = new PowerUpImpl(10, 100, 1);
        assertFalse(p.isOutOfBounds(100));
    }

    // -------------------------------------------------------------------------
    // PowerUpManagerImpl - start game
    // -------------------------------------------------------------------------

    // ---no frozen blocks---
    @Test
    public void testInitiallyNotFrozen() {
        assertFalse(manager.isFrozen());
    }

    // ---the score umltiplier must be 1.0---
    @Test
    public void testInitialScoreMultiplier() {
        assertEquals(1.0, manager.getScoreMultiplier(), DELTA);
    }

    // ---no active power up--
    @Test
    public void testInitialPowerUpListEmpty() {
        assertTrue(manager.getActivePowerUp().isEmpty());
    }

    // ---verify the spawn of the power up---
    @Test
    public void testSpawnPowerUpAddsCapsule() {
        manager.spawnPowerUp(100, 50);
        assertEquals(1, manager.getActivePowerUp().size());
    }

    // ---the power up must be chosen from 1 to 7--- 
    @Test
    public void testSpawnPowerUpValidType() {
        manager.spawnPowerUp(100, 50);
        int type = manager.getActivePowerUp().get(0).getType();
        assertTrue(type >= 1 && type <= 7, "Il tipo deve essere tra 1 e 7");
    }

    // -------------------------------------------------------------------------
    // effects taken
    // -------------------------------------------------------------------------

    // ---paddleShort must be number 2---
    @Test
    public void testCollectShortPaddle() throws Exception {
        collect(2, newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES, manager.getPaddleShortTimer());
    }

    // ---2.0 multiplier must be number 3---
    @Test
    public void testCollectDoublePoints() throws Exception {
        collect(3, newPaddle(), newBall());
        assertEquals(2.0, manager.getScoreMultiplier(), DELTA);
        assertEquals(EFFECT_FRAMES, manager.getDoublePointsTimer());
    }

    // ---paddleLarge must be number 4---
    @Test
    public void testCollectEnlargePaddle() throws Exception {
        collect(4, newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES, manager.getPaddleLargeTimer());
    }

    // ---frozenblocks must be number 5---
    @Test
    public void testCollectFreezeBlocks() throws Exception {
        collect(5, newPaddle(), newBall());
        assertTrue(manager.isFrozen());
        assertEquals(EFFECT_FRAMES, manager.getFreezeBlocksTimer());
    }

    // ---0.5 multiplier must be number 6---
    @Test
    public void testCollectHalfPoints() throws Exception {
        collect(6, newPaddle(), newBall());
        assertEquals(0.5, manager.getScoreMultiplier(), DELTA);
        assertEquals(EFFECT_FRAMES, manager.getHalfPointsTimer());
    }

    //---fastBall must be number 7---
    @Test
    public void testCollectFastBall() throws Exception {
        Ball ball = newBall();
        collect(7, newPaddle(), ball);
        assertEquals(EFFECT_FRAMES, manager.getFastBallTimer());
        assertEquals(6, ball.getVelocityX(), DELTA);
        assertEquals(12, ball.getVelocityY(), DELTA);
    }

    //---extra life must be number 1---
    @Test
    public void testCollectExtraLife() throws Exception {
        LivesManagerImpl lives = new LivesManagerImpl(3);
        manager.setLivesManager(lives);
        collect(1, newPaddle(), newBall());
        assertEquals(4, lives.getlives());
    }

    @Test
    public void testCollectExtraLifeSetsGainedFlag() throws Exception {
        LivesManagerImpl lives = new LivesManagerImpl(3);
        manager.setLivesManager(lives);
        collect(1, newPaddle(), newBall());
        assertTrue(lives.isLifeGained());
    }

    // -------------------------------------------------------------------------
    // opposite effects and repeated effects
    // -------------------------------------------------------------------------

    // ---cancelletion of paddleShort---
    @Test
    public void testEnlargeCancelsShort() throws Exception {
        Paddle paddle = newPaddle();
        Ball ball = newBall();
        collect(2, paddle, ball);
        collect(4, paddle, ball);
        assertEquals(0, manager.getPaddleShortTimer());
        assertEquals(EFFECT_FRAMES, manager.getPaddleLargeTimer());
    }

    // ---cancelletion of half points multiplier---
    @Test
    public void testHalfCancelsDouble() throws Exception {
        Paddle paddle = newPaddle();
        Ball ball = newBall();
        collect(3, paddle, ball);
        collect(6, paddle, ball);
        assertEquals(0, manager.getDoublePointsTimer());
        assertEquals(0.5, manager.getScoreMultiplier(), DELTA);
    }

    // ---same effects taken more times---
    @Test
    public void testSameEffectResetsTimer() throws Exception {
        Paddle paddle = newPaddle();
        Ball ball = newBall();
        collect(2, paddle, ball);
        manager.updateTimer(paddle, ball);
        collect(2, paddle, ball);
        assertEquals(EFFECT_FRAMES, manager.getPaddleShortTimer());
    }

    // -------------------------------------------------------------------------
    // timers and power up
    // -------------------------------------------------------------------------

    // ---udpateTimer()---
    @Test
    public void testUpdateTimerDecreasesFrames() throws Exception {
        collect(5, newPaddle(), newBall()); 
        manager.updateTimer(newPaddle(), newBall());
        assertEquals(EFFECT_FRAMES - 1, manager.getFreezeBlocksTimer());
    }

    // ---doublePointsTimer---
    @Test
    public void testDoublePointsExpiresResetsMultiplier() throws Exception {
        Paddle paddle = newPaddle();
        Ball ball = newBall();
        collect(3, paddle, ball);
        for (int i = 0; i < EFFECT_FRAMES; i++) {
            manager.updateTimer(paddle, ball);
        }
        assertEquals(1.0, manager.getScoreMultiplier(), DELTA);
        assertEquals(0, manager.getDoublePointsTimer());
    }

    // ---fastBallTimer---
    @Test
    public void testFastBallExpiresRestoresSpeed() throws Exception {
        Paddle paddle = newPaddle();
        Ball ball = newBall();
        collect(7, paddle, ball);
        for (int i = 0; i < EFFECT_FRAMES; i++) {
            manager.updateTimer(paddle, ball);
        }
        assertEquals(4, ball.getVelocityX(), DELTA);
        assertEquals(8, ball.getVelocityY(), DELTA);
        assertEquals(0, manager.getFastBallTimer());
    }

    // ---removal of the power up at the bottom of the screen---
    @Test
    public void testCapsuleOutOfBoundsIsRemoved() throws Exception {
        injectPowerUp(new PowerUpImpl(130, 598, 3));
        manager.updatePowerUp(newPaddle(), newBall(), SCREEN_HEIGHT);
        assertTrue(manager.getActivePowerUp().isEmpty());
    }

    // ---test the falling of the power up if not taken---
    @Test
    public void testCapsuleNotCollectedKeepsFalling() throws Exception {
        injectPowerUp(new PowerUpImpl(400, 100, 3));
        manager.updatePowerUp(newPaddle(), newBall(), SCREEN_HEIGHT);
        List<PowerUpImpl> list = manager.getActivePowerUp();
        assertEquals(1, list.size());
        assertEquals(103, list.get(0).getY(), DELTA);
    }

    // ---resetBallframes()---
    @Test
    public void testResetFastBallFrames() throws Exception {
        collect(7, newPaddle(), newBall());
        manager.resetFastBallFrames();
        assertEquals(0, manager.getFastBallTimer());
    }
}
