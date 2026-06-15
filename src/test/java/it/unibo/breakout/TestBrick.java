package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickFactory;
import it.unibo.breakout.model.impl.BrickImpl;

/**
 * Unit tests for the {@link BrickImpl} class.
 */
class TestBrick {

    private static final int    BRICK_X       = 10;
    private static final int    BRICK_Y       = 10;
    private static final int    BRICK_WIDTH   = 4;
    private static final int    BRICK_HEIGHT  = 4;
    private static final int    BRICK_ROW     = 2;
    private static final int    BRICK_COL     = 1;
    private static final double INITIAL_Y     = 10.0;
    private static final double MOVE_DELTA    = 5.5;
    private static final double EXPECTED_Y    = 15.5;
    private static final double TOLERANCE     = 0.001;
    private static final int    HIT_LOOPS     = 100;
    private static final double GETTER_X_D    = 10.0;
    private static final double GETTER_Y_D    = 20.0;
    private static final int    GETTER_W      = 30;
    private static final int    GETTER_H      = 40;
    private static final int    GETTER_ROW    = 5;
    private static final int    GETTER_COL    = 6;
    private static final double SETTER_X      = 15.5;
    private static final double SETTER_Y      = 25.5;
    private static final int    SETTER_W      = 35;
    private static final int    SETTER_H      = 45;

    @Test
    void testNormalBrick() {
        final BrickImpl b = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_NORMAL,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        assertFalse(b.isDestroyed());
        b.hit();
        assertTrue(b.isDestroyed());
    }

    @Test
    void testHardBrick() {
        final BrickImpl b = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_DOUBLE,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        b.hit();
        assertFalse(b.isDestroyed());
        b.hit();
        assertTrue(b.isDestroyed());
    }

    @Test
    void testIndestructibleBrick() {
        final BrickImpl b = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_INDESTRUCTIBLE,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        for (int i = 0; i < HIT_LOOPS; i++) {
            b.hit();
        }
        assertFalse(b.isDestroyed());
    }

    @Test
    void testMovement() {
        final BrickImpl b = new BrickImpl(
                GETTER_X_D,
                INITIAL_Y,
                BrickFactory.TYPE_NORMAL,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        assertEquals(INITIAL_Y, b.getY(), TOLERANCE);
        b.moveDown(MOVE_DELTA);
        assertEquals(EXPECTED_Y, b.getY(), TOLERANCE);
    }

    @Test
    void testSpecialBricks() {
        final BrickImpl type4 = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_BONUS_MALUS,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        assertEquals(1, type4.getLife());
        assertFalse(type4.isIndestructible());
        assertFalse(type4.isDestroyed());
        type4.hit();
        assertTrue(type4.isDestroyed());

        final BrickImpl type5 = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_TNT,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        assertEquals(1, type5.getLife());
        assertFalse(type5.isIndestructible());
        assertFalse(type5.isDestroyed());
        type5.hit();
        assertTrue(type5.isDestroyed());
    }

    @Test
    void testHardBrickTypeTransition() {
        final BrickImpl b = new BrickImpl(
                BRICK_X,
                BRICK_Y,
                BrickFactory.TYPE_DOUBLE,
                BRICK_WIDTH,
                BRICK_HEIGHT,
                BRICK_ROW,
                BRICK_COL
        );
        assertEquals(BrickFactory.TYPE_DOUBLE, b.getType());
        b.hit();
        assertEquals(BrickFactory.TYPE_NORMAL, b.getType());
    }

    @Test
    void testGettersAndSetters() {
        final BrickImpl b = new BrickImpl(
                GETTER_X_D,
                GETTER_Y_D,
                BrickFactory.TYPE_NORMAL,
                GETTER_W,
                GETTER_H,
                GETTER_ROW,
                GETTER_COL
        );
        assertEquals(GETTER_X_D, b.getX(), TOLERANCE);
        assertEquals(GETTER_Y_D, b.getY(), TOLERANCE);
        assertEquals(GETTER_W, b.getWidth());
        assertEquals(GETTER_H, b.getHeight());
        assertEquals(GETTER_ROW, b.getRowId());
        assertEquals(GETTER_COL, b.getColIndex());
        assertEquals(1, b.getLife());
        assertEquals(BrickFactory.TYPE_NORMAL, b.getType());

        b.setX(SETTER_X);
        b.setY(SETTER_Y);
        b.setWidth(SETTER_W);
        b.setHeight(SETTER_H);

        assertEquals(SETTER_X, b.getX(), TOLERANCE);
        assertEquals(SETTER_Y, b.getY(), TOLERANCE);
        assertEquals(SETTER_W, b.getWidth());
        assertEquals(SETTER_H, b.getHeight());
    }

}
