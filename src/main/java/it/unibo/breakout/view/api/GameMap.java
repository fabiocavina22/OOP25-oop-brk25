package it.unibo.breakout.view.api;

/**
 * Represents the game window that displays the Breakout game.
 */
public interface GameMap {
    /**
     * Turns the window visible.
     */
    void showWindow();

    /**
     * Checks if the window is visible.
     *
     * @return true if the window is visible or false if it's not
     */
    boolean isFullScreen();

}
