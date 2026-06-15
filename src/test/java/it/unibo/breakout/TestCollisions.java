package it.unibo.breakout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.LivesManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;
import it.unibo.breakout.model.api.collisions.Collidable;
import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.impl.PaddleImpl;
import it.unibo.breakout.model.impl.PowerUpImpl;
import it.unibo.breakout.model.impl.collisions.CollisionManagerImpl;

/**
 * Unit tests for the collision manager: object hits, scoring and ball bounce physics.
 */
class TestCollisions {

    private static final double DELTA = 0.0001;
    private static final int BLOCK_TYPE = 5;
    private static final int DESTROY_SCORE = 300;
    private static final int HIT_SCORE = 150;
    private static final double MULTIPLIER = 2.0;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final double EXPECTED_SPEED = 5.0;

    /* collisions with objects */

    @Test
    void padGetsHitOnlyOneTime() {
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, null);
        assertFalse(cm.isPadHit());
        cm.padHit();
        assertTrue(cm.isPadHit());
        assertFalse(cm.isPadHit());
    }

    @Test
    void borderGetsHitOnlyOneTime() {
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, null);
        assertFalse(cm.isBorderHit());
        cm.borderHit();
        assertTrue(cm.isBorderHit());
        assertFalse(cm.isBorderHit());
    }

    @Test
    void blockGetsHitOnlyOneTimeAndSavesTheType() {
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, null);
        assertEquals(0, cm.typeOfBlockHit());
        cm.blockHit(new FakeBrick(BLOCK_TYPE, false, false));
        assertEquals(BLOCK_TYPE, cm.typeOfBlockHit());
        assertEquals(0, cm.typeOfBlockHit());
    }

    /* score */

    @Test
    void scoreReturnsTheInitialScore() {
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 100, null);
        assertEquals(100, cm.getScore());
    }

    @Test
    void ifTheBlockIsIndestructibleTheScoreDoesentChange() {
        final FakePowerUpManager pum = new FakePowerUpManager();
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 100, pum);
        final FakeBrick brick = new FakeBrick(1, true, false);
        final int result = cm.points(brick);
        assertEquals(100, result);
        assertEquals(100, cm.getScore());
    }

    @Test
    void pointsAdd300IfTheBlockGetsDestroyed() {
        final FakePowerUpManager pum = new FakePowerUpManager();
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, pum);
        final FakeBrick brick = new FakeBrick(1, false, true);
        cm.points(brick);
        assertEquals(DESTROY_SCORE, cm.getScore());
    }

    @Test
    void pointsAdd150IfTheBlockGetsHitButNotDestroyed() {
        final FakePowerUpManager pum = new FakePowerUpManager();
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, pum);
        final FakeBrick brick = new FakeBrick(1, false, false);
        cm.points(brick);
        assertEquals(HIT_SCORE, cm.getScore());
    }

    @Test
    void pointsAppliesTheMultiplyer() {
        final FakePowerUpManager pum = new FakePowerUpManager();
        pum.setScoreMultiplier(MULTIPLIER);
        final CollisionManagerImpl cm = new CollisionManagerImpl(null, 0, pum);
        final FakeBrick brick = new FakeBrick(1, false, true);
        cm.points(brick);
        assertEquals((int) (DESTROY_SCORE * MULTIPLIER), cm.getScore());
    }

    /* ball's physics */

    void assertBounce(final double ballX, final double expectedAngle) {
        final FakeBall ball = new FakeBall(ballX, 490, 10, 10, 3.0, 4.0);
        final PaddleImpl paddle = new PaddleImpl(100, 500, 100, 20, 5);
        final CollisionManagerImpl cm =
                new CollisionManagerImpl(new FakeCollisionDetector(), 0, new FakePowerUpManager());

        cm.handleCollisions(ball, paddle, List.of(), SCREEN_WIDTH, SCREEN_HEIGHT, 0);

        final double modulo = Math.hypot(ball.getVelocityX(), ball.getVelocityY());
        final double angolo = Math.atan2(ball.getVelocityX(), -ball.getVelocityY());
        assertEquals(EXPECTED_SPEED, modulo, DELTA);
        assertEquals(expectedAngle, angolo, DELTA);
    }
    /* fake classes used only to exercise the collision manager */

    private static final class FakeCollisionDetector implements CollisionDetector {
        @Override
        public boolean isColliding(final Collidable a, final Collidable b) {
            return true;
        }
    }

    private static final class FakeBall implements Ball {

        private double x;
        private double y;
        private final int width;
        private final int height;
        private double velocityX;
        private double velocityY;

        FakeBall(final double x, final double y, final int width, final int height,
                 final double velocityX, final double velocityY) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        /* stub methods, needed only to compile */

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public double getVelocityX() {
            return velocityX;
        }

        @Override
        public double getVelocityY() {
            return velocityY;
        }

        @Override
        public void setVelocityX(final double vx) {
            this.velocityX = vx;
        }

        @Override
        public void setVelocityY(final double vy) {
            this.velocityY = vy;
        }

        @Override
        public void setPosition(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double getRadius() {
            return 0;
        }

        @Override
        public void move() {
        }

        @Override
        public void bounceX() {
            this.velocityX = -this.velocityX;
        }

        @Override
        public void bounceY() {
            this.velocityY = -this.velocityY;
        }

        @Override
        public boolean isOutOfBounds(final double fieldHeight) {
            return false;
        }

        @Override
        public void updateDimensions(final int panelWidth, final int panelHeight, final Paddle paddle) {
        }
    }

    private static final class FakePowerUpManager implements PowerUpManager {

        private double scoreMultiplier = 1.0;

        void setScoreMultiplier(final double multiplier) {
            this.scoreMultiplier = multiplier;
        }

        @Override
        public double getScoreMultiplier() {
            return scoreMultiplier;
        }

        /* stub methods, needed only to compile */

        @Override
        public long getDoublePointsTimer() {
            return 0;
        }

        @Override
        public long getPaddleLargeTimer() {
            return 0;
        }

        @Override
        public long getPaddleShortTimer() {
            return 0;
        }

        @Override
        public long getFreezeBlocksTimer() {
            return 0;
        }

        @Override
        public long getHalfPointsTimer() {
            return 0;
        }

        @Override
        public long getFastBallTimer() {
            return 0;
        }

        @Override
        public boolean isFrozen() {
            return false;
        }

        @Override
        public void updatePowerUp(final Paddle paddle, final Ball ball, final int screenHeight, final LivesManager livesManager) {
        }

        @Override
        public void updateTimer(final Paddle paddle, final Ball ball) {
        }

        @Override
        public List<PowerUpImpl> getActivePowerUp() {
            return List.of();
        }

        @Override
        public int getFastBallFrames() {
            return 0;
        }

        @Override
        public void resetFastBallFrames() {
        }

        @Override
        public void spawnPowerUp(final double x, final double y) {
        }
    }

    private static final class FakeBrick implements Brick {

        private final int type;
        private final boolean indestructible;
        private final boolean destroyed;

        FakeBrick(final int type, final boolean indestructible, final boolean destroyed) {
            this.type = type;
            this.indestructible = indestructible;
            this.destroyed = destroyed;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public boolean isIndestructible() {
            return indestructible;
        }

        @Override
        public boolean isDestroyed() {
            return destroyed;
        }

        /* stub methods, needed only to compile */

        @Override
        public void hit() {
        }

        @Override
        public void moveDown(final double amount) {
        }

        @Override
        public int getLife() {
            return 0;
        }

        @Override
        public int getRowId() {
            return 0;
        }

        @Override
        public void setX(final double x) {
        }

        @Override
        public void setWidth(final int width) {
        }

        @Override
        public void setY(final double y) {
        }

        @Override
        public void setHeight(final int height) {
        }

        @Override
        public int getColIndex() {
            return 0;
        }

        @Override
        public double getX() {
            return 0;
        }

        @Override
        public double getY() {
            return 0;
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }
    }
}
