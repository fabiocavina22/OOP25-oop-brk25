package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickImpl;

public class TestBrick {

    @Test
    public void testNormalBrick() {
        // A normal brick with 1 life
        BrickImpl b = new BrickImpl(10, 10, 1, 4, 4, 2,1);
        assertFalse(b.isDestroyed());
        b.hit();
        assertTrue(b.isDestroyed());
    }

    @Test
    public void testHardBrick() {
        // A double-hit brick with 2 lives
        BrickImpl b = new BrickImpl(10, 10, 2, 4, 4, 2,1);
        b.hit();
        assertFalse(b.isDestroyed()); //vivo
        b.hit();
        assertTrue(b.isDestroyed());  //distrutto
    }

    @Test
    public void testIndestructibleBrick() {
        // An indestructible brick should never be destroyed
        BrickImpl b = new BrickImpl(10, 10, 3, 4, 4, 2,1);
        for(int i=0; i<100; i++) {
            b.hit();
        }
        assertFalse(b.isDestroyed());
    }

    @Test
    public void testMovement() {
        BrickImpl b = new BrickImpl(10.0, 10.0, 1, 4, 4, 2, 1);
        assertEquals(10.0, b.getY(), 0.001);
        b.moveDown(5.5);
        assertEquals(15.5, b.getY(), 0.001);
    }

    @Test
    public void testSpecialBricks() {
        BrickImpl type4 = new BrickImpl(10, 10, 4, 4, 4, 2, 1);
        assertEquals(1, type4.getLife());
        assertFalse(type4.isIndestructible());
        assertFalse(type4.isDestroyed());
        type4.hit();
        assertTrue(type4.isDestroyed());

        BrickImpl type5 = new BrickImpl(10, 10, 5, 4, 4, 2, 1);
        assertEquals(1, type5.getLife());
        assertFalse(type5.isIndestructible());
        assertFalse(type5.isDestroyed());
        type5.hit();
        assertTrue(type5.isDestroyed());
    }

    @Test
    public void testHardBrickTypeTransition() {
        BrickImpl b = new BrickImpl(10, 10, 2, 4, 4, 2, 1);
        assertEquals(2, b.getType());
        b.hit();
        assertEquals(1, b.getType());
    }


    @Test
    public void testGettersAndSetters() {
        BrickImpl b = new BrickImpl(10.0, 20.0, 1, 30, 40, 5, 6);
        assertEquals(10.0, b.getX(), 0.001);
        assertEquals(20.0, b.getY(), 0.001);
        assertEquals(30, b.getWidth());
        assertEquals(40, b.getHeight());
        assertEquals(5, b.getRowId());
        assertEquals(6, b.getColIndex());
        assertEquals(1, b.getLife());
        assertEquals(1, b.getType());

        b.setX(15.5);
        b.setY(25.5);
        b.setWidth(35);
        b.setHeight(45);

        assertEquals(15.5, b.getX(), 0.001);
        assertEquals(25.5, b.getY(), 0.001);
        assertEquals(35, b.getWidth());
        assertEquals(45, b.getHeight());
    }

}