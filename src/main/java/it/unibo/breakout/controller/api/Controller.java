package it.unibo.breakout.controller.api;

/**
 * Input contract between the view layer and the game controller.
 *
 * <p>The view calls these methods in response to keyboard and UI events;
 * the controller reacts by updating the model accordingly. The interface
 * deliberately knows nothing about Swing so it stays framework-agnostic.
 */
public interface Controller {

    /**
     * Notifies the controller that the left-arrow (or equivalent) key has been pressed.
     *
     * <p>Typically starts continuous leftward movement of the paddle.
     */
    void onLeftPressed();

    /**
     * Notifies the controller that the right-arrow (or equivalent) key has been pressed.
     *
     * <p>Typically starts continuous rightward movement of the paddle.
     */
    void onRightPressed();

    /**
     * Notifies the controller that the left-arrow (or equivalent) key has been released.
     *
     * <p>Stops leftward paddle movement.
     */
    void onLeftReleased();

    /**
     * Notifies the controller that the right-arrow (or equivalent) key has been released.
     *
     * <p>Stops rightward paddle movement.
     */
    void onRightReleased();

    /**
     * Notifies the controller that the player wants to toggle the pause state.
     *
     * <p>If the game is running it should be paused; if it is paused it should resume.
     */
    void onPauseToggle();

    /**
     * Notifies the controller that the player wants to start (or restart) a new game.
     *
     * <p>Called both from the main-menu "Start" button and from the game-over
     * "Play Again" button.
     */
    void onNewGame();
}
