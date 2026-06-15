package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.Brick;


/**
 * Implementation of the Brick interface.
 * Represents a brick in the breakout game with life, position, dimensions, and type.
 */
public class BrickImpl implements Brick {

    private int life;
    private final boolean indestructible;
    private double x, y;
    private int width;
    private int height;
    private final int rowId;
    private int type;
    private final int colIndex;


    /**
     * Constructs a brick with the given properties.
     *
     * @param x        horizontal position in pixels
     * @param y        vertical position in pixels
     * @param type     brick type: 1 = normal (1 hit), 2 = resistant (2 hits),
     *                 3 = indestructible, 4 or 5 = special (1 hit)
     * @param width    width in pixels
     * @param height   height in pixels
     * @param rowId    identifier of the row this brick belongs to
     * @param colIndex index of the column this brick belongs to
     */
    public BrickImpl(
            final double x,
            final double y,
            final int type,
            final int width,
            final int height,
            final int rowId,
            final int colIndex
    ) {
        this.x = x;
        this.y = y;
        this.rowId = rowId;
        this.type = type;
        if (type == BrickFactory.TYPE_INDESTRUCTIBLE) {
            this.indestructible = true;
            this.life = 1;
        } else {
            this.indestructible = false;
            if (type == BrickFactory.TYPE_BONUS_MALUS || type == BrickFactory.TYPE_TNT) {
                this.life = 1;
            } else {
                this.life = type;
            }
        }
        this.width = width;
        this.height = height;
        this.colIndex = colIndex;
    }

    /** Returns the identifier of the row this brick belongs to. */
    @Override
    public int getRowId() {
        return this.rowId;
    }

    /** Moves the brick down by the given amount of pixels. */
    @Override
    public void moveDown(final double amount) {
        this.y += amount;
    }

    /** Decreases life by one if the brick is not indestructible. */
    @Override
    public void hit() {
        if (!indestructible) {
            life--;
        }
        if (type == BrickFactory.TYPE_DOUBLE) {
            type = BrickFactory.TYPE_NORMAL;
        }
    }

    /** Returns true if the brick has no life left and is not indestructible. */
    @Override
    public boolean isDestroyed() {
        return !indestructible && life <= 0;
    }

    /** Returns the brick's X position. */
    @Override
    public double getX() {
        return this.x;
    }

    /** Returns the brick's Y position. */
    @Override
    public double getY() {
        return this.y;
    }

    /** Returns true if the brick cannot be destroyed. */
    @Override
    public boolean isIndestructible() {
        return this.indestructible;
    }

    /** Returns the width of the brick in pixels. */
    @Override
    public int getWidth() {
        return this.width;
    }

    /** Returns the height of the brick in pixels. */
    @Override
    public int getHeight() {
        return this.height;
    }

    /** Returns the current life of the brick (remaining hits before destruction). */
    @Override
    public int getLife() {
        return this.life;
    }

    /** Returns the current type of the brick. */
    @Override
    public int getType() {
        return this.type;
    }

    /** Sets the horizontal position (X) of the brick in pixels. */
    @Override
    public void setX(final double x) {
        this.x = x;
    }

    /** Sets the width of the brick in pixels. */
    @Override
    public void setWidth(final int width) {
        this.width = width;
    }

    /** Sets the vertical position (Y) of the brick in pixels. */
    @Override
    public void setY(final double y) {
        this.y = y;
    }

    /** Sets the height of the brick in pixels. */
    @Override
    public void setHeight(final int height) {
        this.height = height;
    }

    /** Returns the column index this brick belongs to. */
    @Override
    public int getColIndex() {
        return colIndex;
    }
}
