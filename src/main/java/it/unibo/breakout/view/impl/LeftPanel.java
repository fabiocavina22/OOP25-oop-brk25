package it.unibo.breakout.view.impl;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.net.URL;

public class LeftPanel extends JPanel {

        ImageIcon iconW, iconA, iconS, iconD;

        ImageIcon iconWPressed, iconAPressed, iconSPressed, iconDPressed;

        ImageIcon iconHeart;

        private final JLabel lblW = new JLabel();
        private final JLabel lblA = new JLabel();
        private final JLabel lblS = new JLabel();
        private final JLabel lblD = new JLabel();

        private final JLabel lblLives = new JLabel("3");
        private final JLabel lblScore = new JLabel("0");

        private final int[] effectTypes = new int[7];
        private final long[] effectExpires = new long[7];
        private final JLabel[] effectLabels = new JLabel[7];
        private int effectCount = 0;
        private int lifeBonus = 0;
        private JPanel effectsPanel;

        private long effectPauseStart = 0;
        private final ImageIcon[] effectIcons = new ImageIcon[8];
        private static final int LIFE_BONUS_FRAMES = 120;

        public LeftPanel() {


                setBackground(Color.WHITE);

                Border RightBorder = BorderFactory.createMatteBorder(
                0, // top
                0, // left
                0, // bottom
                10, // right
                Color.BLACK
                );

                Border padding = BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
                );

                setBorder(
                BorderFactory.createCompoundBorder(
                        RightBorder,
                        padding
                )
                );

                loadImages();

                effectsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                effectsPanel.setBackground(Color.WHITE);

                lblW.setIcon(iconW);
                lblA.setIcon(iconA);
                lblS.setIcon(iconS);
                lblD.setIcon(iconD);

                Font retroFont = new Font("Courier New", Font.BOLD, 18);

                JPanel hudContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                hudContainer.setBackground(Color.WHITE);

                lblLives.setFont(retroFont);
                JLabel lblHeartImg = new JLabel(iconHeart);

                /* Score's rectangle */
                lblScore.setFont(retroFont);
                lblScore.setForeground(Color.WHITE);
                lblScore.setBackground(Color.BLACK);
                lblScore.setOpaque(true);
                lblScore.setHorizontalAlignment(SwingConstants.CENTER);
                lblScore.setPreferredSize(new Dimension(85, 35));
                lblScore.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

                hudContainer.add(lblLives);
                hudContainer.add(lblHeartImg);
                hudContainer.add(Box.createHorizontalStrut(15)); // create an empty space for the HUD
                hudContainer.add(lblScore);

                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST; /* Forces the HUD on the left */
                gbc.insets = new Insets(10, 5, 20, 5);     /* extern margin */
                add(hudContainer, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(150, 5, 5, 5);
                add(effectsPanel, gbc);

                // --- CENTRAL LEVEL ---
                /* force the HUD in the top and the keys in the bottom */
                gbc.gridy = 2;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;
                add(Box.createVerticalGlue(), gbc);

                // --- LOWER LEVEL : WASD KEYS ---
                /* reset the constrain to avoid that the keys get the HUD's proprieties */
                JPanel keysContainer = new JPanel(new GridBagLayout());
                keysContainer.setBackground(Color.WHITE);

                GridBagConstraints gbcKeys = new GridBagConstraints();



                gbcKeys.insets = new Insets(1, 1, 1, 1);
                gbcKeys.fill = GridBagConstraints.NONE;
                gbcKeys.weightx = 0.0;
                gbcKeys.weighty = 0.0;

                /* first row: W key */
                gbcKeys.gridx = 1;
                gbcKeys.gridy = 0;
                keysContainer.add(lblW, gbcKeys);

                /* second row: A,S,D Key */
                gbcKeys.gridx = 0; gbcKeys.gridy = 1; keysContainer.add(lblA, gbcKeys);
                gbcKeys.gridx = 1; gbcKeys.gridy = 1; keysContainer.add(lblS, gbcKeys);
                gbcKeys.gridx = 2; gbcKeys.gridy = 1; keysContainer.add(lblD, gbcKeys);


                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(5, 5, 15, 5);

                add(keysContainer, gbc);
        }

        /**
         * Updates the HUD in real-time
         */
        public void updateHUD(int score, int lives) {
                lblScore.setText(String.valueOf(score));
                lblLives.setText(String.valueOf(lives));
        }

        public void addEffect(int type, long frames){
                if(type == 1){
                        lifeBonus = LIFE_BONUS_FRAMES;
                        for(int i = 0; i < effectCount; i++){
                                if(effectTypes[i] == 1) return;
                        }
                        effectTypes[effectCount] = 1;
                        effectExpires[effectCount] = LIFE_BONUS_FRAMES;
                        JLabel lbl = new JLabel(effectIcons[1]);
                        effectLabels[effectCount] = lbl;
                        effectsPanel.add(lbl);
                        effectsPanel.repaint();
                        effectCount++;
                        return;
                }
                        for(int i = 0; i < effectCount; i++){
                        if(effectTypes[i] == type){
                                effectExpires[i] = frames;
                                return;
                        }
                }
                effectTypes[effectCount] = type;
                effectExpires[effectCount] = frames;
                JLabel lbl = new JLabel(effectIcons[type]);
                effectLabels[effectCount] = lbl;
                effectsPanel.add(lbl);
                effectsPanel.revalidate();
                effectsPanel.repaint();
                effectCount++;
        }

