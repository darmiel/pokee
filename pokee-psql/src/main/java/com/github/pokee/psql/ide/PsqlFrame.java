package com.github.pokee.psql.ide;

import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.StatementContext;
import com.github.pokee.psql.exception.ParseException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.List;

public class PsqlFrame extends JFrame {

    private final JTextArea textArea;
    private final JTextArea statusBar;

    private final Highlighter.HighlightPainter errorHighlighter;
    private final Highlighter.HighlightPainter textHighlighter;
    private final Highlighter.HighlightPainter keywordHighlighter;
    private final Highlighter.HighlightPainter valueHighlighter;
    private final Highlighter.HighlightPainter containerHighlighter;
    private final Highlighter.HighlightPainter namespaceHighlighter;

    public PsqlFrame() {
        super("PSQL IDE");

        this.textArea = new JTextArea("query my_query Pokemon::", 20, 60);
        this.textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    checkDocument(e.getDocument());
                } catch (BadLocationException ex) {
                    System.out.println("Error checking document: " + ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    checkDocument(e.getDocument());
                } catch (BadLocationException ex) {
                    System.out.println("Error checking document: " + ex.getMessage());
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        this.statusBar = new JTextArea("Ready", 10, 60);
        this.statusBar.setEditable(false);
        this.statusBar.setFont(new Font("Monospaced", Font.PLAIN, 18));

        this.errorHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED.brighter());
        this.textHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN.brighter().brighter().brighter());
        this.keywordHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN.brighter().brighter());
        this.valueHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY.brighter());
        this.containerHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE.brighter());
        this.namespaceHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA.brighter().brighter());

        this.setLayout(new BorderLayout());

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);
        this.add(new JScrollPane(statusBar), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null); // Center the window
        this.setVisible(true);
    }

    public void checkDocument(final Document document) throws BadLocationException {
        // Check the document for errors
        // Highlight the errors
        this.textArea.getHighlighter().removeAllHighlights();

        final String text = document.getText(0, document.getLength());

        try {
            final Lexer lexer = new Lexer(text);

            while (lexer.nextToken()) {
                final Token currentToken = lexer.getCurrentToken();
                switch (currentToken.type()) {
                    case STRING_LITERAL, IDENTIFIER, NUMBER -> {
                        this.textArea.getHighlighter().addHighlight(currentToken.startIndex(), currentToken.endIndex(), textHighlighter);
                    }
                    case QUERY_NAME, FUNCTION_NAME, USE, AS -> {
                        this.textArea.getHighlighter().addHighlight(currentToken.startIndex(), currentToken.endIndex(), valueHighlighter);
                    }
                    case NAMESPACE_NAME -> {
                        this.textArea.getHighlighter().addHighlight(currentToken.startIndex(), currentToken.endIndex() - 2, namespaceHighlighter);
                    }
                    case FILTER, MAP, QUERY -> {
                        this.textArea.getHighlighter().addHighlight(currentToken.startIndex(), currentToken.endIndex(), keywordHighlighter);
                    }
                    case LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE, STAR, COMMA, DOT, SEMICOLON -> {
                        this.textArea.getHighlighter().addHighlight(currentToken.startIndex(), currentToken.endIndex(), containerHighlighter);
                    }
                }
            }
        } catch (final Exception exception) {
            this.statusBar.setForeground(Color.ORANGE.darker());
            this.statusBar.setText(" Error lexing document: " + exception.getMessage());
            return;
        }

        System.out.println("\n\n");
        try {
            final Lexer lexer = new Lexer(text);
            final Parser parser = new Parser(lexer);

            final List<StatementContext> trees = parser.parseProgram();
            this.statusBar.setForeground(Color.GREEN);

            final StringBuilder bob = new StringBuilder();
            bob.append("Parsed ").append(trees.size()).append(" trees\n");

            for (int i = 0; i < trees.size(); i++) {
                final StatementContext tree = trees.get(i);
                bob.append("# ").append(i).append(":\n").append(tree.toStringTree()).append("\n");
            }

            this.statusBar.setForeground(Color.GREEN);
            this.statusBar.setText(bob.toString());
        } catch (final ParseException e) {
            this.textArea.getHighlighter().removeAllHighlights();
            this.textArea.getHighlighter().addHighlight(e.getIndex() - 1, e.getIndex() + 1, errorHighlighter);

            this.statusBar.setForeground(Color.RED);
            this.statusBar.setText(" Error parsing document: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final PsqlFrame ide = new PsqlFrame();
        });
    }


}
