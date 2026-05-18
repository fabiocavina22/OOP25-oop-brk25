package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Brick;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelManagerImpl implements LevelManager {

    private final List<Brick> activeBricks;
    private final int screenWidth;
    private final int screenHeight;
    private final int brickWidth;
    private final int brickHeight;
    private final double rowSpacing;

    private double scrollSpeed;
    private double distanceSinceLastRow;
    private int rowsGenerated;

    private final Random rng = new Random();

    private static final double BASE_SPEED      = 30.0;
    private static final double SPEED_INCREMENT = 0.5;
    private static final double ROW_GAP         = 10.0;
    private static final int    INITIAL_ROWS    = 3;

    /**
     * @param screenWidth  width of the game screen in pixels
     * @param brickWidth   width of a single brick in pixels
     * @param brickHeight  height of a single brick in pixels
     * @param screenHeight height of the game screen in pixels
     */
    public LevelManagerImpl(int screenWidth, int brickWidth, int brickHeight, int screenHeight) {
        this.activeBricks = new ArrayList<>();
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        this.brickWidth   = brickWidth;
        this.brickHeight  = brickHeight;
        this.rowSpacing   = brickHeight + ROW_GAP;
        reset();
    }

    /**
     * Resets the level to its initial state.
     * Clears all bricks, resets speed and counters,
     * and pre-generates the first rows on screen.
     */
    @Override
    public void reset() {
        activeBricks.clear();
        scrollSpeed          = BASE_SPEED;
        distanceSinceLastRow = 0.0;
        rowsGenerated        = 0;
        for (int r = 0; r < INITIAL_ROWS; r++) {
            generateNewRow(r * rowSpacing);
        }
    }

    /** Returns an unmodifiable view of the active bricks. */
    @Override
    public List<Brick> getActiveBricks() {
        return Collections.unmodifiableList(activeBricks);
    }

    /** Returns the current scroll speed in pixels per second. */
    @Override
    public double getScrollSpeed() {
        return scrollSpeed;
    }

    /** Returns the total number of rows generated since the game started. */
    @Override
    public int getRowsGenerated() {
        return rowsGenerated;
    }

    /**
     * Updates brick positions and spawns new rows each frame.
     * Moves all bricks down, removes off-screen ones,
     * and increases speed as more rows are generated.
     *
     * @param deltaTime time elapsed since the last frame, in seconds
     */
    @Override
    public void update(double deltaTime) {
        double movement = scrollSpeed * deltaTime;

        for (Brick b : activeBricks) {
            b.moveDown(movement);
        }

        activeBricks.removeIf(b -> b.getY() > screenHeight);

        distanceSinceLastRow += movement;
        if (distanceSinceLastRow >= rowSpacing) {
            generateNewRow(-brickHeight);
            distanceSinceLastRow = 0.0;
            scrollSpeed = BASE_SPEED + rowsGenerated * SPEED_INCREMENT;
        }
    }

    /**
     * Removes a brick from the active list.
     * Call this when the ball destroys a brick.
     */
    @Override
    public void removeBrick(Brick brick) {
        activeBricks.remove(brick);
    }

    /**
     * Returns true if any brick's bottom edge has reached or passed thresholdY.
     * Typically used to detect bricks touching the paddle area.
     *
     * @param thresholdY the Y coordinate of the danger line (usually the paddle's Y)
     * @return true if at least one brick has crossed the threshold
     */
    @Override
    public boolean hasBricksReachedThreshold(double thresholdY) {
        return activeBricks.stream()
                .anyMatch(b -> b.getY() + brickHeight >= thresholdY);
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Generates a full row of bricks at the given Y position.
     * Indestructible bricks are capped at one third of the columns.
     *
     * @param yPosition Y coordinate where the row is placed
     */
    private void generateNewRow(double yPosition) {
        int columns             = screenWidth / brickWidth;
        int maxIndestructible   = columns / 3;
        int indestructibleCount = 0;

        for (int i = 0; i < columns; i++) {
            double xPosition = i * brickWidth;
            int type = chooseBrickType(indestructibleCount, maxIndestructible);
            if (type == 3) indestructibleCount++;
            activeBricks.add(new BrickImpl(xPosition, yPosition, type));
        }

        rowsGenerated++;
    }

    /**
     * Picks a brick type using weighted random selection:
     * 10% indestructible (type 3, capped at max),
     * 25% double-hit (type 2),
     * 65% normal (type 1).
     *
     * @param currentIndestructible indestructible bricks already placed in this row
     * @param max                   maximum allowed indestructible bricks per row
     * @return brick type: 1, 2, or 3
     */
    private int chooseBrickType(int currentIndestructible, int max) {
        int roll = rng.nextInt(100);
        if (roll < 10 && currentIndestructible < max) return 3;
        if (roll < 35) return 2;
        return 1;
    }
}