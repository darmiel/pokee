package com.github.pokee.canvas.util.jframe;

import javax.swing.*;
import java.awt.*;

public class JFramePixelCanvasPanel extends JPanel {

    private final int thickness;
    private final int width, height;
    private final int[][] colors;

    public JFramePixelCanvasPanel(
            final int width,
            final int height,
            final int[][] colors,
            final int thickness
    ) {
        this.width = width;
        this.height = height;
        this.colors = colors;
        this.thickness = thickness;
    }

    @Override
    public void paint(final Graphics graphics) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                final int color = this.colors[x][y];

                // don't render transparent pixels
                final int alpha = color >> 24 & 0xFF;
                if (alpha == 0) {
                    continue;
                }

                graphics.setColor(new Color(color));
                graphics.fillRect(
                        x * this.thickness,
                        y * this.thickness,
                        this.thickness,
                        this.thickness
                );
            }
        }
    }
}
