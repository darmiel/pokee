package com.github.pokee.psql.ide;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class TextLineNumber extends JPanel implements DocumentListener {

    private final JTextPane textPane;
    private final Font font;

    public TextLineNumber(JTextPane textPane) {
        this.textPane = textPane;
        this.textPane.getDocument().addDocumentListener(this);

        this.font = new Font("Monospaced", Font.PLAIN, 12);
        this.setFont(font);

        this.setPreferredSize(new Dimension(30, Integer.MAX_VALUE));
        this.setForeground(Color.GRAY);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.GRAY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get first and last visible lines
        Rectangle viewRect = textPane.getVisibleRect();
        int startOffset = textPane.viewToModel2D(new Point(0, viewRect.y));
        int endOffset = textPane.viewToModel2D(new Point(0, viewRect.y + viewRect.height));

        // Iterate over lines in the document
        while (startOffset <= endOffset) {
            try {
                int startLine = textPane.getDocument().getDefaultRootElement().getElementIndex(startOffset);
                String lineNumber = Integer.toString(startLine + 1);
                int y = getOffsetY(startOffset, g);
                g.drawString(lineNumber, 5, y);

                // Move to the next line
                startOffset = textPane.getDocument().getDefaultRootElement().getElement(startLine + 1).getStartOffset();
            } catch (Exception e) {
                break;
            }
        }
    }

    private int getOffsetY(int offset, Graphics g) {
        try {
            final Rectangle2D r = textPane.modelToView2D(offset);
            return (int) (r.getY() + r.getHeight() - g.getFontMetrics(font).getDescent());
        } catch (Exception e) {
            return 0;
        }
    }

    // DocumentListener methods
    @Override
    public void insertUpdate(DocumentEvent e) {
        repaint();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        repaint();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        repaint();
    }
}
