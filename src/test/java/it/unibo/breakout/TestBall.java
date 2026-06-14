package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.PaddleImpl;

public class TestBall {

    private static final double DELTA = 1e-9;

    // --- Costruttore ---

    @Test
    public void testConstructorThrowsOnZeroRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new BallImpl(0, 0, 0, 0, 0));
    }

    @Test
    public void testConstructorThrowsOnNegativeRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new BallImpl(0, 0, -5, 3, 4));
    }

    @Test
    public void testConstructorValidRadiusDoesNotThrow() {
        assertDoesNotThrow(() -> new BallImpl(10, 20, 1, 3, 4));
    }

    // --- move() ---

    @Test
    public void testMoveUpdatesPositionByVelocity() {
        BallImpl ball = new BallImpl(10, 20, 5, 3, 7);
        ball.move();
        assertEquals(13, ball.getX(), DELTA);
        assertEquals(27, ball.getY(), DELTA);
    }

    @Test
    public void testMoveAccumulatesOverMultipleCalls() {
        BallImpl ball = new BallImpl(0, 0, 5, 2, 3);
        ball.move();
        ball.move();
        ball.move();
        assertEquals(6, ball.getX(), DELTA);
        assertEquals(9, ball.getY(), DELTA);
    }

    @Test
    public void testMoveWithNegativeVelocityDecreasesPosition() {
        BallImpl ball = new BallImpl(10, 20, 5, -2, -4);
        ball.move();
        assertEquals(8, ball.getX(), DELTA);
        assertEquals(16, ball.getY(), DELTA);
    }

    // --- bounceX() ---

    @Test
    public void testBounceXInvertsVelocityX() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceX();
        assertEquals(-3, ball.getVelocityX(), DELTA);
    }

    @Test
    public void testBounceXLeavesVelocityYUnchanged() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceX();
        assertEquals(7, ball.getVelocityY(), DELTA);
    }

    @Test
    public void testBounceXTwiceRestoresOriginalVelocity() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceX();
        ball.bounceX();
        assertEquals(3, ball.getVelocityX(), DELTA);
    }

    @Test
    public void testBounceXReversesMoveDirectionOnX() {
        BallImpl ball = new BallImpl(10, 20, 5, 4, 3);
        ball.bounceX();
        ball.move();
        assertEquals(6, ball.getX(), DELTA);   // 10 + (-4)
        assertEquals(23, ball.getY(), DELTA);  // 20 + 3, invariato
    }

    // --- bounceY() ---

    @Test
    public void testBounceYInvertsVelocityY() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceY();
        assertEquals(-7, ball.getVelocityY(), DELTA);
    }

    @Test
    public void testBounceYLeavesVelocityXUnchanged() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceY();
        assertEquals(3, ball.getVelocityX(), DELTA);
    }

    @Test
    public void testBounceYTwiceRestoresOriginalVelocity() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 7);
        ball.bounceY();
        ball.bounceY();
        assertEquals(7, ball.getVelocityY(), DELTA);
    }

    @Test
    public void testBounceYReversesMoveDirectionOnY() {
        BallImpl ball = new BallImpl(10, 20, 5, 3, 4);
        ball.bounceY();
        ball.move();
        assertEquals(13, ball.getX(), DELTA);  // 10 + 3, invariato
        assertEquals(16, ball.getY(), DELTA);  // 20 + (-4)
    }

    // --- setPosition() ---

    @Test
    public void testSetPositionUpdatesCoordinates() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 4);
        ball.setPosition(50, 75);
        assertEquals(50, ball.getX(), DELTA);
        assertEquals(75, ball.getY(), DELTA);
    }

    @Test
    public void testSetPositionDoesNotAffectVelocity() {
        BallImpl ball = new BallImpl(0, 0, 5, 3, 4);
        ball.setPosition(50, 75);
        assertEquals(3, ball.getVelocityX(), DELTA);
        assertEquals(4, ball.getVelocityY(), DELTA);
    }

    // --- isOutOfBounds() ---

    @Test
    public void testIsOutOfBoundsReturnsTrueWhenBallPastBottom() {
        // bordo inferiore della palla: 91 + 10 = 101 > 100 → true
        BallImpl ball = new BallImpl(50, 91, 10, 0, 0);
        assertTrue(ball.isOutOfBounds(100));
    }

    @Test
    public void testIsOutOfBoundsReturnsFalseWhenBallAboveBottom() {
        // bordo inferiore: 50 + 10 = 60, non supera 100 → false
        BallImpl ball = new BallImpl(50, 50, 10, 0, 0);
        assertFalse(ball.isOutOfBounds(100));
    }

    @Test
    public void testIsOutOfBoundsReturnsFalseWhenBottomEdgeExactlyAtBoundary() {
        // bordo inferiore: 90 + 10 = 100, non strettamente > 100 → false
        BallImpl ball = new BallImpl(50, 90, 10, 0, 0);
        assertFalse(ball.isOutOfBounds(100));
    }

    // --- getRadius() ---

    @Test
    public void testGetRadiusReturnsConstructorValue() {
        BallImpl ball = new BallImpl(10, 20, 7, 3, 4);
        assertEquals(7, ball.getRadius(), DELTA);
    }

    // --- updateDimensions() ---
    // NON è uno scaler proporzionale: ha due branch distinti.
    // Branch A (vx==0, vy==0): riposiziona la palla centrata sopra il paddle.
    // Branch B (vx!=0 || vy!=0): clamp di x e y se fuoriescono dai bordi.
    // In entrambi: radius, velocityX e velocityY non vengono modificati.

    @Test
    public void testUpdateDimensionsFlyingBallInBoundsDoesNotChangePosition() {
        // bordo destro: 50+10=60 <= 200; bordo inferiore: 30+10=40 <= 200 → nessun clamp
        BallImpl ball = new BallImpl(50, 30, 10, 3, 4);
        ball.updateDimensions(200, 200, new PaddleImpl(0, 150, 80, 15, 12));
        assertEquals(50, ball.getX(), DELTA);
        assertEquals(30, ball.getY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsFlyingBallClampsXWhenExceedsWidth() {
        // bordo destro: 195+10=205 > 200 → x = 200-10 = 190; y invariato
        BallImpl ball = new BallImpl(195, 30, 10, 3, 4);
        ball.updateDimensions(200, 200, new PaddleImpl(0, 150, 80, 15, 12));
        assertEquals(190, ball.getX(), DELTA);
        assertEquals(30, ball.getY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsFlyingBallClampsYWhenExceedsHeight() {
        // bordo inferiore: 195+10=205 > 200 → y = 200-10 = 190; x invariato
        BallImpl ball = new BallImpl(30, 195, 10, 3, 4);
        ball.updateDimensions(200, 200, new PaddleImpl(0, 150, 80, 15, 12));
        assertEquals(30, ball.getX(), DELTA);
        assertEquals(190, ball.getY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsFlyingBallDoesNotChangeVelocity() {
        // il clamp non deve alterare la velocità
        BallImpl ball = new BallImpl(195, 195, 10, 3, 4);
        ball.updateDimensions(200, 200, new PaddleImpl(0, 150, 80, 15, 12));
        assertEquals(3, ball.getVelocityX(), DELTA);
        assertEquals(4, ball.getVelocityY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsFlyingBallDoesNotChangeRadius() {
        BallImpl ball = new BallImpl(195, 195, 10, 3, 4);
        ball.updateDimensions(200, 200, new PaddleImpl(0, 150, 80, 15, 12));
        assertEquals(10, ball.getRadius(), DELTA);
    }

    @Test
    public void testUpdateDimensionsStationaryBallCentersOnPaddle() {
        // x = paddleX(100) + paddleWidth/2.0(40.0) = 140  (centro del paddle = centro della palla)
        // y = paddleY(300) - radius(10) = 290
        PaddleImpl paddle = new PaddleImpl(100, 300, 80, 15, 12);
        BallImpl ball = new BallImpl(50, 50, 10, 0, 0);
        ball.updateDimensions(800, 600, paddle);
        assertEquals(140, ball.getX(), DELTA);
        assertEquals(290, ball.getY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsStationaryBallDoesNotChangeVelocity() {
        PaddleImpl paddle = new PaddleImpl(100, 300, 80, 15, 12);
        BallImpl ball = new BallImpl(50, 50, 10, 0, 0);
        ball.updateDimensions(800, 600, paddle);
        assertEquals(0, ball.getVelocityX(), DELTA);
        assertEquals(0, ball.getVelocityY(), DELTA);
    }

    @Test
    public void testUpdateDimensionsStationaryBallDoesNotChangeRadius() {
        PaddleImpl paddle = new PaddleImpl(100, 300, 80, 15, 12);
        BallImpl ball = new BallImpl(50, 50, 10, 0, 0);
        ball.updateDimensions(800, 600, paddle);
        assertEquals(10, ball.getRadius(), DELTA);
    }
}
