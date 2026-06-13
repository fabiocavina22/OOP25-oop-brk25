package it.unibo.breakout.view.impl;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

import it.unibo.breakout.view.api.SoundManager;

public class SoundManagerImpl implements SoundManager {

    @Override
    public void playSound (final String fileName){
        try {

           final URL soundURL = SoundManager.class.getResource("/it/unibo/breakout/sounds/" + fileName);

            if (soundURL != null) {

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
                        } catch (Exception e) {

                        }
                    }
                });
                // -------------------------------------------------------------

                clip.open(audioIn);

                // Impostiamo esplicitamente che non deve ripetere il suono
                clip.loop(0);

                clip.start();
            }

            else {
                System.err.println("Errore: Impossibile trovare il file audio - " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la riproduzione del suono: " + e.getMessage());
        }
    }
}


