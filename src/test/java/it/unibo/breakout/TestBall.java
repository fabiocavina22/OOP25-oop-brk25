package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BallImpl;
import it.unibo.breakout.model.impl.PaddleImpl;

/**
 * Unit tests for {@link BallImpl}.
 */
final class TestBall {

    private static final double DELTA             = 1e-9;
    private static final int    NEG_RADIUS        = -5;
    private static final int    BALL_Y_INIT       = 20;
    private static final int    BALL_RADIUS       = 5;
    private static final int    VEL_Y             = 7;
    private static final int    EXPECTED_X_MOVE   = 13;
    private static final int    EXPECTED_Y_MOVE   = 27;
    private static final int    TRIPLE_X          = 6;
    private static final int    TRIPLE_Y          = 9;
    private static final int    NEG_VX            = -2;
    private static final int    NEG_VY            = -4;
    private static final int    BOUNCED_VX        = -3;
    private static final int    BOUNCED_VY        = -7;
    private static final int    AFTER_BOUNCEX_Y   = 23;
    private static final int    SET_POS_X         = 50;
    private static final int    SET_POS_Y         = 75;
    private static final int    BALL_Y_PAST       = 91;
    private static final int    PANEL_DIM         = 200;
    private static final int    PADDLE_Y_FLY      = 150;
    private static final int    PADDLE_WIDTH      = 80;
    private static final int    PADDLE_HEIGHT     = 15;
    private static final int    PADDLE_SPEED      = 12;
    private static final int    COORD_30          = 30;
    private static final int    NEAR_EDGE         = 195;
    private static final int    CLAMPED_POS       = 190;
    private static final int    PADDLE_Y_STAT     = 300;
    private static final int    PANEL_W_STAT      = 800;
    private static final int    PANEL_H_STAT      = 600;
    private static final int    EXPECTED_CENTER_X = 130;
    private static final int    EXPECTED_CENTER_Y = 290;
    private static final int    RADIUS_SEVEN      = 7;

    // --- Costruttore ---

