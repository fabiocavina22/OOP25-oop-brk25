package it.unibo.breakout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.breakout.model.impl.PaddleImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPaddle {

    private static final int X = 50;
    private static final int Y = 100;
    private static final int WIDTH = 120;
    private static final int HEIGHT = 20;
    private static final int SPEED = 10;

    private static final double DELTA = 0.0001;

    private PaddleImpl paddle;


    /** CONSTRUCTOR */

    @BeforeEach
    void setUp(){
        paddle = new PaddleImpl(X, Y, WIDTH, HEIGHT, SPEED);
        assertEquals(paddle.getX(), X);
        assertEquals(paddle.getY(), Y);
        assertEquals(paddle.getWidth(), WIDTH);
        assertEquals(paddle.getHeight(), HEIGHT);
        assertEquals(paddle.getSpeed(), SPEED);

    }

    /** MOVEMENTS */

    @Test
    public void testPaddleMoveLeft(){
        paddle.moveLeft();
        assertEquals(X - SPEED, paddle.getX(), DELTA );
    }

    @Test
    public void testPaddleMoveRight(){
        paddle.moveRight();
        assertEquals(X + SPEED, paddle.getX(), DELTA );
    }

    @Test
    public void leftLimit(){
        final PaddleImpl p = new PaddleImpl(-20, Y, WIDTH, HEIGHT, SPEED);
        p.clamp(500);
        assertEquals(p.getX(), 0, DELTA);
    }

    @Test
    public void rightLimit(){
        final PaddleImpl p = new PaddleImpl(450, Y, WIDTH, HEIGHT, SPEED);
        p.clamp(500);
        assertEquals(p.getX(), 380, DELTA);
    }

    /** DIMENSIONS */

    @Test
    public void dimensionsChangesTest(){

        paddle.updateDimensions(1920, 1080);
        assertEquals(paddle.getWidth(), 480);
        assertEquals(paddle.getY(), 864);
        assertTrue(paddle.getX() >= 0);
        assertTrue(paddle.getX() <= 1920 - paddle.getWidth());

    }

    /** BONUS - MALUS */

    @Test
    public void paddleGetsShorter(){
        paddle.paddleShort();
        assertEquals(paddle.getWidth(), 120-30);
    }

    @Test
    public void paddleGetsBigger(){
        paddle.paddleLarge();
        assertEquals(paddle.getWidth(), 120+30);

    }

    @Test
    public void paddleNotShorterThanLimit() {
        final PaddleImpl p = new PaddleImpl(X, Y, 60, HEIGHT, SPEED);
        p.paddleShort();
        assertEquals(60, p.getWidth());
    }

    @Test
    public void paddleNotGreaterThanLimit() {
        final PaddleImpl p = new PaddleImpl(X, Y, 240, HEIGHT, SPEED);
        p.paddleLarge();
        assertEquals(240, p.getWidth());
    }
}
