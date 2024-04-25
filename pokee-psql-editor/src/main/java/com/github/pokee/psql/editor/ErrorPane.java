package com.github.pokee.psql.editor;

import com.github.pokee.psql.exception.ExpressionException;
import com.github.pokee.psql.exception.LexerException;
import com.github.pokee.psql.exception.ParseException;
import com.github.pokee.psql.exception.SemanticException;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Objects;

public class ErrorPane extends JPanel {

    private final JTextPane textPane;
    private final Highlighter.HighlightPainter errorHighlighter;
    private LexerException lexerException;
    private ParseException parseException;
    private SemanticException semanticException;
    private ExpressionException expressionException;

    public ErrorPane() {
        this.setLayout(new BorderLayout());

        this.textPane = new JTextPane();
        this.textPane.setEditable(false);
        this.textPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        this.textPane.setBackground(Color.BLACK);
        this.textPane.setForeground(Color.WHITE);

        // Calculate the height for 10 rows of text
        final FontMetrics fm = this.textPane.getFontMetrics(this.textPane.getFont());
        final int height = fm.getHeight() * 10;
        final JScrollPane scrollPane = new JScrollPane(this.textPane);
        scrollPane.setPreferredSize(new Dimension(this.textPane.getPreferredSize().width, height));

        this.add(scrollPane, BorderLayout.CENTER);
        this.textPane.setText("No errors found.");

        this.errorHighlighter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 0, 0, 50));
    }

    public void setText(final String text, final Color color) {
        this.textPane.setForeground(color);

        this.textPane.setText(Objects.requireNonNullElse(text, "No errors found."));
    }

    public void setLexerException(LexerException lexerException) {
        this.lexerException = lexerException;

        if (lexerException != null) {
            this.setText("Lexer | " + lexerException.getMessage(), Color.ORANGE);
        }
    }

    public void setParseException(ParseException parseException) {
        this.parseException = parseException;

        if (parseException != null) {
            this.setText("Parser | " + parseException.getMessage(), Color.RED);
        }
    }

    public void setSemanticException(SemanticException semanticException) {
        this.semanticException = semanticException;

        if (semanticException != null) {
            this.setText("Semantic | " + semanticException.getMessage(), Color.MAGENTA);
        }
    }

    public void setExpressionException(ExpressionException expressionException) {
        this.expressionException = expressionException;

        if (expressionException != null) {
            this.setText("Expression | " + expressionException.getMessage(), Color.CYAN);
        }
    }

    public void reset() {
        this.setLexerException(null);
        this.setParseException(null);
        this.setSemanticException(null);
        this.setExpressionException(null);
        this.setText(null, Color.GREEN);
    }

}