    /**
     * Verifies that constructing a ball with radius zero throws IllegalArgumentException.
     */
    @Test
    void testConstructorThrowsOnZeroRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new BallImpl(0, 0, 0, 0, 0));
    }

    /**
     * Verifies that constructing a ball with a negative radius throws IllegalArgumentException.
     */
    @Test
    void testConstructorThrowsOnNegativeRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new BallImpl(0, 0, NEG_RADIUS, 3, 4));
    }

    /**
     * Verifies that constructing a ball with a valid positive radius does not throw.
     */
    @Test
    void testConstructorValidRadiusDoesNotThrow() {
        assertDoesNotThrow(() -> new BallImpl(10, BALL_Y_INIT, 1, 3, 4));
    }

    // --- move() ---

    /**
     * Verifies that move() updates position by the current velocity.
     */
    @Test
    void testMoveUpdatesPositionByVelocity() {
        final BallImpl ball = new BallImpl(10, BALL_Y_INIT, BALL_RADIUS, 3, VEL_Y);
        ball.move();
        assertEquals(EXPECTED_X_MOVE, ball.getX(), DELTA);
        assertEquals(EXPECTED_Y_MOVE, ball.getY(), DELTA);
    }

    /**
     * Verifies that repeated calls to move() accumulate position changes correctly.
     */
    @Test
    void testMoveAccumulatesOverMultipleCalls() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 2, 3);
        ball.move();
        ball.move();
        ball.move();
        assertEquals(TRIPLE_X, ball.getX(), DELTA);
        assertEquals(TRIPLE_Y, ball.getY(), DELTA);
    }

    /**
     * Verifies that move() with negative velocity decreases the ball's position.
     */
    @Test
    void testMoveWithNegativeVelocityDecreasesPosition() {
        final BallImpl ball = new BallImpl(10, BALL_Y_INIT, BALL_RADIUS, NEG_VX, NEG_VY);
        ball.move();
        assertEquals(8, ball.getX(), DELTA);
        assertEquals(16, ball.getY(), DELTA);
    }

    // --- bounceX() ---

    /**
     * Verifies that bounceX() inverts the horizontal velocity.
     */
    @Test
    void testBounceXInvertsVelocityX() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceX();
        assertEquals(BOUNCED_VX, ball.getVelocityX(), DELTA);
    }

    /**
     * Verifies that bounceX() does not affect the vertical velocity.
     */
    @Test
    void testBounceXLeavesVelocityYUnchanged() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceX();
        assertEquals(VEL_Y, ball.getVelocityY(), DELTA);
    }

    /**
     * Verifies that two consecutive bounceX() calls restore the original horizontal velocity.
     */
    @Test
    void testBounceXTwiceRestoresOriginalVelocity() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceX();
        ball.bounceX();
        assertEquals(3, ball.getVelocityX(), DELTA);
    }

    /**
     * Verifies that bounceX() followed by move() reverses horizontal movement.
     */
    @Test
    void testBounceXReversesMoveDirectionOnX() {
        final BallImpl ball = new BallImpl(10, BALL_Y_INIT, BALL_RADIUS, 4, 3);
        ball.bounceX();
        ball.move();
        assertEquals(TRIPLE_X, ball.getX(), DELTA);          // 10 + (-4)
        assertEquals(AFTER_BOUNCEX_Y, ball.getY(), DELTA);   // 20 + 3
    }

    // --- bounceY() ---

    /**
     * Verifies that bounceY() inverts the vertical velocity.
     */
    @Test
    void testBounceYInvertsVelocityY() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceY();
        assertEquals(BOUNCED_VY, ball.getVelocityY(), DELTA);
    }

    /**
     * Verifies that bounceY() does not affect the horizontal velocity.
     */
    @Test
    void testBounceYLeavesVelocityXUnchanged() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceY();
        assertEquals(3, ball.getVelocityX(), DELTA);
    }

    /**
     * Verifies that two consecutive bounceY() calls restore the original vertical velocity.
     */
    @Test
    void testBounceYTwiceRestoresOriginalVelocity() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, VEL_Y);
        ball.bounceY();
        ball.bounceY();
        assertEquals(VEL_Y, ball.getVelocityY(), DELTA);
    }

    /**
     * Verifies that bounceY() followed by move() reverses vertical movement.
     */
    @Test
    void testBounceYReversesMoveDirectionOnY() {
        final BallImpl ball = new BallImpl(10, BALL_Y_INIT, BALL_RADIUS, 3, 4);
        ball.bounceY();
        ball.move();
        assertEquals(EXPECTED_X_MOVE, ball.getX(), DELTA);  // 10 + 3
        assertEquals(16, ball.getY(), DELTA);                // 20 + (-4)
    }

    // --- setPosition() ---

    /**
     * Verifies that setPosition() updates both coordinates.
     */
    @Test
    void testSetPositionUpdatesCoordinates() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, 4);
        ball.setPosition(SET_POS_X, SET_POS_Y);
        assertEquals(SET_POS_X, ball.getX(), DELTA);
        assertEquals(SET_POS_Y, ball.getY(), DELTA);
    }

    /**
     * Verifies that setPosition() does not alter the ball's velocity.
     */
    @Test
    void testSetPositionDoesNotAffectVelocity() {
        final BallImpl ball = new BallImpl(0, 0, BALL_RADIUS, 3, 4);
        ball.setPosition(SET_POS_X, SET_POS_Y);
        assertEquals(3, ball.getVelocityX(), DELTA);
        assertEquals(4, ball.getVelocityY(), DELTA);
    }

    // --- isOutOfBounds() ---

    /**
     * Verifies that isOutOfBounds() returns true when the ball's bottom edge exceeds the field height.
     */
    @Test
    void testIsOutOfBoundsReturnsTrueWhenBallPastBottom() {
        // bordo inferiore della palla: 91 + 10 = 101 > 100 → true
        final BallImpl ball = new BallImpl(SET_POS_X, BALL_Y_PAST, 10, 0, 0);
        assertTrue(ball.isOutOfBounds(100));
    }

    /**
     * Verifies that isOutOfBounds() returns false when the ball is above the bottom boundary.
     */
    @Test
    void testIsOutOfBoundsReturnsFalseWhenBallAboveBottom() {
        // bordo inferiore: 50 + 10 = 60, non supera 100 → false
        final BallImpl ball = new BallImpl(SET_POS_X, SET_POS_X, 10, 0, 0);
        assertFalse(ball.isOutOfBounds(100));
    }

    /**
     * Verifies that isOutOfBounds() returns false when the ball's bottom edge exactly meets the boundary.
     */
    @Test
    void testIsOutOfBoundsReturnsFalseWhenBottomEdgeExactlyAtBoundary() {
        // bordo inferiore: 90 + 10 = 100, non strettamente > 100 → false
        final BallImpl ball = new BallImpl(SET_POS_X, 90, 10, 0, 0);
        assertFalse(ball.isOutOfBounds(100));
    }

    // --- getRadius() ---

    /**
     * Verifies that getRadius() returns the value supplied to the constructor.
     */
    @Test
    void testGetRadiusReturnsConstructorValue() {
        final BallImpl ball = new BallImpl(10, BALL_Y_INIT, RADIUS_SEVEN, 3, 4);
        assertEquals(RADIUS_SEVEN, ball.getRadius(), DELTA);
    }

    // --- updateDimensions() ---
    // NON è uno scaler proporzionale: ha due branch distinti.
    // Branch A (vx==0, vy==0): riposiziona la palla centrata sopra il paddle.
    // Branch B (vx!=0 || vy!=0): clamp di x e y se fuoriescono dai bordi.
    // In entrambi: radius, velocityX e velocityY non vengono modificati.

    /**
     * Verifies that updateDimensions() does not move a flying ball that is within panel bounds.
     */
    @Test
    void testUpdateDimensionsFlyingBallInBoundsDoesNotChangePosition() {
        // bordo destro: 50+10=60 <= 200; bordo inferiore: 30+10=40 <= 200 → nessun clamp
        final BallImpl ball = new BallImpl(SET_POS_X, COORD_30, 10, 3, 4);
        ball.updateDimensions(PANEL_DIM, PANEL_DIM,
                new PaddleImpl(0, PADDLE_Y_FLY, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED));
        assertEquals(SET_POS_X, ball.getX(), DELTA);
        assertEquals(COORD_30, ball.getY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() clamps the X coordinate when a flying ball exceeds the panel width.
     */
    @Test
    void testUpdateDimensionsFlyingBallClampsXWhenExceedsWidth() {
        // bordo destro: 195+10=205 > 200 → x = 200-10 = 190; y invariato
        final BallImpl ball = new BallImpl(NEAR_EDGE, COORD_30, 10, 3, 4);
        ball.updateDimensions(PANEL_DIM, PANEL_DIM,
                new PaddleImpl(0, PADDLE_Y_FLY, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED));
        assertEquals(CLAMPED_POS, ball.getX(), DELTA);
        assertEquals(COORD_30, ball.getY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() clamps the Y coordinate when a flying ball exceeds the panel height.
     */
    @Test
    void testUpdateDimensionsFlyingBallClampsYWhenExceedsHeight() {
        // bordo inferiore: 195+10=205 > 200 → y = 200-10 = 190; x invariato
        final BallImpl ball = new BallImpl(COORD_30, NEAR_EDGE, 10, 3, 4);
        ball.updateDimensions(PANEL_DIM, PANEL_DIM,
                new PaddleImpl(0, PADDLE_Y_FLY, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED));
        assertEquals(COORD_30, ball.getX(), DELTA);
        assertEquals(CLAMPED_POS, ball.getY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() does not alter a flying ball's velocity when clamping its position.
     */
    @Test
    void testUpdateDimensionsFlyingBallDoesNotChangeVelocity() {
        // il clamp non deve alterare la velocità
        final BallImpl ball = new BallImpl(NEAR_EDGE, NEAR_EDGE, 10, 3, 4);
        ball.updateDimensions(PANEL_DIM, PANEL_DIM,
                new PaddleImpl(0, PADDLE_Y_FLY, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED));
        assertEquals(3, ball.getVelocityX(), DELTA);
        assertEquals(4, ball.getVelocityY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() does not alter a flying ball's radius.
     */
    @Test
    void testUpdateDimensionsFlyingBallDoesNotChangeRadius() {
        final BallImpl ball = new BallImpl(NEAR_EDGE, NEAR_EDGE, 10, 3, 4);
        ball.updateDimensions(PANEL_DIM, PANEL_DIM,
                new PaddleImpl(0, PADDLE_Y_FLY, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED));
        assertEquals(10, ball.getRadius(), DELTA);
    }

    /**
     * Verifies that updateDimensions() centres a stationary ball on the paddle.
     */
    @Test
    void testUpdateDimensionsStationaryBallCentersOnPaddle() {
        // x = paddleX(100) + paddleWidth/2.0(40.0) - radius(10) = 130
        // y = paddleY(300) - radius(10) = 290
        final PaddleImpl paddle = new PaddleImpl(100, PADDLE_Y_STAT, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED);
        final BallImpl ball = new BallImpl(SET_POS_X, SET_POS_X, 10, 0, 0);
        ball.updateDimensions(PANEL_W_STAT, PANEL_H_STAT, paddle);
        assertEquals(EXPECTED_CENTER_X, ball.getX(), DELTA);
        assertEquals(EXPECTED_CENTER_Y, ball.getY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() does not change a stationary ball's velocity.
     */
    @Test
    void testUpdateDimensionsStationaryBallDoesNotChangeVelocity() {
        final PaddleImpl paddle = new PaddleImpl(100, PADDLE_Y_STAT, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED);
        final BallImpl ball = new BallImpl(SET_POS_X, SET_POS_X, 10, 0, 0);
        ball.updateDimensions(PANEL_W_STAT, PANEL_H_STAT, paddle);
        assertEquals(0, ball.getVelocityX(), DELTA);
        assertEquals(0, ball.getVelocityY(), DELTA);
    }

    /**
     * Verifies that updateDimensions() does not change a stationary ball's radius.
     */
    @Test
    void testUpdateDimensionsStationaryBallDoesNotChangeRadius() {
        final PaddleImpl paddle = new PaddleImpl(100, PADDLE_Y_STAT, PADDLE_WIDTH, PADDLE_HEIGHT, PADDLE_SPEED);
        final BallImpl ball = new BallImpl(SET_POS_X, SET_POS_X, 10, 0, 0);
        ball.updateDimensions(PANEL_W_STAT, PANEL_H_STAT, paddle);
        assertEquals(10, ball.getRadius(), DELTA);
    }
}
