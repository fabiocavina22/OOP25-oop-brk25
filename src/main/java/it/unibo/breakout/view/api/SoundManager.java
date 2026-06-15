package it.unibo.breakout.view.api;

/**
 * Manages the playback of sound effects in the game.
 */
public interface SoundManager {

    /**
     * Plays the sound contained in the given file.
     *
     * @param fileName the name of the file to play
     */
    void playSound(String fileName);

}
