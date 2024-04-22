package com.github.pokee.psql.ide;

import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.expression.ExpressionNode;
import com.github.pokee.psql.exception.ParseException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;

public class PsqlFrame extends JFrame {

    private final JTextPane textArea;
    private final JTextArea statusBar;

    private final Highlighter.HighlightPainter errorHighlighter;

    public PsqlFrame() {
        super("PSQL IDE");

        this.textArea = new JTextPane();
        this.textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        checkDocument(e.getDocument());
                    } catch (BadLocationException ex) {
                        System.out.println("Error checking document: " + ex.getMessage());
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        checkDocument(e.getDocument());
                    } catch (BadLocationException ex) {
                        System.out.println("Error checking document: " + ex.getMessage());
                    }
                });
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        // Initialize styles
        final StyledDocument doc = this.textArea.getStyledDocument();
        this.addStylesToDocument(doc);

        this.statusBar = new JTextArea("Ready", 10, 60);
        this.statusBar.setEditable(false);
        this.statusBar.setFont(new Font("Monospaced", Font.PLAIN, 18));
        this.statusBar.setBackground(Color.BLACK);
        this.statusBar.setForeground(Color.WHITE);

        this.errorHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED.brighter());

        this.setLayout(new BorderLayout());

        final JScrollPane scrollPane = new JScrollPane(this.textArea);
        final TextLineNumber lineNumberComponent = new TextLineNumber(this.textArea);
        scrollPane.setRowHeaderView(lineNumberComponent);

        this.add(scrollPane, BorderLayout.CENTER);

        this.add(new JScrollPane(statusBar), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null); // Center the window
        this.setVisible(true);
    }

    private void addStylesToDocument(StyledDocument doc) {
        // Normal text style
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily(regular, "SansSerif");

        // Style for keywords
        Style keywordStyle = doc.addStyle("keyword", regular);
        StyleConstants.setBold(keywordStyle, true);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        // Other styles for different token types
        Style identifierStyle = doc.addStyle("identifier", regular);
        StyleConstants.setForeground(identifierStyle, new Color(0, 120, 0));  // Dark green

        Style literalStyle = doc.addStyle("literal", regular);
        StyleConstants.setForeground(literalStyle, Color.RED);

        Style functionStyle = doc.addStyle("function", keywordStyle);
        StyleConstants.setForeground(functionStyle, new Color(0, 0, 150)); // Darker blue

        Style namespaceStyle = doc.addStyle("namespace", regular);
        StyleConstants.setForeground(namespaceStyle, new Color(128, 0, 128)); // Purple

        Style errorStyle = doc.addStyle("error", regular);
        StyleConstants.setUnderline(errorStyle, true);
        StyleConstants.setForeground(errorStyle, Color.RED);
    }


    private String getStyleKeyForTokenType(final TokenType type) {
        if (type == TokenType.NAMESPACE_NAME) {
            return "namespace";
        }
        if (type == TokenType.QUERY_NAME || type == TokenType.FUNCTION_NAME) {
            return "function";
        }
        if (type.isLiteral()) {
            return "literal";
        }
        if (type.isIdentifier()) {
            return "identifier";
        }
        if (type.isKeyword()) {
            return "keyword";
        }
        return "regular";
    }

    public void checkDocument(final Document document) throws BadLocationException {
        final String text = document.getText(0, document.getLength());

        // lexer syntax highlighting
        try {
            final StyledDocument doc = textArea.getStyledDocument();

            // Clear existing highlights
            doc.setCharacterAttributes(0, text.length(), doc.getStyle("regular"), true);

            Lexer lexer = new Lexer(text);
            while (lexer.nextToken()) {
                Token currentToken = lexer.getCurrentToken();
                String styleKey = getStyleKeyForTokenType(currentToken.type());

                final int length = currentToken.endIndex() - currentToken.startIndex();
                doc.setCharacterAttributes(currentToken.startIndex(), length, doc.getStyle(styleKey), false);
            }
        } catch (final Exception exception) {
            this.statusBar.setForeground(Color.ORANGE.darker());
            this.statusBar.setText(" Error lexing document: " + exception.getMessage());
            return;
        }

        System.out.println("\n\n");

        try {
            this.textArea.getHighlighter().removeAllHighlights();

            final Lexer lexer = new Lexer(text);
            lexer.nextToken();

            final Parser parser = new Parser(lexer);

            final ExpressionNode node = parser.parseExpressionNode();
            this.statusBar.setForeground(Color.GREEN);
            this.statusBar.setText(node.toString());

//            final List<StatementContext> trees = parser.parseProgram();
//            this.statusBar.setForeground(Color.GREEN);
//
//            final StringBuilder bob = new StringBuilder();
//            bob.append("Parsed ").append(trees.size()).append(" trees\n");
//
//            for (int i = 0; i < trees.size(); i++) {
//                final StatementContext tree = trees.get(i);
//                bob.append("# ").append(i).append(":\n").append(tree.toStringTree()).append("\n");
//            }
//
//            this.statusBar.setForeground(Color.GREEN);
//            this.statusBar.setText(bob.toString());
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
