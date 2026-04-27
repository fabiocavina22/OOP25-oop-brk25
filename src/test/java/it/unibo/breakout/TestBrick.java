package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickImpl;

public class TestBrick {

    @Test
    public void testNormalBrick() {
        // Un mattone normale con 1 vita
        BrickImpl b = new BrickImpl(10, 10, 1);
        assertFalse(b.isDestroyed());
        b.hit();
        assertTrue(b.isDestroyed());
    }

    @Test
    public void testHardBrick() {
        // Un mattone con 2 vite
        BrickImpl b = new BrickImpl(10, 10, 2);
        b.hit();
        assertFalse(b.isDestroyed()); //vivo
        b.hit();
        assertTrue(b.isDestroyed());  //distrutto
    }

    @Test
    public void testIndestructibleBrick() {
        // mattone indistruttibile
        BrickImpl b = new BrickImpl(10, 10, 3);
        for(int i=0; i<100; i++) {
            b.hit();
        }
        assertFalse(b.isDestroyed());
    }
}