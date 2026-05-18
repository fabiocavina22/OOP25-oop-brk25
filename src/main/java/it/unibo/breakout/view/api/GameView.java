package it.unibo.breakout.view.api;

import it.unibo.breakout.model.api.GameState;

/**
 * View contract in the MVC architecture for the Breakout game.
 *
 * <p>Implementations are responsible for rendering the game to the screen and
 * switching between the three main screens (menu, game, game-over). This
 * interface does not import any Swing or AWT class so it can be implemented
 * against any UI toolkit.
 *
 * <p>Typical call sequence:
 * <ol>
 *   <li>{@link #showMenu()} — displayed on startup</li>
 *   <li>{@link #showGame()} — called by the controller when the game starts</li>
 *   <li>{@link #render(GameState)} — called every tick by the game loop</li>
 *   <li>{@link #showGameOver(int)} — called when all lives are lost</li>
 * </ol>
 */
public interface GameView {

    /**
     * Switches the display to the main menu screen.
     *
     * <p>Any in-progress game rendering is hidden. The view must be ready
     * to accept a subsequent {@link #showGame()} call once the player
     * presses "Start".
     */
    void showMenu();

    /**
     * Switches the display to the game screen.
     *
     * <p>The game screen must be ready to receive {@link #render(GameState)}
     * calls immediately after this method returns. Implementations should
     * ensure the game panel has keyboard focus so that input events are captured.
     */
    void showGame();

    /**
     * Redraws the game screen to reflect the current state of the world.
     *
     * <p>This method is called by the game loop on every tick. Implementations
     * must be efficient and thread-safe: the game loop may run on a thread
     * other than the UI thread.
     *
     * @param state a read-only snapshot of the current game world; never {@code null}
     */
    void render(GameState state);

    /**
     * Switches the display to the game-over screen, showing the player's final score.
     *
     * <p>From this screen the player must be able to start a new game (e.g. via
     * a "Play Again" button) or return to the main menu.
     *
     * @param finalScore the score achieved in the just-finished game; always &ge; 0
     */
    void showGameOver(int finalScore);
}
