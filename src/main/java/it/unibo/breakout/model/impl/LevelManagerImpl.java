package it.unibo.breakout.model.impl;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Brick;
import java.util.ArrayList;
import java.util.List;

public class LevelManagerImpl implements LevelManager {


        private final List<Brick> activeBricks;
        private final int screenWidth;
        private final int brickWidth;
        private final int brickHeight;

        public LevelManagerImpl(int screenWidth, int brickWidth, int brickHeight) {
            this.activeBricks = new ArrayList<>();
            this.screenWidth = screenWidth;
            this.brickWidth = brickWidth;
            this.brickHeight = brickHeight;

            generateNewRow(0);
        }

        @Override
        public List<Brick> getActiveBricks() {
            return this.activeBricks;
        }

        //generazione di una riga di mattoni

        private void generateNewRow(double yPosition) {
            int columns = screenWidth / brickWidth;
            for (int i = 0; i < columns; i++) {
                double xPosition = i * brickWidth;
                Brick newBrick = new BrickImpl(xPosition, yPosition, 1, brickWidth, brickHeight);
                activeBricks.add(newBrick);
            }
        }
}
