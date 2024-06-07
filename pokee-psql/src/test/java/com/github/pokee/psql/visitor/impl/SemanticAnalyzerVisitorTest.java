package com.github.pokee.psql.visitor.impl;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.exception.SemanticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemanticAnalyzerVisitorTest {

    private SemanticAnalyzerVisitor semanticAnalyzerVisitor;

    @BeforeEach
    void setUp() {
        semanticAnalyzerVisitor = new SemanticAnalyzerVisitor(Arrays.asList("en", "de", "fr"));
    }

    public static TerminalNode createTerminalNode(final String text) {
        return createTerminalNode(text, TokenType.IDENTIFIER);
    }

    public static TerminalNode createTerminalNode(final String text, final TokenType type) {
        final TerminalNode terminalNode = new TerminalNode();

        final Token token = new Token(type, text, 0, text.length());
        terminalNode.setSymbol(token);

        return terminalNode;
    }


    @Test
    void testVisitQuery() {
        // Create a QueryContext with a unique name
        final QueryContext queryContext1 = new QueryContext(createTerminalNode("query1"), List.of(), List.of());
        assertDoesNotThrow(() -> semanticAnalyzerVisitor.visitQuery(queryContext1));

        // Create another QueryContext with a different name
        final QueryContext queryContext2 = new QueryContext(createTerminalNode("query2"), List.of(), List.of());
        assertDoesNotThrow(() -> semanticAnalyzerVisitor.visitQuery(queryContext2));

        // Create a QueryContext with a duplicate name
        final QueryContext queryContext3 = new QueryContext(createTerminalNode("query1"), List.of(), List.of());
        assertThrows(SemanticException.class, () -> semanticAnalyzerVisitor.visitQuery(queryContext3));
    }

    @Test
    void testVisitLanguage() {
        // Create a LanguageContext with a valid language
        final LanguageContext languageContext1 = new LanguageContext(createTerminalNode("en"));
        assertDoesNotThrow(() -> semanticAnalyzerVisitor.visitLanguage(languageContext1));

        // Create a LanguageContext with an invalid language
        final LanguageContext languageContext2 = new LanguageContext(createTerminalNode("es"));
        assertThrows(SemanticException.class, () -> semanticAnalyzerVisitor.visitLanguage(languageContext2));
    }

}