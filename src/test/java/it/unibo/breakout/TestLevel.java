package it.unibo.breakout;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.breakout.model.impl.BrickImpl;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.impl.LevelManagerImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelManagerImplTest {

    private static final int SCREEN_WIDTH  = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int BRICK_WIDTH   = 80;
    private static final int BRICK_HEIGHT  = 20;

    private static final double BASE_SPEED         = 3.0;
    private static final double SPEED_TOLERANCE    = 0.001;
    private static final double UPDATE_DELTA_SMALL = 0.1;
    private static final double UPDATE_DELTA_LARGE = 5.0;
    private static final double UPDATE_DELTA_HUGE  = 10.0;
    private static final double THRESHOLD_OFFSET   = 50.0;
    private static final double THRESHOLD_FIXED    = 500.0;
    private static final int    FRAMES_MANY        = 2000;
    private static final int    FRAMES_MEDIUM      = 1000;
    private static final int    BRICK_SIZE_INJECT  = 80;
    private static final int    RESIZE_WIDTH_FIRST = 1000;
    private static final int    RESIZE_HEIGHT_FIRST = 700;
    private static final int    EXPECTED_COL_WIDTH = 100;
    private static final int    RESIZE_WIDTH_BASE  = 800;
    private static final int    RESIZE_HEIGHT_BASE = 600;
    private static final int    RESIZE_WIDTH_LARGE = 1200;
    private static final int    RESIZE_HEIGHT_LARGE = 900;
    private static final double SCALE_FACTOR       = 1.5;
    private static final double SCALE_TOLERANCE    = 0.01;
    private static final int    INVALID_WIDTH_ZERO = 0;
    private static final int    INVALID_HEIGHT_NEG = -10;
    private static final int    ROWS_GENERATED_INIT = 3;
    private static final int    MONOTONE_ITERATIONS = 5;

    private LevelManagerImpl levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new LevelManagerImpl(SCREEN_WIDTH, BRICK_HEIGHT, SCREEN_HEIGHT);
    }

    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    private void injectBricks(final List<Brick> bricks) throws NoSuchFieldException, IllegalAccessException {
        final Field field = LevelManagerImpl.class.getDeclaredField("activeBricks");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        final List<Brick> activeList = (List<Brick>) field.get(levelManager);
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
        levelManager.update(UPDATE_DELTA_LARGE);
        levelManager.reset();
        assertEquals(ROWS_GENERATED_INIT, levelManager.getRowsGenerated(),
                "rowsGenerated should equal INITIAL_ROWS after reset");
    }

    @Test
    void testResetRestoresBaseSpeed() {
        levelManager.update(100.0);
        levelManager.reset();
        assertEquals(BASE_SPEED, levelManager.getScrollSpeed(), SPEED_TOLERANCE,
                "Scroll speed should be reset to BASE_SPEED");
    }

    // -------------------------------------------------------------------------
    // getActiveBricks()
    // -------------------------------------------------------------------------

    @Test
    void testGetActiveBricksIsUnmodifiable() {
        final List<Brick> bricks = levelManager.getActiveBricks();
        assertThrows(UnsupportedOperationException.class,
                () -> bricks.remove(0),
                "Active bricks list should be unmodifiable");
    }

    @Test
    void testInitialBrickCountMatchesExpectedRows() {
        // INITIAL_ROWS = 3, columns = SCREEN_WIDTH / BRICK_WIDTH = 10
        final int expectedColumns = SCREEN_WIDTH / BRICK_WIDTH;
        final int expectedBricks  = ROWS_GENERATED_INIT * expectedColumns;
        assertEquals(expectedBricks, levelManager.getActiveBricks().size(),
                "Initial brick count should be INITIAL_ROWS * columns");
    }

    // -------------------------------------------------------------------------
    // update()
    // -------------------------------------------------------------------------

    @Test
    void testUpdateMovesBricksDown() {
        final double initialY = levelManager.getActiveBricks().get(0).getY();
        levelManager.update(1.0);
        final double newY = levelManager.getActiveBricks().get(0).getY();
        assertTrue(newY > initialY,
                "Bricks should move down after update");
    }

    @Test
    void testUpdateSpawnsNewRowOverTime() {
        final int initialRows = levelManager.getRowsGenerated();
        levelManager.update(UPDATE_DELTA_HUGE);
        assertTrue(levelManager.getRowsGenerated() > initialRows,
                "New rows should be generated after sufficient time");
    }

    @Test
    void testUpdateRemovesOffScreenBricks() {
        levelManager.update(1000.0);
        for (final Brick b : levelManager.getActiveBricks()) {
            assertTrue(b.getY() <= SCREEN_HEIGHT,
                    "No brick should be below the screen after update");
        }
    }

    @Test
    void testScrollSpeedIncreasesOverTime() {
        final double initialSpeed = levelManager.getScrollSpeed();
        levelManager.update(THRESHOLD_OFFSET);
        assertTrue(levelManager.getScrollSpeed() > initialSpeed,
                "Scroll speed should increase as more rows are generated");
    }

    // -------------------------------------------------------------------------
    // removeBrick()
    // -------------------------------------------------------------------------

    @Test
    void testRemoveBrickDecreasesCount() {
        final List<Brick> bricks = levelManager.getActiveBricks();
        final int initialSize = bricks.size();
        final Brick toRemove = bricks.get(0);

        levelManager.removeBrick(toRemove);

        assertEquals(initialSize - 1, levelManager.getActiveBricks().size(),
                "Active brick count should decrease by 1 after removal");
    }

    @Test
    void testRemoveBrickActuallyRemovesCorrectBrick() {
        final Brick toRemove = levelManager.getActiveBricks().get(0);
        levelManager.removeBrick(toRemove);
        assertFalse(levelManager.getActiveBricks().contains(toRemove),
                "Removed brick should no longer be in the active list");
    }

    // -------------------------------------------------------------------------
    // removeDestroyedBricks()
    // -------------------------------------------------------------------------

    @Test
    void testRemoveDestroyedBricks() throws NoSuchFieldException, IllegalAccessException {
        final BrickImpl normalBrick    = new BrickImpl(0, 0, 1, BRICK_SIZE_INJECT, BRICK_SIZE_INJECT, 1, 0);
        final BrickImpl destroyedBrick = new BrickImpl(BRICK_SIZE_INJECT, 0, 1, BRICK_SIZE_INJECT, BRICK_SIZE_INJECT, 1, 1);

        destroyedBrick.hit();

        injectBricks(List.of(normalBrick, destroyedBrick));
        levelManager.removeDestroyedBricks();
        final List<Brick> bricks = levelManager.getActiveBricks();
        assertEquals(1, bricks.size());
        assertTrue(bricks.contains(normalBrick));
        assertFalse(bricks.contains(destroyedBrick));
    }

    // -------------------------------------------------------------------------
    // hasBricksReachedThreshold()
    // -------------------------------------------------------------------------

    @Test
    void testHasBricksReachedThresholdFalseInitially() {
        assertFalse(levelManager.hasBricksReachedThreshold(SCREEN_HEIGHT - THRESHOLD_OFFSET),
                "No brick should have reached the paddle threshold initially");
    }

    @Test
    void testHasBricksReachedThresholdTrueAfterScroll() {
        final double threshold = SCREEN_HEIGHT - THRESHOLD_OFFSET;
        boolean reached = false;

        for (int i = 0; i < FRAMES_MANY; i++) {
            levelManager.update(UPDATE_DELTA_SMALL);
            if (levelManager.hasBricksReachedThreshold(threshold)) {
                reached = true;
                break;
            }
        }

        assertTrue(reached, "Bricks should reach the threshold after scrolling far enough");
    }

    @Test
    void testHasBricksReachedThresholdUsesBottomEdge() {
        for (int i = 0; i < FRAMES_MEDIUM; i++) {
            levelManager.update(UPDATE_DELTA_SMALL);
            if (levelManager.hasBricksReachedThreshold(THRESHOLD_FIXED)) {
                break;
            }
        }
        assertTrue(levelManager.hasBricksReachedThreshold(THRESHOLD_FIXED),
                "Bottom edge of brick should trigger the threshold check");
    }

    // -------------------------------------------------------------------------
    // updateDimensions()
    // -------------------------------------------------------------------------

    @Test
    void testUpdateDimensionsInvalidInputs() {
        final int initialCount = levelManager.getActiveBricks().size();

        levelManager.updateDimensions(INVALID_WIDTH_ZERO, SCREEN_HEIGHT);
        assertEquals(initialCount, levelManager.getActiveBricks().size());

        levelManager.updateDimensions(SCREEN_WIDTH, INVALID_HEIGHT_NEG);
        assertEquals(initialCount, levelManager.getActiveBricks().size());
    }

    @Test
    void testUpdateDimensionsFirstResize() {
        levelManager.updateDimensions(RESIZE_WIDTH_FIRST, RESIZE_HEIGHT_FIRST);

        final List<Brick> bricks = levelManager.getActiveBricks();
        assertFalse(bricks.isEmpty());
        assertEquals(EXPECTED_COL_WIDTH, bricks.get(0).getWidth(), SPEED_TOLERANCE);
    }

    @Test
    void testUpdateDimensionsSubsequentResizeScalesBricks() {
        levelManager.updateDimensions(RESIZE_WIDTH_BASE, RESIZE_HEIGHT_BASE);
        final double originalX = levelManager.getActiveBricks().get(1).getX();

        levelManager.updateDimensions(RESIZE_WIDTH_LARGE, RESIZE_HEIGHT_LARGE);

        final double scaledX = levelManager.getActiveBricks().get(1).getX();
        assertEquals(originalX * SCALE_FACTOR, scaledX, SCALE_TOLERANCE,
                "The X coordinates of the existing blocks must be scaled proportionally");
    }

    // -------------------------------------------------------------------------
    // getScrollSpeed() / getRowsGenerated()
    // -------------------------------------------------------------------------

    @Test
    void testGetScrollSpeedInitialValue() {
        assertEquals(BASE_SPEED, levelManager.getScrollSpeed(), SPEED_TOLERANCE,
                "Initial scroll speed should be BASE_SPEED (3.0)");
    }

    @Test
    void testGetRowsGeneratedInitialValue() {
        assertEquals(ROWS_GENERATED_INIT, levelManager.getRowsGenerated(),
                "Rows generated should equal INITIAL_ROWS after construction");
    }

    @Test
    void testGetRowsGeneratedIncreasesMonotonically() {
        int prev = levelManager.getRowsGenerated();
        for (int i = 0; i < MONOTONE_ITERATIONS; i++) {
            levelManager.update(UPDATE_DELTA_LARGE);
            final int current = levelManager.getRowsGenerated();
            assertTrue(current >= prev,
                    "rowsGenerated should never decrease");
            prev = current;
        }
    }
}
