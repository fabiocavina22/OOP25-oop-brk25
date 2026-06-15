package it.unibo.breakout.view.impl;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import java.util.Locale;

/**
 * Left side panel of the game window. Shows the HUD (lives and score),
 * the active power-up/power-down effect icons and the WASD key indicators.
 */
public final class LeftPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private ImageIcon iconW, iconA, iconS, iconD;

        private ImageIcon iconWPressed, iconAPressed, iconSPressed, iconDPressed;

        private final JLabel lblW = new JLabel();
        private final JLabel lblA = new JLabel();
        private final JLabel lblS = new JLabel();
        private final JLabel lblD = new JLabel();

        private final JLabel lblLives = new JLabel("3");
        private final JLabel lblScore = new JLabel("0");

        private final int[] effectTypes = new int[MAX_EFFECTS];
        private final long[] effectExpires = new long[MAX_EFFECTS];
        private final JLabel[] effectLabels = new JLabel[MAX_EFFECTS];
        private int effectCount;
        private int lifeBonus;
        private final JPanel effectsPanel;

        private final ImageIcon[] effectIcons = new ImageIcon[EFFECT_TYPE_COUNT];

        private static final int MAX_EFFECTS = 7;
        private static final int EFFECT_TYPE_COUNT = 8;
        private static final int LIFE_BONUS_FRAMES = 120;
        private static final int LIFE_EFFECT_TYPE = 1;

        private static final int BORDER_RIGHT = 10;
        private static final int PADDING = 10;
        private static final int FLOW_GAP = 10;
        private static final int FONT_SIZE = 18;
        private static final int HEART_SIZE = 30;
        private static final int EFFECT_ICON_SIZE = 80;

        private static final int SCORE_WIDTH = 85;
        private static final int SCORE_HEIGHT = 35;
        private static final int HUD_STRUT = 15;

        private static final int INSET = 5;
        private static final int INSET_TOP = 10;
        private static final int INSET_BOTTOM = 20;
        private static final int EFFECTS_TOP_INSET = 150;
        private static final int KEYS_BOTTOM_INSET = 15;

        /**
         * Builds the panel, loads the images and lays out the HUD, effects area
         * and the WASD key indicators.
         */
        public LeftPanel() {


                setBackground(Color.WHITE);

                final Border rightBorder = BorderFactory.createMatteBorder(
                0,
                0,
                0,
                BORDER_RIGHT,
                Color.BLACK
                );

                final Border padding = BorderFactory.createEmptyBorder(
                PADDING,
                PADDING,
                PADDING,
                PADDING
                );

                setBorder(
                BorderFactory.createCompoundBorder(
                        rightBorder,
                        padding
                )
                );

                loadImages();

                effectsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_GAP, FLOW_GAP));
                effectsPanel.setBackground(Color.WHITE);

                lblW.setIcon(iconW);
                lblA.setIcon(iconA);
                lblS.setIcon(iconS);
                lblD.setIcon(iconD);

                final Font retroFont = new Font("Courier New", Font.BOLD, FONT_SIZE);

                final JPanel hudContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_GAP, 0));
                hudContainer.setBackground(Color.WHITE);

                lblLives.setFont(retroFont);
                final JLabel lblHeartImg = new JLabel(loadHeart());

                /* Score's rectangle */
                lblScore.setFont(retroFont);
                lblScore.setForeground(Color.WHITE);
                lblScore.setBackground(Color.BLACK);
                lblScore.setOpaque(true);
                lblScore.setHorizontalAlignment(SwingConstants.CENTER);
                lblScore.setPreferredSize(new Dimension(SCORE_WIDTH, SCORE_HEIGHT));
                lblScore.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

                hudContainer.add(lblLives);
                hudContainer.add(lblHeartImg);
                hudContainer.add(Box.createHorizontalStrut(HUD_STRUT)); // create an empty space for the HUD
                hudContainer.add(lblScore);

                setLayout(new GridBagLayout());
                final GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST; /* Forces the HUD on the left */
                gbc.insets = new Insets(INSET_TOP, INSET, INSET_BOTTOM, INSET);     /* extern margin */
                add(hudContainer, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(EFFECTS_TOP_INSET, INSET, INSET, INSET);
                add(effectsPanel, gbc);

                // --- CENTRAL LEVEL ---
                /*force the HUD in the top and the keys in the bottom */
                gbc.gridy = 2;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;
                add(Box.createVerticalGlue(), gbc);

                // --- LOWER LEVEL : WASD KEYS ---
                /** reset the constrain to avoid that the keys get the HUD's proprieties */
                final JPanel keysContainer = new JPanel(new GridBagLayout());
                keysContainer.setBackground(Color.WHITE);

                final GridBagConstraints gbcKeys = new GridBagConstraints();
                gbcKeys.insets = new Insets(1, 1, 1, 1);
                gbcKeys.fill = GridBagConstraints.NONE;
                gbcKeys.weightx = 0.0;
                gbcKeys.weighty = 0.0;

                /* first row: W key */
                gbcKeys.gridx = 1;
                gbcKeys.gridy = 0;
                keysContainer.add(lblW, gbcKeys);

                /* second row: A,S,D Key */
                gbcKeys.gridx = 0;
                gbcKeys.gridy = 1;
                keysContainer.add(lblA, gbcKeys);
                gbcKeys.gridx = 1;
                gbcKeys.gridy = 1;
                keysContainer.add(lblS, gbcKeys);
                gbcKeys.gridx = 2;
                gbcKeys.gridy = 1;
                keysContainer.add(lblD, gbcKeys);

                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(INSET, INSET, KEYS_BOTTOM_INSET, INSET);

                add(keysContainer, gbc);
        }

          /**
         * Updates the HUD in real time.
         *
         * @param score the current score
         * @param lives the current number of lives
         */
        public void updateHUD(final int score, final int lives) {
                lblScore.setText(String.valueOf(score));
                lblLives.setText(String.valueOf(lives));
        }

       /**
         * Adds or refreshes the icon of an active effect. The extra life is shown
         * for a fixed duration.
         *
         * @param type the effect type identifier
         * @param frames how many frames the effect stays active
         */
        public void addEffect(final int type, final long frames) {
                if (type == LIFE_EFFECT_TYPE) {
                        lifeBonus = LIFE_BONUS_FRAMES;
                        for (int i = 0; i < effectCount; i++) {
                                if (effectTypes[i] == 1) {
                                        return;
                                }
                        }
                        effectTypes[effectCount] = LIFE_EFFECT_TYPE;
                        effectExpires[effectCount] = LIFE_BONUS_FRAMES;
                        final JLabel lbl = new JLabel(effectIcons[LIFE_EFFECT_TYPE]);
                        effectLabels[effectCount] = lbl;
                        effectsPanel.add(lbl);
                        effectsPanel.repaint();
                        effectCount++;
                        return;
                }
                        for (int i = 0; i < effectCount; i++) {
                        if (effectTypes[i] == type) {
                                effectExpires[i] = frames;
                                return;
                        }
                }
                effectTypes[effectCount] = type;
                effectExpires[effectCount] = frames;
                final JLabel lbl = new JLabel(effectIcons[type]);
                effectLabels[effectCount] = lbl;
                effectsPanel.add(lbl);
                effectsPanel.revalidate();
                effectsPanel.repaint();
                effectCount++;
        }

        /**
         * Updates the effect icons each frame.
         */
        public void updateEffects() {
                if (lifeBonus > 0) {
                        lifeBonus--;
                        if (lifeBonus == 0) {
                                removeEffect(1);
                        }
                }
                for (int i = 0; i < effectCount; i++) {
                        if (effectExpires[i] <= 0) {
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

       /**
        * Removes the icon of the given effect type from the panel.
        *
        * @param type
        */
        public void removeEffect(final int type) {
                for (int i = 0; i < effectCount; i++) {
                        if (effectTypes[i] == type) {
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

        /* safely upload all the requested images (keys, pressed and non-pressed) */
        private void loadImages() {
                iconW = getSafeIcon("/it/unibo/breakout/images/W_key.png");
                iconA = getSafeIcon("/it/unibo/breakout/images/A_key.png");
                iconS = getSafeIcon("/it/unibo/breakout/images/S_key.png");
                iconD = getSafeIcon("/it/unibo/breakout/images/D_key.png");

                iconWPressed = getSafeIcon("/it/unibo/breakout/images/pressed_W_key.png");
                iconAPressed = getSafeIcon("/it/unibo/breakout/images/pressed_A_Key.png");
                iconSPressed = getSafeIcon("/it/unibo/breakout/images/pressed_S_key.png");
                iconDPressed = getSafeIcon("/it/unibo/breakout/images/pressed_D_key.png");

                loadEffectIcons();
        }

        /* loads and scales to 80x80 the effect icons */
        private void loadEffectIcons() {
                final String[] paths = {
                        null,
                        "/it/unibo/breakout/images/lifebonus.png",
                        "/it/unibo/breakout/images/paddleshort.png",
                        "/it/unibo/breakout/images/doublepoints.png",
                        "/it/unibo/breakout/images/paddlelarge.png",
                        "/it/unibo/breakout/images/frozenblocks.png",
                        "/it/unibo/breakout/images/halfpoints.png",
                        "/it/unibo/breakout/images/fastball.png",
                };
                for (int i = 1; i < paths.length; i++) {
                        final URL url = getClass().getResource(paths[i]);
                        if (url != null) {
                                final Image img = new ImageIcon(url).getImage()
                                .getScaledInstance(EFFECT_ICON_SIZE, EFFECT_ICON_SIZE, Image.SCALE_SMOOTH);
                                effectIcons[i] = new ImageIcon(img);
                        }
                }
        }

        /**
         * Returns the icon at the given path, or an empty icon if it is missing.
         *
         * @param path the classpath resource path of the image
         * @return the loaded icon, or an empty icon if the resource was not found
         */
        private ImageIcon getSafeIcon(final String path) {
                final URL url = getClass().getResource(path);
                return (url != null) ? new ImageIcon(url) : new ImageIcon();
        }


        private ImageIcon loadHeart() {
        final URL heartUrl = getClass().getResource("/it/unibo/breakout/images/iconHeart.png");
        if (heartUrl != null) {
            final Image img = new ImageIcon(heartUrl).getImage()
                    .getScaledInstance(HEART_SIZE, HEART_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return new ImageIcon();
    }

        // --- PUBLIC METHODS FOR THE GAME LOOP ---

        /**
         *
         *Called when the user presses a key.
         *
         * @param key ("W", "A", "S", "D")
         */
        public void setKeyPressed(final String key) {
                switch (key.toUpperCase(Locale.ROOT)) {
                case "W" -> lblW.setIcon(iconWPressed);
                case "A" -> lblA.setIcon(iconAPressed);
                case "S" -> lblS.setIcon(iconSPressed);
                case "D" -> lblD.setIcon(iconDPressed);
                default -> { }
                }
        }

        /**
         * Called when the user releases a key.
         *
         * @param key ("W", "A", "S", "D")
         */
        public void setKeyReleased(final String key) {
                switch (key.toUpperCase(Locale.ROOT)) {
                case "W" -> lblW.setIcon(iconW);
                case "A" -> lblA.setIcon(iconA);
                case "S" -> lblS.setIcon(iconS);
                case "D" -> lblD.setIcon(iconD);
                default -> { }
                }
        }
}
