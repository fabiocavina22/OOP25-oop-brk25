package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.LevelManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerImplTest {

    private static final int SCREEN_WIDTH  = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int BRICK_WIDTH   = 80;
    private static final int BRICK_HEIGHT  = 20;

    private LevelManager levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManagerImpl(SCREEN_WIDTH, BRICK_WIDTH, BRICK_HEIGHT, SCREEN_HEIGHT);
    }

    // -------------------------------------------------------------------------
    // reset()
    // -------------------------------------------------------------------------

    @Test
    void testResetGeneratesInitialBricks() {
        // After construction (which calls reset), bricks must be present
        assertFalse(levelManager.getActiveBricks().isEmpty(),
                "Active bricks should not be empty after reset");
    }

    @Test
    void testResetClearsPreviousState() {
        // Simulate some updates, then reset
        levelManager.update(5.0);
        levelManager.reset();

        // rowsGenerated restarts from INITIAL_ROWS (3 pre-generated rows)
        assertEquals(3, levelManager.getRowsGenerated(),
                "rowsGenerated should equal INITIAL_ROWS after reset");
    }

    @Test
    void testResetRestoresBaseSpeed() {
        levelManager.update(100.0); // force many row spawns → speed increases
        levelManager.reset();
        assertEquals(30.0, levelManager.getScrollSpeed(), 0.001,
                "Scroll speed should be reset to BASE_SPEED");
    }

    // -------------------------------------------------------------------------
    // getActiveBricks()
    // -------------------------------------------------------------------------

    @Test
    void testGetActiveBricksIsUnmodifiable() {
        List<Brick> bricks = levelManager.getActiveBricks();
        assertThrows(UnsupportedOperationException.class,
                () -> bricks.remove(0),
                "Active bricks list should be unmodifiable");
    }

    @Test
    void testInitialBrickCountMatchesExpectedRows() {
        // INITIAL_ROWS = 3, columns = SCREEN_WIDTH / BRICK_WIDTH = 10
        int expectedColumns = SCREEN_WIDTH / BRICK_WIDTH;
        int expectedBricks  = 3 * expectedColumns;
        assertEquals(expectedBricks, levelManager.getActiveBricks().size(),
                "Initial brick count should be INITIAL_ROWS * columns");
    }

    // -------------------------------------------------------------------------
    // update()
    // -------------------------------------------------------------------------

    @Test
    void testUpdateMovesBricksDown() {
        double initialY = levelManager.getActiveBricks().get(0).getY();
        levelManager.update(1.0); // 1 second → bricks move down by scrollSpeed px
        double newY = levelManager.getActiveBricks().get(0).getY();
        assertTrue(newY > initialY,
                "Bricks should move down after update");
    }

    @Test
    void testUpdateSpawnsNewRowOverTime() {
        int initialRows = levelManager.getRowsGenerated();
        // Force enough time to trigger at least one new row spawn
        levelManager.update(10.0);
        assertTrue(levelManager.getRowsGenerated() > initialRows,
                "New rows should be generated after sufficient time");
    }

    @Test
    void testUpdateRemovesOffScreenBricks() {
        // Push all bricks past the bottom of the screen
        levelManager.update(1000.0);
        for (Brick b : levelManager.getActiveBricks()) {
            assertTrue(b.getY() <= SCREEN_HEIGHT,
                    "No brick should be below the screen after update");
        }
    }

    @Test
    void testScrollSpeedIncreasesOverTime() {
        double initialSpeed = levelManager.getScrollSpeed();
        levelManager.update(50.0); // trigger many row spawns
        assertTrue(levelManager.getScrollSpeed() > initialSpeed,
                "Scroll speed should increase as more rows are generated");
    }

    // -------------------------------------------------------------------------
    // removeBrick()
    // -------------------------------------------------------------------------

    @Test
    void testRemoveBrickDecreasesCount() {
        List<Brick> bricks = levelManager.getActiveBricks();
        int initialSize = bricks.size();
        Brick toRemove = bricks.get(0);

        levelManager.removeBrick(toRemove);

        assertEquals(initialSize - 1, levelManager.getActiveBricks().size(),
                "Active brick count should decrease by 1 after removal");
    }

    @Test
    void testRemoveBrickActuallyRemovesCorrectBrick() {
        Brick toRemove = levelManager.getActiveBricks().get(0);
        levelManager.removeBrick(toRemove);
        assertFalse(levelManager.getActiveBricks().contains(toRemove),
                "Removed brick should no longer be in the active list");
    }

    // -------------------------------------------------------------------------
    // hasBricksReachedThreshold()
    // -------------------------------------------------------------------------

    @Test
    void testHasBricksReachedThresholdFalseInitially() {
        // Bricks start near y=0, paddle is near bottom
        assertFalse(levelManager.hasBricksReachedThreshold(SCREEN_HEIGHT - 50),
                "No brick should have reached the paddle threshold initially");
    }

    @Test
    void testHasBricksReachedThresholdTrueAfterScroll() {
        double threshold = SCREEN_HEIGHT - 50;
        boolean reached = false;

        // Small steps: movement = 30 × 0.1 = 3px per frame
        // Bricks cross the threshold gradually and are detectable
        for (int i = 0; i < 500; i++) {
            levelManager.update(0.1);
            if (levelManager.hasBricksReachedThreshold(threshold)) {
                reached = true;
                break;
            }
        }

        assertTrue(reached, "Bricks should reach the threshold after scrolling far enough");
    }

    @Test
    void testHasBricksReachedThresholdUsesBottomEdge() {
        // Place a brick just above the threshold and check bottom edge detection
        // brickHeight = 20, so a brick at y = thresholdY - brickHeight is exactly on the line
        double threshold = 500.0;
        // Scroll until at least one brick bottom edge hits the threshold
        for (int i = 0; i < 1000; i++) {
            levelManager.update(0.1);
            if (levelManager.hasBricksReachedThreshold(threshold)) break;
        }
        assertTrue(levelManager.hasBricksReachedThreshold(threshold),
                "Bottom edge of brick should trigger the threshold check");
    }

    // -------------------------------------------------------------------------
    // getScrollSpeed() / getRowsGenerated()
    // -------------------------------------------------------------------------

    @Test
    void testGetScrollSpeedInitialValue() {
        assertEquals(30.0, levelManager.getScrollSpeed(), 0.001,
                "Initial scroll speed should be BASE_SPEED (30.0)");
    }

    @Test
    void testGetRowsGeneratedInitialValue() {
        assertEquals(3, levelManager.getRowsGenerated(),
                "Rows generated should equal INITIAL_ROWS after construction");
    }

    @Test
    void testGetRowsGeneratedIncreasesMonotonically() {
        int prev = levelManager.getRowsGenerated();
        for (int i = 0; i < 5; i++) {
            levelManager.update(5.0);
            int current = levelManager.getRowsGenerated();
            assertTrue(current >= prev,
                    "rowsGenerated should never decrease");
            prev = current;
        }
    }
}