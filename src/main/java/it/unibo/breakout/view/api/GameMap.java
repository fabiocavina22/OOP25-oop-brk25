package it.unibo.breakout.view.api;

public interface GameMap {
    /**
     * turn the window visible
     */
    void showWindow();

    /**
     * checks if the window is visible
     * @return true if the window is visible or fals if it's not
     */
    boolean isFullScreen();

}