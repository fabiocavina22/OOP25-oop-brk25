package it.unibo.breakout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.breakout.model.impl.PaddleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestPaddle {

    private static final int X = 50;
    private static final int Y = 100;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 20;
    private static final int SPEED = 10;

    private static final int SCREEN_WIDTH = 500;
    private static final int PANEL_WIDTH = 1920;
    private static final int PANEL_HEIGHT = 1080;

    private static final int CHANGE = 30;
    private static final int MIN_WIDTH = 60;
    private static final int MAX_WIDTH = 240;
    private static final int EXPECTED_WIDTH = 480;
    private static final int EXPECTED_Y = 864;

    private static final double DELTA = 0.0001;

    private PaddleImpl paddle;

    /* constructor */

    @BeforeEach
    void setUp() {
        paddle = new PaddleImpl(X, Y, WIDTH, HEIGHT, SPEED);
        assertEquals(X, paddle.getX());
        assertEquals(Y, paddle.getY());
        assertEquals(WIDTH, paddle.getWidth());
        assertEquals(HEIGHT, paddle.getHeight());
        assertEquals(SPEED, paddle.getSpeed());
    }

    /* movements */

    @Test
    void testPaddleMoveLeft() {
        paddle.moveLeft();
        assertEquals(X - SPEED, paddle.getX(), DELTA);
    }

    @Test
    void testPaddleMoveRight() {
        paddle.moveRight();
        assertEquals(X + SPEED, paddle.getX(), DELTA);
    }

    @Test
    void leftLimit() {
        final PaddleImpl p = new PaddleImpl(-20, Y, WIDTH, HEIGHT, SPEED);
        p.clamp(SCREEN_WIDTH);
        assertEquals(0, p.getX(), DELTA);
    }

    @Test
    void rightLimit() {
        final PaddleImpl p = new PaddleImpl(450, Y, WIDTH, HEIGHT, SPEED);
        p.clamp(SCREEN_WIDTH);
        assertEquals(SCREEN_WIDTH - p.getWidth(), p.getX(), DELTA);
    }

    /* dimensions */

    @Test
    void dimensionsChangesTest() {
        paddle.updateDimensions(PANEL_WIDTH, PANEL_HEIGHT);
        assertEquals(EXPECTED_WIDTH, paddle.getWidth());
        assertEquals(EXPECTED_Y, paddle.getY());
        assertTrue(paddle.getX() >= 0);
        assertTrue(paddle.getX() <= PANEL_WIDTH - paddle.getWidth());
    }

    /* bonus - malus */

    @Test
    void paddleGetsShorter() {
        paddle.paddleShort();
        assertEquals(WIDTH - CHANGE, paddle.getWidth());
    }

    @Test
    void paddleGetsBigger() {
        paddle.paddleLarge();
        assertEquals(WIDTH + CHANGE, paddle.getWidth());
    }

    @Test
    void paddleNotShorterThanLimit() {
        final PaddleImpl p = new PaddleImpl(X, Y, MIN_WIDTH, HEIGHT, SPEED);
        p.paddleShort();
        assertEquals(MIN_WIDTH, p.getWidth());
    }

    @Test
    void paddleNotGreaterThanLimit() {
        final PaddleImpl p = new PaddleImpl(X, Y, MAX_WIDTH, HEIGHT, SPEED);
        p.paddleLarge();
        assertEquals(MAX_WIDTH, p.getWidth());
    }
}
