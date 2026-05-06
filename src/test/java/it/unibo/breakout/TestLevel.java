package it.unibo.breakout;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.impl.LevelManagerImpl;

import java.util.List;

public class TestLevel {

    public static void main(String[] args) {
        // esempio: schermo largo 500 e mattoni larghi 100
        int screenW = 500;
        int brickW = 100;
        int brickH = 30;

        System.out.println("--- Inizio Test LevelManager ---");

        LevelManager manager = new LevelManagerImpl(screenW, brickW, brickH);
        List<Brick> bricks = manager.getActiveBricks();

        // Verifica 1: Numero di mattoni
        // Se schermo è 500 e mattone 100, ci devono essere 5 mattoni
        System.out.println("Numero di mattoni generati: " + bricks.size());
        if (bricks.size() == 5) {
            System.out.println("OK: Conteggio mattoni corretto.");
        } else {
            System.out.println("ERRORE: Conteggio errato!");
        }

        // verifica posizione x
        System.out.println("\nCoordinate dei mattoni generati:");
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            System.out.println("Mattone " + i + " -> X: " + b.getX() + ", Y: " + b.getY());
        }

        System.out.println("\n--- Fine Test ---");
    }
}