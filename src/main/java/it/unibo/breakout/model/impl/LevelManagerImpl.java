package it.unibo.breakout.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Brick;

/**
 * Concrete implementation of the {@link LevelManager} interface.
 * Handles level progression, brick placement, and real-time screen resizing.
 */
public class LevelManagerImpl implements LevelManager {

    private final List<Brick> activeBricks;
    private int screenWidth;
    private int screenHeight;
    private final int brickHeight;
    private double rowSpacing;
    private double scrollSpeed;
    private double distanceSinceLastRow;
    private int rowsGenerated;
    private boolean isFirstResize = true;

    private final Random rng = new Random();

    private static final double BASE_SPEED      = 3.0;
    private static final double SPEED_INCREMENT = 0.5;
    private static final double ROW_GAP         = 0.0;
    private static final int    INITIAL_ROWS    = 3;

    private static final int ROLL_MAX                      = 100;
    private static final int ROLL_THRESHOLD_INDESTRUCTIBLE = 10;
    private static final int ROLL_THRESHOLD_DOUBLE         = 35;
    private static final int ROLL_THRESHOLD_BONUS_MALUS    = 44;
    private static final int ROLL_THRESHOLD_TNT            = 50;

    /**
     * @param screenWidth  width of the game screen in pixels
     * @param brickHeight  height of a single brick in pixels
     * @param screenHeight height of the game screen in pixels
     */
    public LevelManagerImpl(final int screenWidth, final int brickHeight, final int screenHeight) {
        this.activeBricks = new ArrayList<>();
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
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
    public final void reset() {
        activeBricks.clear();
        scrollSpeed          = BASE_SPEED;
        distanceSinceLastRow = rowSpacing;
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
     * Moves all bricks down, removes off-screen and destroyed ones,
     * removes all the indestructible bricks if a row is all destroyed
     * and increases speed as more rows are generated.
     *
     * @param deltaTime time elapsed since the last frame, in seconds
     */
    @Override
    public void update(final double deltaTime) {
        final double movement = scrollSpeed * deltaTime;
        for (final Brick b : activeBricks) {
            b.moveDown(movement);
        }
        activeBricks.removeIf(b -> b.getY() > screenHeight);
        activeBricks.removeIf(Brick::isDestroyed);
        removeIndestructibleFromClearedRows();
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
     *
     * @param brick the brick to be removed.
     */
    @Override
    public void removeBrick(final Brick brick) {
        activeBricks.remove(brick);
    }

    /**
     * Removes the list of bricks if all of them are destroyed.
     */
    @Override
    public void removeDestroyedBricks() {
        activeBricks.removeIf(Brick::isDestroyed);
    }

    /**
     * Returns true if any brick's bottom edge has reached or passed thresholdY.
     * Typically used to detect bricks touching the paddle area.
     *
     * @param thresholdY the Y coordinate of the danger line (usually the paddle's Y)
     * @return true if at least one brick has crossed the threshold
     */
    @Override
    public boolean hasBricksReachedThreshold(final double thresholdY) {
        return activeBricks.stream()
                .anyMatch(b -> b.getY() + brickHeight >= thresholdY);
    }

    /**
     * Proportionally resizes the positions, dimensions, and spacing of all active bricks
     * based on the new dimensions of the game window.
     *
     * @param newWidth  the new width of the screen in pixels.
     * @param newHeight the new height of the screen in pixels.
     */
    @Override
    public void updateDimensions(final int newWidth, final int newHeight) {
        if (newWidth <= 0 || newHeight <= 0) {
            return;
        }

        if (isFirstResize) {
            this.screenWidth = newWidth;
            this.screenHeight = newHeight;
            this.isFirstResize = false;

            activeBricks.clear();
            rowsGenerated = 0;
            this.rowSpacing = (newWidth / 10.0) + ROW_GAP;
            this.distanceSinceLastRow = this.rowSpacing;

            for (int r = 0; r < INITIAL_ROWS; r++) {
                generateNewRow(r * this.rowSpacing);
            }
            return;
        }

        if (this.screenWidth == newWidth && this.screenHeight == newHeight) {
            return;
        }

        final double scale = (double) newWidth / this.screenWidth;

        this.screenWidth = newWidth;
        this.screenHeight = newHeight;

        this.rowSpacing = this.rowSpacing * scale;
        this.distanceSinceLastRow = this.distanceSinceLastRow * scale;

        for (final Brick b : activeBricks) {
            b.setX(b.getX() * scale);
            b.setY(b.getY() * scale);

            final int newSquareSize = (int) (b.getWidth() * scale);
            b.setWidth(newSquareSize);
            b.setHeight(newSquareSize);
        }
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
    private void generateNewRow(final double yPosition) {
        final int columns             = 10;
        final int baseWidth = screenWidth / columns;
        final int r = screenWidth % columns;
        final int maxIndestructible   = columns / 3;
        int indestructibleCount = 0;
        final int currentRowId     = rowsGenerated;
        boolean specialGenerated = false;
        double currentX = 0.0;

        for (int i = 0; i < columns; i++) {
            final int currentBrickWidth = baseWidth + (i < r ? 1 : 0);
            final int type = chooseBrickType(indestructibleCount, maxIndestructible, specialGenerated);
            if (type == BrickFactory.TYPE_INDESTRUCTIBLE) {
                indestructibleCount++;
            }
            if (type == BrickFactory.TYPE_BONUS_MALUS || type == BrickFactory.TYPE_TNT) {
                specialGenerated = true;
            }

            // Bricks are square, so we pass currentBrickWidth as both width and height
            activeBricks.add(BrickFactory.create(
                    currentX,
                    yPosition,
                    type,
                    currentBrickWidth,
                    currentBrickWidth,
                    currentRowId,
                    i
            ));
            currentX += currentBrickWidth;
        }
        rowsGenerated++;
    }

    /**
     * Picks a brick type using weighted random selection.
     * The distribution includes indestructible, double-hit,
     * bonus/malus, tnt, and normal bricks.
     *
     * @param currentIndestructible indestructible bricks already placed in this row
     * @param max                   maximum allowed indestructible bricks per row
     * @param specialGenerated      true if a special brick has already been generated in the row
     * @return brick type: 1, 2, 3, 4 or 5
     */
    private int chooseBrickType(final int currentIndestructible, final int max, final boolean specialGenerated) {
        final int roll = rng.nextInt(ROLL_MAX);
        if (roll < ROLL_THRESHOLD_INDESTRUCTIBLE && currentIndestructible < max) {
            return BrickFactory.TYPE_INDESTRUCTIBLE;
        }
        if (roll < ROLL_THRESHOLD_DOUBLE) {
            return BrickFactory.TYPE_DOUBLE;
        }
        if (roll < ROLL_THRESHOLD_BONUS_MALUS && !specialGenerated) {
            return BrickFactory.TYPE_BONUS_MALUS; //power up
        }
        if (roll < ROLL_THRESHOLD_TNT && !specialGenerated) {
            return BrickFactory.TYPE_TNT; //explosive block
        }
        return BrickFactory.TYPE_NORMAL;
    }

    /**
     * Removes indestructible bricks from any row that no longer contains
     * destructible bricks, effectively clearing the entire row once all
     * hittable bricks have been destroyed.
     *
     * <p>Bricks are grouped by their row identifier; if a group contains
     * no destructible bricks, all remaining bricks in that row
     * (i.e. indestructible ones) are removed from the active list.
     */
    private void removeIndestructibleFromClearedRows() {
        final Map<Integer, List<Brick>> byRow = activeBricks.stream()
                .collect(Collectors.groupingBy(Brick::getRowId));

        byRow.forEach((rowId, bricks) -> {
            final boolean hasDestructible = bricks.stream().anyMatch(b -> !b.isIndestructible());
            if (!hasDestructible) {
                activeBricks.removeAll(bricks);
            }
        });
    }
}