        public void updateEffects(){
                if(lifeBonus > 0){
                        lifeBonus--;
                        if(lifeBonus == 0){
                                removeEffect(1);
                        }
                }
                for(int i = 0; i < effectCount; i++){
                        if(effectExpires[i] <= 0){
                                effectsPanel.remove(effectLabels[i]);
                                effectTypes[i] = effectTypes[effectCount - 1];
                                effectExpires[i] = effectExpires[effectCount - 1];
                                effectLabels[i] = effectLabels[effectCount - 1];
                                effectCount--;
                                i--;
                                effectsPanel.revalidate();
                                effectsPanel.repaint();
                        }
                }
        }

        public void removeEffect(int type){
                for(int i = 0; i < effectCount; i++){
                        if(effectTypes[i] == type){
                                effectsPanel.remove(effectLabels[i]);
                                effectTypes[i] = effectTypes[effectCount - 1];
                                effectExpires[i] = effectExpires[effectCount - 1];
                                effectLabels[i] = effectLabels[effectCount - 1];
                                effectCount--;
                                effectsPanel.revalidate();
                                effectsPanel.repaint();
                                return;
                        }
                }
        }

        public void pauseEffects(){
                effectPauseStart = System.currentTimeMillis();
        }

        public void resumeEffects(){
                if(effectPauseStart == 0) return;
                long pauseDuration = System.currentTimeMillis() - effectPauseStart;
                for(int i = 0; i < effectCount; i++){
                        effectExpires[i] += pauseDuration;
                }
                effectPauseStart = 0;
        }

        /**
         * safely upload all the requested images (keys, pressed and non-pressed)
         */
        private void loadImages() {
                iconW = getSafeIcon("/it/unibo/breakout/images/W_key.png");
                iconA = getSafeIcon("/it/unibo/breakout/images/A_key.png");
                iconS = getSafeIcon("/it/unibo/breakout/images/S_key.png");
                iconD = getSafeIcon("/it/unibo/breakout/images/D_key.png");

                iconWPressed = getSafeIcon("/it/unibo/breakout/images/pressed_W_key.png");
                iconAPressed = getSafeIcon("/it/unibo/breakout/images/pressed_A_key.png");
                iconSPressed = getSafeIcon("/it/unibo/breakout/images/pressed_S_key.png");
                iconDPressed = getSafeIcon("/it/unibo/breakout/images/pressed_D_key.png");

                URL heartUrl = getClass().getResource("/it/unibo/breakout/images/iconHeart.png");
                if (heartUrl != null) {
                        Image img = new ImageIcon(heartUrl).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        iconHeart = new ImageIcon(img);
                } else {
                        iconHeart = new ImageIcon();
                }
                loadEffectIcons();
        }

        private void loadEffectIcons(){
                String[] paths = {
                        null,                                                  // 0 non esiste
                        "/it/unibo/breakout/images/lifebonus.png",             // 1 vita extra
                        "/it/unibo/breakout/images/paddleshort.png",           // 2 pad piccolo
                        "/it/unibo/breakout/images/doublepoints.png",          // 3 punti doppi
                        "/it/unibo/breakout/images/paddlelarge.png",           // 4 pad grande
                        "/it/unibo/breakout/images/frozenblocks.png",          // 5 blocchi fermi
                        "/it/unibo/breakout/images/halfpoints.png",            // 6 punti mezzi
                        "/it/unibo/breakout/images/fastball.png",              // 7 palla veloce
                };
                for(int i = 1; i < paths.length; i++){
                        URL url = getClass().getResource(paths[i]);
                        if(url != null){
                                Image img = new ImageIcon(url).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                effectIcons[i] = new ImageIcon(img);
                        }
                }
        }

        /**
         * return a void icon if one misses
         */
        private ImageIcon getSafeIcon(String path) {
                URL url = getClass().getResource(path);
                return (url != null) ? new ImageIcon(url) : new ImageIcon();
        }

        // --- PUBLIC METHODS FOR THE GAME LOOP ---

        /**
         *this method get called if the user press a key
         * @param key ("W", "A", "S", "D")
         */
        public void setKeyPressed(String key) {
                switch (key.toUpperCase()) {
                case "W" -> lblW.setIcon(iconWPressed);
                case "A" -> lblA.setIcon(iconAPressed);
                case "S" -> lblS.setIcon(iconSPressed);
                case "D" -> lblD.setIcon(iconDPressed);
                }
        }

        /**
         * this method get called if the user release a key
         * @param key ("W", "A", "S", "D")
         */
        public void setKeyReleased(String key) {
                switch (key.toUpperCase()) {
                case "W" -> lblW.setIcon(iconW);
                case "A" -> lblA.setIcon(iconA);
                case "S" -> lblS.setIcon(iconS);
                case "D" -> lblD.setIcon(iconD);
                }
        }
}
