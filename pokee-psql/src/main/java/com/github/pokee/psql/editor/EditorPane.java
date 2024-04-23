package com.github.pokee.psql.editor;

import com.github.pokee.psql.Lexer;
import com.github.pokee.psql.Parser;
import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProgramContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.StatementContext;
import com.github.pokee.psql.exception.LexerException;
import com.github.pokee.psql.exception.ParseException;
import com.github.pokee.psql.exception.SemanticException;
import com.github.pokee.psql.visitor.InterpreterVisitor;
import com.github.pokee.psql.visitor.NamespaceAnalyzerVisitor;
import com.github.pokee.psql.visitor.SemanticAnalyzerVisitor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditorPane extends JPanel implements DocumentListener {

    private final JTextPane textPane;
    private final StatusPagePane statusPage;
    private final ErrorPane errorPane;
    int a = 0;

    public EditorPane(final StatusPagePane statusPage,
                      final ErrorPane errorPane) {
        statusPage.setEditorPane(this);

        this.statusPage = statusPage;
        this.errorPane = errorPane;

        this.setLayout(new BorderLayout());

        this.textPane = new JTextPane();
        this.textPane.setFont(new Font("Monospaced", Font.PLAIN, 18));
        this.textPane.getDocument().addDocumentListener(this);

        // Initialize styles
        final StyledDocument doc = this.textPane.getStyledDocument();
        EditorPane.addStylesToDocument(doc);

        final JScrollPane scrollPane = new JScrollPane(this.textPane);
        scrollPane.setRowHeaderView(new TextLineNumberComponent(this.textPane));


        // Calculate the height for 10 rows of text
        final FontMetrics fm = this.textPane.getFontMetrics(this.textPane.getFont());
        final int height = fm.getHeight() * 50;
        final int width = fm.getWidths()['m'] * 100;
        scrollPane.setPreferredSize(new Dimension(width, height));

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public static void addStylesToDocument(StyledDocument doc) {
        // Normal text style
        final Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        final Style regular = doc.addStyle("regular", defaultStyle);
        StyleConstants.setFontFamily(regular, "SansSerif");

        // Style for keywords
        final Style keywordStyle = doc.addStyle("keyword", regular);
        StyleConstants.setBold(keywordStyle, true);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        // Other styles for different token types
        final Style identifierStyle = doc.addStyle("identifier", regular);
        StyleConstants.setForeground(identifierStyle, new Color(0, 120, 0));  // Dark green

        final Style literalStyle = doc.addStyle("literal", regular);
        StyleConstants.setForeground(literalStyle, Color.RED);

        final Style functionStyle = doc.addStyle("function", keywordStyle);
        StyleConstants.setForeground(functionStyle, new Color(0, 0, 150)); // Darker blue

        final Style namespaceStyle = doc.addStyle("namespace", regular);
        StyleConstants.setForeground(namespaceStyle, new Color(128, 0, 128)); // Purple

        final Style errorStyle = doc.addStyle("error", regular);
        StyleConstants.setUnderline(errorStyle, true);
        StyleConstants.setForeground(errorStyle, Color.RED);
    }

    public static String getStyleKeyForTokenType(final TokenType type) {
        if (type == TokenType.NAMESPACE_NAME) {
            return "namespace";
        }
        if (type == TokenType.FUNCTION_NAME) {
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

    private void swingValidateAndHighlightDocument(final Document document) {
        SwingUtilities.invokeLater(() -> {
            try {
                this.validateAndHighlightDocument(document);
            } catch (final BadLocationException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        });
    }

    public void validateAndHighlightDocument(final Document document) throws BadLocationException {
        final String text = document.getText(0, document.getLength());
        final StyledDocument doc = textPane.getStyledDocument();

        // clear existing highlights
        doc.setCharacterAttributes(0, text.length(), doc.getStyle("regular"), true);
        this.textPane.getHighlighter().removeAllHighlights();

        final Lexer lexer = new Lexer(text);
        final Parser parser = new Parser(lexer);

        this.statusPage.reset();
        this.errorPane.reset();

        try {
            // do syntax highlighting
            final List<Token> tokens = new ArrayList<>();
            while (lexer.nextToken()) {
                tokens.add(lexer.getCurrentToken());

                final Token currentToken = lexer.getCurrentToken();
                final String styleKey = EditorPane.getStyleKeyForTokenType(currentToken.type());

                final int length = currentToken.endIndex() - currentToken.startIndex();
                doc.setCharacterAttributes(currentToken.startIndex(), length, doc.getStyle(styleKey), false);
            }
            this.statusPage.updateTokens(tokens);

            // reset the lexer so the parser can start from the beginning
            lexer.reset();

            // next we try to parse the program
            final ProgramContext program = parser.parseProgram();
            final List<StatementContext> statements = program.getStatements();
            this.statusPage.updateStatements(statements);

            // make sure all namespaces are defined
            final NamespaceAnalyzerVisitor namespaceAnalyzerVisitor = new NamespaceAnalyzerVisitor(
                    NamespaceAnalyzerVisitor.EXAMPLE_NAMESPACES
            );
            program.accept(namespaceAnalyzerVisitor);
            this.statusPage.updateNamespaces(namespaceAnalyzerVisitor.getImportedNamespaces());

            program.accept(new SemanticAnalyzerVisitor(SemanticAnalyzerVisitor.EXAMPLE_NAMESPACE_PROJECTIONS, List.of("de", "en")));
            this.statusPage.setSemanticOk(true);

            program.accept(new InterpreterVisitor(InterpreterVisitor.DEFAULT_LANGUAGE, SemanticAnalyzerVisitor.EXAMPLE_NAMESPACE_PROJECTIONS));
            this.statusPage.setInterpreterOk(true);

        } catch (final LexerException lexerException) {
            this.errorPane.setLexerException(lexerException);
        } catch (final ParseException parseException) {
            this.errorPane.setParseException(parseException);
        } catch (final SemanticException semanticException) {
            this.errorPane.setSemanticException(semanticException);
        }
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        System.out.println("insertUpdate " + a++);
        this.swingValidateAndHighlightDocument(e.getDocument());
    }

    ///

    @Override
    public void removeUpdate(final DocumentEvent e) {
        System.out.println("removeUpdate " + a++);
        this.swingValidateAndHighlightDocument(e.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public void setSelectedToken(final Token selectedValue) {
        // highlight
        final int startIndex = selectedValue.startIndex();
        final int endIndex = selectedValue.endIndex();
        this.textPane.setSelectionStart(startIndex);
        this.textPane.setSelectionEnd(endIndex);
    }
}
