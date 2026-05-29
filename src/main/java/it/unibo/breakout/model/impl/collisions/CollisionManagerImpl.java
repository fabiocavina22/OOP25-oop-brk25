package it.unibo.breakout.model.impl.collisions;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.collisions.CollisionDetector;
import it.unibo.breakout.model.api.collisions.CollisionManager;
import it.unibo.breakout.model.impl.PowerUpImpl;


public class CollisionManagerImpl implements CollisionManager {

    private final CollisionDetector detector;
    private int score;
    private int lives = 3;
    private boolean lifeLost = false;
    private final List<PowerUpImpl> activePowerUp = new ArrayList<>();
    private final Random rng = new Random(); 
    private long doublePointsTimer = 0;
    private long paddleLargeTimer = 0;
    private long paddleShortTimer = 0;
    private long freezeBlocksTimer = 0;
    private long halfPointsTimer = 0;
    private long fastBallTimer = 0;
    private long pauseStart = 0;

    private static final long TIME = 8000;
    private int blockHit;
    private boolean padHit;
    private boolean borderHit;

    public CollisionManagerImpl(CollisionDetector detector, int score) {
        this.detector = detector;
        this.score = score;
    }

    @Override
    public void handleCollisions(Ball ball,Paddle paddle, List<Brick> bricks, int gameWidth, int gameHeight, int score){
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks, isFrozen());
        checkBorderCollision(ball, gameWidth, gameHeight, paddle);
    }

    @Override
    public int points(Brick brick){
        if(brick.isIndestructible()){
            return score;
        }
        if(brick.isDestroyed()){
            score += 300;
        }
        else{
            score += 150;
        }
        System.out.println(score);
        return score;
    }

    @Override
    public int getlives(){
        return lives;
    }


    private void loselives(){
        lives--;
        lifeLost = true;
    }

    @Override
    public boolean isLifeLost(){
        boolean result = lifeLost;
        lifeLost = false;
        return result;
    }

    @Override
    public boolean isGameOver(){
        if(lives <= 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int getScore(){
        return score;
    }

    public boolean isFrozen(){
        return freezeBlocksTimer > 0;
    }

    public List<PowerUpImpl> getActivePowerUp(){
        return activePowerUp;
    }

    public void updateTimer(Paddle paddle, Ball ball){
        long now = System.currentTimeMillis();

        if(paddleShortTimer > 0 && now > paddleShortTimer){
            paddle.paddleLarge();
            paddleShortTimer = 0;
            System.out.println("effetto pad piccolo terminato");
        }
        if(doublePointsTimer > 0 && now > doublePointsTimer){
            score /= 2;
            doublePointsTimer = 0;
            System.out.println("effetto punti doppi terminato");
        }
        if(paddleLargeTimer > 0 && now > paddleLargeTimer){
            paddle.paddleShort();
            paddleLargeTimer = 0;
            System.out.println("effetto pad grande terminato");
        }
        if(freezeBlocksTimer > 0 && now > freezeBlocksTimer){
            freezeBlocksTimer = 0;
            System.out.println("effettp blocchi fermi terminato");
        }
        if(halfPointsTimer > 0 && now > halfPointsTimer){
            score *= 2;
            halfPointsTimer = 0;
            System.out.println("effetto punti mezzi terminato");
        }
        if(fastBallTimer > 0 && now > fastBallTimer){
            ball.setVelocityX(ball.getVelocityX() / 1.5);
            ball.setVelocityY(ball.getVelocityY() / 1.5);
            fastBallTimer = 0;
            System.out.println("effetto pallina veloce terminato");
        }
    }

    public void pauseTimer(){
        pauseStart = System.currentTimeMillis();
    }

    public void resumeTimer(){
        if(pauseStart == 0){
            return;
        }
        long pauseDuration = System.currentTimeMillis() - pauseStart;
        if (paddleShortTimer > 0) paddleShortTimer += pauseDuration;
        if (doublePointsTimer > 0) doublePointsTimer += pauseDuration;
        if (paddleLargeTimer > 0) paddleLargeTimer += pauseDuration;
        if (freezeBlocksTimer > 0) freezeBlocksTimer += pauseDuration;
        if (halfPointsTimer > 0) halfPointsTimer += pauseDuration;
        if (fastBallTimer > 0) fastBallTimer += pauseDuration;
        pauseStart = 0;
    }

    public void updatePowerUp(Paddle paddle, Ball ball, int screenHeight){
        for(int i = 0; i < activePowerUp.size(); i++){
            PowerUpImpl powerUp = activePowerUp.get(i);
            powerUp.fall();
            if(powerUp.isOutOfBounds(screenHeight)){
                activePowerUp.remove(i);
                i--;
            }
            else if(powerUp.getX() + 20 > paddle.getX() &&
            powerUp.getX() < paddle.getX() + paddle.getWidth() &&
            powerUp.getY() + 10 >  paddle.getY() &&
            powerUp.getY() < paddle.getY() + paddle.getHeight()){
                switch(powerUp.getType()){
                    case 1:
                        lives++;
                        System.out.println("vita extra");
                        break;
                    case 2:
                        if(paddleShortTimer > 0){
                            paddleShortTimer = System.currentTimeMillis() + TIME;
                        }
                        else{
                            if(paddleLargeTimer > 0){
                                paddle.paddleShort();
                                paddleLargeTimer = 0;
                            }
                            paddle.paddleShort();
                            paddleShortTimer = System.currentTimeMillis() + TIME;
                        }
                        System.out.println("pad piccolo");
                        break;
                    case 3:
                        if(doublePointsTimer > 0){
                            doublePointsTimer = System.currentTimeMillis() + TIME;
                        }
                        else{
                            if(halfPointsTimer > 0){
                                score *= 2;
                                halfPointsTimer = 0;
                            }
                            score *= 2;
                            doublePointsTimer = System.currentTimeMillis() + TIME;
                        }
                        System.out.println("punti doppi");
                        break;
                    case 4:
                        if(paddleLargeTimer > 0){
                            paddleLargeTimer = System.currentTimeMillis() + TIME;
                        }
                        else{
                            if(paddleShortTimer > 0){
                                paddle.paddleLarge();
                                paddleShortTimer = 0;
                            }
                            paddle.paddleLarge();
                            paddleLargeTimer = System.currentTimeMillis() + TIME;
                        }
                        System.out.println("pad grande");
                        break;
                    case 5:
                        freezeBlocksTimer = System.currentTimeMillis() + TIME;
                        System.out.println("blocchi fermi");
                        break;
                    case 6:
                        if(halfPointsTimer > 0){
                            halfPointsTimer = System.currentTimeMillis() + TIME;
                        }
                        else{
                            if(doublePointsTimer > 0){
                                score /= 2;
                                doublePointsTimer = 0;
                            }
                            score /= 2;
                            halfPointsTimer = System.currentTimeMillis() + TIME;
                        }
                        System.out.println("punti dimezzati");
                        break;
                    case 7:
                        ball.setVelocityX(ball.getVelocityX() * 1.5);
                        ball.setVelocityY(ball.getVelocityY() * 1.5);
                        fastBallTimer = System.currentTimeMillis() + TIME;
                        System.out.println("pallina più veloce");
                        break;
                }
                activePowerUp.remove(i);
            }
        }
    }
    @Override
    public void blockHit(Brick bricks){
        this.blockHit = bricks.getType();
    }

    @Override
    public void padHit(){
        this.padHit = true ;
    }

    @Override
    public void borderHit(){
        this.borderHit = true ;
    }

    @Override
    public int getBlockHit() {
        int result = this.blockHit;
        this.blockHit = 0;
        return result;
    }

    @Override
    public boolean getPadHit() {
        boolean result = this.padHit;
        this.padHit = false; // Resetta a false dopo la lettura
        return result;
    }

    @Override
    public boolean getBorderHit() {
        boolean result = this.borderHit;
        this.borderHit = false; // Resetta a false dopo la lettura
        return result;
    }

    private void checkPaddleCollision(Ball ball, Paddle paddle){

        if(detector.isColliding(ball, paddle)){

            double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
            double ballCenter = ball.getX() + ball.getWidth() / 2.0;

            double offset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

            // velocità totale attuale
            double speed = Math.sqrt( ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY() );

            // nuova direzione
            double maxBounceAngle = Math.toRadians(60);

            // angolo finale
            double bounceAngle = offset * maxBounceAngle;

            // nuove velocità
            double newVelocityX = speed * Math.sin(bounceAngle);
            double newVelocityY = -speed * Math.cos(bounceAngle);

            padHit();

            ball.setVelocityX(newVelocityX);
            ball.setVelocityY(newVelocityY);
        }

    }

    private void checkBorderCollision(Ball ball, int gameWidth, int gameHeight, Paddle paddle) {

        if (ball.getX() <= 0) { //SX
            borderHit();
            ball.setPosition(0, ball.getY());
            ball.setVelocityX(Math.abs(ball.getVelocityX()));
        }

        else if (ball.getX() + ball.getWidth() >= gameWidth ) { //DX
            borderHit();
            ball.setPosition(gameWidth - ball.getWidth(), ball.getY());
            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
        }

        if (ball.getY() <= 0) { //SUP
            borderHit();
            ball.setPosition(ball.getX(), 0);
            ball.setVelocityY(Math.abs(ball.getVelocityY()));
        }

        if(ball.getY() > paddle.getY() + paddle.getHeight() + 22 && !lifeLost){
            loselives();
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getHeight());
            ball.setVelocityX(0);
            ball.setVelocityY(0);
        }
    }

    private boolean isAdjacent(Brick brick, Brick adjacentBrick){
        double dx = Math.abs(brick.getX() - adjacentBrick.getX());
        double dy = Math.abs(brick.getY() - adjacentBrick.getY());
        boolean adjacentX = dx < brick.getWidth() * 1.5;
        boolean adjacentY = dy < brick.getHeight() * 1.5;
        boolean notSame = dx > 0 || dy > 0;
        return adjacentX && adjacentY && notSame;
    }

    private void checkBrickCollisions(Ball ball, List<Brick> bricks, boolean frozen) {

        for (Brick brick : bricks) {

            if (detector.isColliding(ball, brick)) {

                // 1. Ricostruiamo dove si trovava la palla nel frame precedente (prima del movimento)
                // Usiamo un delta fittizio basato sulle velocità per capire la traiettoria di provenienza
                double prevBallX = ball.getX() - ball.getVelocityX();
                double prevBallY = ball.getY() - ball.getVelocityY();

                boolean comingFromLeft = (prevBallX + ball.getWidth() <= brick.getX());
                boolean comingFromRight   = (prevBallX >= brick.getX() + brick.getWidth());
                boolean comingFromAbove   = (prevBallY + ball.getHeight() <= brick.getY());
                boolean comingFromBottom   = (prevBallY >= brick.getY() + brick.getHeight());

                // 2. Calcoliamo gli overlap attuali per il riposizionamento
                double overlapLeft   = (ball.getX() + ball.getWidth()) - brick.getX();
                double overlapRight  = (brick.getX() + brick.getWidth()) - ball.getX();
                double overlapTop    = (ball.getY() + ball.getHeight()) - brick.getY();
                double overlapBottom = (brick.getY() + brick.getHeight()) - ball.getY();

                // 4. Risoluzione tramite cronologia delle posizioni
                if (comingFromLeft ) {
                    ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                    ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                }
                else if (comingFromRight) {
                    ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                    ball.setVelocityX(Math.abs(ball.getVelocityX()));
                }
                else if (comingFromAbove) {
                    ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                    ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                }
                else if (comingFromBottom) {
                    ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                    ball.setVelocityY(Math.abs(ball.getVelocityY()));
                }
                else {
                    // Fallback geometrico d'emergenza se la palla si è materializzata dentro da un angolo perfetto
                    double minOverlapX = Math.min(overlapLeft, overlapRight);
                    double minOverlapY = Math.min(overlapTop, overlapBottom);

                    if (minOverlapX < minOverlapY) {
                        if (overlapLeft < overlapRight) {
                            ball.setPosition(brick.getX() - ball.getWidth(), ball.getY());
                            ball.setVelocityX(-Math.abs(ball.getVelocityX()));
                        } else {
                            ball.setPosition(brick.getX() + brick.getWidth(), ball.getY());
                            ball.setVelocityX(Math.abs(ball.getVelocityX()));
                        }
                    } else {
                        if (overlapTop < overlapBottom) {
                            ball.setPosition(ball.getX(), brick.getY() - ball.getHeight());
                            ball.setVelocityY(-Math.abs(ball.getVelocityY()));
                        } else {
                            ball.setPosition(ball.getX(), brick.getY() + brick.getHeight());
                            ball.setVelocityY(Math.abs(ball.getVelocityY()));
                        }
                    }
                }

                // distruzione brick
                    brick.hit();
                    points(brick);

                    //blocco bomba
                    if(brick.getType() == 5){
                        System.out.println("bomba a x=" + brick.getX() + "y=" + brick.getY());
                        int count = 0;
                        for(int i = 0; i < bricks.size(); i++){
                            Brick adjacentBrick = bricks.get(i);
                            if(adjacentBrick != brick && isAdjacent(brick, adjacentBrick)){
                                count++;
                                System.out.println("adiacente a x=" + adjacentBrick.getX() + "y=" + adjacentBrick.getY());
                                adjacentBrick.hit();
                                System.out.println("tipo:" + adjacentBrick.getType() + "distrutto" + adjacentBrick.isDestroyed());
                                points(adjacentBrick);
                                if(adjacentBrick.isDestroyed() && adjacentBrick.getType() == 4){
                                    int powerUpType = rng.nextInt(7) + 1;
                                    activePowerUp.add(new PowerUpImpl(
                                        adjacentBrick.getX() + adjacentBrick.getWidth() / 2.0,
                                        adjacentBrick.getY(),
                                        powerUpType
                                    ));
                                }
                            }
                        }
                        System.out.println("numero di blocchi adiacenti: " + count);
                    }

                    //blocco powerUp
                    if(brick.isDestroyed() && brick.getType() == 4){
                        int powerUpType = rng.nextInt(7) + 1;
                        activePowerUp.add(new PowerUpImpl(
                            brick.getX() + brick.getWidth() / 2.0,
                            brick.getY(),
                            powerUpType
                        ));
                    }
                blockHit(brick);
                brick.hit();
                points(brick);

                break; // evita multi-collisione nello stesso frame
            }
        }
    }
}
