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


public class MainPanel extends JPanel {

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager;
    private List<PowerUpImpl> activePowerUp = new ArrayList<>();
    private Image brickImage1;
    private Image brickImage2;
    private Image brickImage3;
    private Image brickImage4;
    private Image brickImage5;



    public MainPanel(Paddle paddle, LevelManager levelManager, Ball ball) {

        this.paddle = paddle;
        this.ball = ball;
        this.levelManager = levelManager;

        brickImage1 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick1.jpg")).getImage();
        brickImage2 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick2.jpg")).getImage();
        brickImage3 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick3.jpg")).getImage();
        brickImage4 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick4.jpg")).getImage();
        brickImage5 = new ImageIcon(getClass().getResource("/it/unibo/breakout/images/brick5.jpg")).getImage();

        setBackground(Color.WHITE);

        Border rightBorder = BorderFactory.createMatteBorder(
                10, 0, 10, 0, Color.BLACK
        );

        Border padding = BorderFactory.createEmptyBorder(
                10, 10, 10, 10
        );

        setBorder(BorderFactory.createCompoundBorder(rightBorder, padding));
    }

    public int getGameWidth() {

        return getWidth();

    }

    public int getGameHeight() {

        return getHeight();

    }
    
    public void setPowerUp(List<PowerUpImpl> powerUp){
        this.activePowerUp = powerUp;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setColor(Color.BLACK);

        g.fillRect(
                (int) paddle.getX(),
                (int) paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight()
        );

        // MATTONI (DAL LEVEL MANAGER)
        for (Brick b : levelManager.getActiveBricks()) {

            Image img;
            if (b.isIndestructible()) {
                img = brickImage3;
            } else if (b.getType() == 2) {
                img = brickImage2;
            } else if (b.getType() == 1) {
                img = brickImage1;
            } else if (b.getType() == 4) {
                img = brickImage4;
            } else img = brickImage5 ;

            g.drawImage(img,
                    (int) b.getX() + 1,
                    (int) b.getY() + 1,
                    b.getWidth() - 2,
                    b.getHeight() - 2,
                    null);

            /*
            g.setColor(Color.BLACK);
            g.drawRect(
                    (int) b.getX(),
                    (int) b.getY(),
                    b.getWidth(),
                    b.getHeight()
            );
            */
        }

            g.setColor(Color.RED);

            g.fillOval(
                (int) ball.getX(),
                (int) ball.getY(),
                (int) ball.getRadius() * 2,
                (int) ball.getRadius() * 2
            );

            for(int i = 0; i < activePowerUp.size(); i++){
                PowerUpImpl p = activePowerUp.get(i);
                int type = p.getType();
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