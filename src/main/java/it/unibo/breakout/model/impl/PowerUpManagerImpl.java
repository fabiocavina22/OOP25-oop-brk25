package it.unibo.breakout.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.LivesManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.api.PowerUpManager;

public class PowerUpManagerImpl implements PowerUpManager{
    private final List<PowerUpImpl> activePowerUp = new ArrayList<>();
    private int doublePointsFrames = 0;
    private int paddleLargeFrames = 0;
    private int paddleShortFrames = 0;
    private int freezeBlocksFrames = 0;
    private int halfPointsFrames = 0;
    private int fastBallFrames = 0;
    private LivesManager livesManager;
    private Random rng = new Random();

    private double scoreMultiplier = 1.0;

    private static final int EFFECT_FRAMES = 500;

    @Override
    public double getScoreMultiplier(){
        return this.scoreMultiplier;
    }

    @Override
    public int getFastBallFrames(){
        return this.fastBallFrames;
    }

    public long getDoublePointsTimer() { return doublePointsFrames; }
    public long getPaddleLargeTimer() { return paddleLargeFrames; }
    public long getPaddleShortTimer() { return paddleShortFrames; }
    public long getFreezeBlocksTimer() { return freezeBlocksFrames; }
    public long getHalfPointsTimer() { return halfPointsFrames; }
    public long getFastBallTimer() { return fastBallFrames; }

    @Override
    public boolean isFrozen(){
        return freezeBlocksFrames > 0;
    }

    @Override
    public void resetFastBallFrames(){
        this.fastBallFrames = 0 ;
    }

    @Override
    public List<PowerUpImpl> getActivePowerUp() {
        return new ArrayList<>(this.activePowerUp);
    }

    public void spawnPowerUp(double x , double y){
        int powerUpType = rng.nextInt(7) + 1;

        activePowerUp.add(new PowerUpImpl(x, y, powerUpType));
    }

    public void setLivesManager(LivesManager livesManager){
        this.livesManager = livesManager;
    }

    public void updateTimer(Paddle paddle, Ball ball){
        if(doublePointsFrames > 0){
            doublePointsFrames--;
            if(doublePointsFrames == 0){
                scoreMultiplier = 1.0;
            }
        }
        if(paddleShortFrames > 0){
            paddleShortFrames--;
            if(paddleShortFrames == 0){
                paddle.paddleLarge();
            }
        }
        if(paddleLargeFrames > 0){
            paddleLargeFrames--;
            if(paddleLargeFrames == 0){
                paddle.paddleShort();
            }
        }
        if(freezeBlocksFrames > 0){
            freezeBlocksFrames--;
            if(freezeBlocksFrames == 0){
            }
        }
        if(halfPointsFrames > 0){
            halfPointsFrames--;
            if(halfPointsFrames == 0){
                scoreMultiplier = 1.0;
            }
        }
        if(fastBallFrames > 0){
            fastBallFrames--;
            if(fastBallFrames == 0){
                ball.setVelocityX(ball.getVelocityX() / 1.5);
                ball.setVelocityY(ball.getVelocityY() / 1.5);
            }
        }
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
                        livesManager.addLife() ;
                        break;
                    case 2:
                        if(paddleShortFrames > 0){
                            paddleShortFrames = EFFECT_FRAMES;
                        }
                        else{
                            if(paddleLargeFrames > 0){
                                paddle.paddleShort();
                                paddleLargeFrames = 0;
                            }
                            paddle.paddleShort();
                            paddleShortFrames = EFFECT_FRAMES;
                        }
                        break;
                    case 3:
                        if(doublePointsFrames > 0){
                            doublePointsFrames = EFFECT_FRAMES;
                        }
                        else{
                            if(halfPointsFrames > 0){
                                scoreMultiplier = 1.0;
                                halfPointsFrames = 0;
                            }
                            scoreMultiplier = 2.0;
                            doublePointsFrames = EFFECT_FRAMES;
                        }
                        break;
                    case 4:
                        if(paddleLargeFrames > 0){
                            paddleLargeFrames = EFFECT_FRAMES;
                        }
                        else{
                            if(paddleShortFrames > 0){
                                paddle.paddleLarge();
                                paddleShortFrames = 0;
                            }
                            paddle.paddleLarge();
                            paddleLargeFrames = EFFECT_FRAMES;
                        }
                        break;
                    case 5:
                        freezeBlocksFrames = EFFECT_FRAMES;
                        break;
                    case 6:
                        if(halfPointsFrames > 0){
                            halfPointsFrames = EFFECT_FRAMES;
                        }
                        else{
                            if(doublePointsFrames > 0){
                                scoreMultiplier = 1.0;
                                doublePointsFrames = 0;
                            }
                            scoreMultiplier = 0.5;
                            halfPointsFrames = EFFECT_FRAMES;
                        }
                        break;
                    case 7:
                        ball.setVelocityX(ball.getVelocityX() * 1.5);
                        ball.setVelocityY(ball.getVelocityY() * 1.5);
                        fastBallFrames = EFFECT_FRAMES;
                        break;
                }
                activePowerUp.remove(i);
            }
        }
    }

}
