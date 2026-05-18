package it.unibo.breakout.view.impl;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class RightPanel extends JPanel {

    public RightPanel() {

         setBackground(Color.GRAY);

        // Bordo laterale destro
        Border leftBorder = BorderFactory.createMatteBorder(
                0, // top
                10, // left
                0, // bottom
                0, // right
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
                        leftBorder,
                        padding
                )
        );

    }
}
