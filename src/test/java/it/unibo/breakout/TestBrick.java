package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickImpl;

public class TestBrick {

    @Test
    public void testNormalBrick() {
        // A normal brick with 1 life
        BrickImpl b = new BrickImpl(10, 10, 1, 4, 4);
        assertFalse(b.isDestroyed());
        b.hit();
        assertTrue(b.isDestroyed());
    }

    @Test
    public void testHardBrick() {
        // A double-hit brick with 2 lives
        BrickImpl b = new BrickImpl(10, 10, 2, 4, 4);
        b.hit();
        assertFalse(b.isDestroyed()); //vivo
        b.hit();
        assertTrue(b.isDestroyed());  //distrutto
    }

    @Test
    public void testIndestructibleBrick() {
        // An indestructible brick should never be destroyed
        BrickImpl b = new BrickImpl(10, 10, 3, 4, 4);
        for(int i=0; i<100; i++) {
            b.hit();
        }
        assertFalse(b.isDestroyed());
    }
}