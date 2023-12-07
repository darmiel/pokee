package com.github.pokee.canvas.display;

import com.github.pokee.canvas.canvas.PixelCanvas;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

public class JFramePixelCanvas extends PixelCanvas implements Renderable<Void> {

    @RequiredArgsConstructor
    class Panel extends JPanel {
        private final int thickness;

        @Override
        public void paint(final Graphics graphics) {
            for (int y = 0; y < JFramePixelCanvas.this.height; y++) {
                for (int x = 0; x < JFramePixelCanvas.this.width; x++) {
                    final int color = JFramePixelCanvas.this.colors[x][y];

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
    public JFramePixelCanvas(final int width, final int height, final int thickness) {
        super(width, height);

        this.frame = new JFrame("JFrameCanvas");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(
                JFramePixelCanvas.this.width * thickness,
                (JFramePixelCanvas.this.height) * thickness + (thickness * 25)
        );
        this.frame.setBackground(Color.BLACK);
        this.frame.add(new Panel(thickness));
        this.frame.setVisible(true);
    }

    @Override
    public Void render() {
        this.frame.repaint();
        return null;
    }

}
