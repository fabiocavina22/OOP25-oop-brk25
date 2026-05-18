package it.unibo.breakout.view.impl;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class LeftPanel extends JPanel {

    public LeftPanel() {

        setBackground(Color.GRAY);

        // Bordo laterale destro
        Border rightBorder = BorderFactory.createMatteBorder(
                0, // top
                0, // left
                0, // bottom
                10, // right
                Color.BLACK
        );

        // Padding interno
        Border padding = BorderFactory.createEmptyBorder(
                10,
                10,
                10,
                10
        );

        // Combina bordo + padding
        setBorder(
                BorderFactory.createCompoundBorder(
                        rightBorder,
                        padding
                )
        );
    }
}
