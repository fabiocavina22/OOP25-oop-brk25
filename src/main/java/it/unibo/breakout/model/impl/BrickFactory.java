package it.unibo.breakout.model.impl;

import it.unibo.breakout.model.api.Brick;

/**
 * Factory class responsible for creating Brick instances.
 *
 * <p>This class implements the <b>Factory Method</b> pattern:
 * the codebase, all brick creation is centralised here.
 */
public final class BrickFactory {

    /** Brick type constant – normal brick (1 hit to destroy). */
    public static final int TYPE_NORMAL        = 1;

    /** Brick type constant – double-hit brick (2 hits to destroy). */
    public static final int TYPE_DOUBLE        = 2;

    /** Brick type constant – indestructible brick. */
    public static final int TYPE_INDESTRUCTIBLE = 3;

    /** Brick type constant – bonus-malus brick (1 hit, drops a power-up). */
    public static final int TYPE_BONUS_MALUS     = 4;

    /** Brick type constant – TNT / explosive brick (1 hit, chain explosion). */
    public static final int TYPE_TNT           = 5;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BrickFactory() {
        // Prevents instantiation
    }

    /**
     * Creates a {@link Brick} of the requested type.
     *
     * @param x        horizontal position in pixels
     * @param y        vertical position in pixels
     * @param type     brick type (use the {@code TYPE_*} constants)
     * @param width    brick width in pixels
     * @param height   brick height in pixels
     * @param rowId    row index used for row-clearing logic
     * @param colIndex column index inside the row
     * @return a new {@link BrickImpl} configured for the given type
     * @throws IllegalArgumentException if the type is not recognised
     */
    public static Brick create(
            final double x,
            final double y,
            final int type,
            final int width,
            final int height,
            final int rowId,
            final int colIndex
    ) {
        return switch (type) {
            case TYPE_NORMAL         -> new BrickImpl(x, y, TYPE_NORMAL, width, height, rowId, colIndex);
            case TYPE_DOUBLE         -> new BrickImpl(x, y, TYPE_DOUBLE, width, height, rowId, colIndex);
            case TYPE_INDESTRUCTIBLE -> new BrickImpl(x, y, TYPE_INDESTRUCTIBLE, width, height, rowId, colIndex);
            case TYPE_BONUS_MALUS    -> new BrickImpl(x, y, TYPE_BONUS_MALUS, width, height, rowId, colIndex);
            case TYPE_TNT            -> new BrickImpl(x, y, TYPE_TNT, width, height, rowId, colIndex);
            default -> throw new IllegalArgumentException("Unknown brick type: " + type);
        };
    }
}
