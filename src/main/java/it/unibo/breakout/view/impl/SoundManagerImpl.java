package it.unibo.breakout.view.impl;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.breakout.view.api.SoundManager;

/**
 * Implementation of {@link SoundManager} that plays audio clips
 * loaded from the classpath.
 */
public final class SoundManagerImpl implements SoundManager {

    private static final Logger LOGGER = Logger.getLogger(SoundManagerImpl.class.getName());

    @Override
    public void playSound(final String fileName) {
        final URL soundURL = SoundManager.class.getResource("/it/unibo/breakout/sounds/" + fileName);

        if (soundURL == null) {
            LOGGER.log(Level.WARNING, "Unable to find audio file - {0}", fileName);
            return;
        }

        try {

            final AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
        /*
            * create a clip
            **/
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close();
                    try {
                        audioIn.close();
                    } catch (final IOException e) {
                        LOGGER.log(Level.WARNING, "Error closing the audio stream", e);
                    }
                }
            });
            clip.open(audioIn);
            clip.loop(0);
            clip.start();
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            LOGGER.log(Level.WARNING, "Error while playing the sound: " + fileName, e);
        }
    }
}


