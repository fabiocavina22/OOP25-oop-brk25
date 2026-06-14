package it.unibo.breakout.view.impl;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

import it.unibo.breakout.model.api.Ball;
import it.unibo.breakout.model.api.Brick;
import it.unibo.breakout.model.api.LevelManager;
import it.unibo.breakout.model.api.Paddle;
import it.unibo.breakout.model.impl.PowerUpImpl;
import java.util.List;
import java.util.ArrayList;


public final class MainPanel extends JPanel {

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager;
    private List<PowerUpImpl> activePowerUp = new ArrayList<>();
    final private Image brickImage1;
    final private Image brickImage2;
    final private Image brickImage3;
    final private Image brickImage4;
    final private Image brickImage5;
    final private Image gameBackground;



    public MainPanel(final Paddle paddle, final LevelManager levelManager, final  Ball ball) {

        this.paddle = paddle;
        this.ball = ball;
        this.levelManager = levelManager;

        /* images loaders */
        brickImage1 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick1.jpg")).getImage();
        brickImage2 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick2.jpg")).getImage();
        brickImage3 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick3.jpg")).getImage();
        brickImage4 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick4.jpg")).getImage();
        brickImage5 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick5.jpg")).getImage();

        gameBackground = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/gameBackground.jpg")).getImage();

        final Border rightBorder = BorderFactory.createMatteBorder(
                10, 0, 10, 0, Color.BLACK
        );

        final Border padding = BorderFactory.createEmptyBorder(
                10, 10, 10, 10
        );

        setBorder(BorderFactory.createCompoundBorder(rightBorder, padding));
    }

    // --- CUSTOM BACKGROUND ---

    public int getGameWidth() {

        return getWidth();

    }

    public int getGameHeight() {

        return getHeight();

    }

    public void setPowerUp(final List<PowerUpImpl> powerUp){
        this.activePowerUp = powerUp;
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);

        /*
        * Background
        **/
        if (gameBackground != null) {
           g.drawImage(gameBackground, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        /* Paddle */
        g.setColor(Color.BLACK);

        g.fillRect(
                (int) paddle.getX(),
                (int) paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight()
        );

        /*
        * Bricks
        **/
        for (final Brick b : levelManager.getActiveBricks()) {

            final Image img;
            if (b.isIndestructible()) {
                img = brickImage3;
            } else if (b.getType() == 2) {
                img = brickImage2;
            } else if (b.getType() == 1) {
                img = brickImage1;
            } else if (b.getType() == 4) {
                img = brickImage4;
            } else {
                img = brickImage5 ;
            }

            g.drawImage(img,
                    (int) b.getX() + 1,
                    (int) b.getY() + 1,
                    b.getWidth() - 2,
                    b.getHeight() - 2,
                    null);

        }
            /*
            * Ball
            **/
            g.setColor(Color.RED);

            g.fillOval(
                (int) ball.getX(),
                (int) ball.getY(),
                (int) ball.getRadius() * 2,
                (int) ball.getRadius() * 2
            );

            /*
            *Power Up
            **/
            for(int i = 0; i < activePowerUp.size(); i++){
                final PowerUpImpl p = activePowerUp.get(i);
                final int type = p.getType();
                if(type ==1 || type == 3 || type == 4 || type == 5){
                    g.setColor(Color.GREEN);
                }
                else{
                    g.setColor(new Color(128, 0, 128));
                }
                g.fillOval((int) p.getX(), (int) p.getY(), 15, 15);
            }
        }
}