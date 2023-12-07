package com.github.pokee.canvas.impl;

import com.github.pokee.canvas.Canvas;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

public class JFrameCanvas extends Canvas<Void> {

    @RequiredArgsConstructor
    class Panel extends JPanel {
        private final int thickness;

        @Override
        public void paint(final Graphics graphics) {
            for (int y = 0; y < JFrameCanvas.this.height; y++) {
                for (int x = 0; x < JFrameCanvas.this.width; x++) {
                    final int color = JFrameCanvas.this.colors[x][y];

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

    private final JFrame frame;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width  the width of the canvas
     * @param height the height of the canvas
     * @param thickness multiplier for pixel representation
     */
    public JFrameCanvas(final int width, final int height, final int thickness) {
        super(width, height);

        this.frame = new JFrame("JFrameCanvas");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(
                JFrameCanvas.this.width * thickness,
                JFrameCanvas.this.height * thickness
        );
        this.frame.add(new Panel(thickness));
        this.frame.setVisible(true);
    }

    @Override
    public Void render() {
        this.frame.repaint();
        return null;
    }

}
