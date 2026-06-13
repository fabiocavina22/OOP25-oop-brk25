package it.unibo.breakout;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickImpl;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.impl.LevelManagerImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelManagerImplTest {

    private static final int SCREEN_WIDTH  = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int BRICK_WIDTH   = 80;
    private static final int BRICK_HEIGHT  = 20;

    private LevelManagerImpl levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManagerImpl(SCREEN_WIDTH, BRICK_WIDTH, BRICK_HEIGHT, SCREEN_HEIGHT);
    }

    private void injectBricks(List<Brick> bricks) throws Exception {
        Field field = LevelManagerImpl.class.getDeclaredField("activeBricks");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Brick> activeList = (List<Brick>) field.get(levelManager);
        activeList.clear();
        activeList.addAll(bricks);
    }

    // -------------------------------------------------------------------------
    // reset()
    // -------------------------------------------------------------------------

    @Test
    void testResetGeneratesInitialBricks() {
        assertFalse(levelManager.getActiveBricks().isEmpty(),
                "Active bricks should not be empty after reset");
    }

    @Test
    void testResetClearsPreviousState() {
        levelManager.update(5.0);
        levelManager.reset();
        assertEquals(3, levelManager.getRowsGenerated(),
                "rowsGenerated should equal INITIAL_ROWS after reset");
    }

    @Test
    void testResetRestoresBaseSpeed() {
        levelManager.update(100.0);
        levelManager.reset();
        assertEquals(3.0, levelManager.getScrollSpeed(), 0.001,
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
        levelManager.update(50.0);
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
    // removeDestroyedBricks()
    // -------------------------------------------------------------------------

    @Test
    void testRemoveDestroyedBricks() throws Exception {
        BrickImpl normalBrick = new BrickImpl(0, 0, 1, 80, 80, 1, 0);
        BrickImpl destroyedBrick = new BrickImpl(80, 0, 1, 80, 80, 1, 1);

        destroyedBrick.hit();

        injectBricks(List.of(normalBrick, destroyedBrick));
        levelManager.removeDestroyedBricks();
        List<Brick> bricks = levelManager.getActiveBricks();
        assertEquals(1, bricks.size());
        assertTrue(bricks.contains(normalBrick));
        assertFalse(bricks.contains(destroyedBrick));
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

        // movement = 3.0 × 0.1 = 0.3px per frame → need ~2000 frames to cross ~550px
        for (int i = 0; i < 2000; i++) {
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
    // updateDimensions()
    // -------------------------------------------------------------------------

    @Test
    void testUpdateDimensionsInvalidInputs() {
        int initialCount = levelManager.getActiveBricks().size();

        levelManager.updateDimensions(0, 600);
        assertEquals(initialCount, levelManager.getActiveBricks().size());

        levelManager.updateDimensions(800, -10);
        assertEquals(initialCount, levelManager.getActiveBricks().size());
    }

    @Test
    void testUpdateDimensionsFirstResize() {
        levelManager.updateDimensions(1000, 700);

        List<Brick> bricks = levelManager.getActiveBricks();
        assertFalse(bricks.isEmpty());
        // On a 1000px screen divided into 10 columns, the initial width is 100
        assertEquals(100.0, bricks.get(0).getWidth(), 0.001);
    }

    @Test
    void testUpdateDimensionsSubsequentResizeScalesBricks() {
        levelManager.updateDimensions(800, 600);
        double originalX = levelManager.getActiveBricks().get(1).getX();

        // Next resizing: 1.5x scale (800 -> 1200)
        levelManager.updateDimensions(1200, 900);

        double scaledX = levelManager.getActiveBricks().get(1).getX();
        assertEquals(originalX * 1.5, scaledX, 0.01,
                "The X coordinates of the existing blocks must be scaled proportionally");
    }


    // -------------------------------------------------------------------------
    // getScrollSpeed() / getRowsGenerated()
    // -------------------------------------------------------------------------

    @Test
    void testGetScrollSpeedInitialValue() {
        assertEquals(3.0, levelManager.getScrollSpeed(), 0.001,
                "Initial scroll speed should be BASE_SPEED (3.0)");
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