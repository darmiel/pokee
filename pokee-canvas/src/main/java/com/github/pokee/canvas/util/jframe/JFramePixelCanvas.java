package com.github.pokee.canvas.util.jframe;

import com.github.pokee.canvas.PixelCanvas;
import com.github.pokee.canvas.Renderable;

import javax.swing.*;
import java.awt.*;

public class JFramePixelCanvas extends PixelCanvas implements Renderable<Void> {

    private final JFrame frame;

    /**
     * Constructs a canvas with the given width and height.
     *
     * @param width     the width of the canvas
     * @param height    the height of the canvas
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
        this.frame.add(new JFramePixelCanvasPanel(
                this.getWidth(),
                this.getHeight(),
                this.getColors(),
                thickness
        ));
        this.frame.setVisible(true);
    }

    @Override
    public Void render() {
        this.frame.repaint();
        return null;
    }

    public JFrame getFrame() {
        return frame;
    }
}
