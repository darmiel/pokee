package com.github.pokee.psql.visitor.impl;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.query.NamespaceValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class InterpreterVisitorTest {

    private InterpreterVisitor interpreterVisitor;
    private List<InterpreterVisitor.Query> queries;

    public static TerminalNode createTerminalNode(final String text) {
        final TerminalNode terminalNode = Mockito.mock(TerminalNode.class);
        when(terminalNode.getText()).thenReturn(text);
        return terminalNode;
    }

    @BeforeEach
    public void setup() {
        queries = new ArrayList<>();
        final Map<String, NamespaceValues> namespaceValues = new HashMap<>();
        interpreterVisitor = new InterpreterVisitor(queries, InterpreterVisitor.DEFAULT_LANGUAGE, new HashMap<>(), namespaceValues);
    }

    @Test
    public void testVisitLanguage() {
        final LanguageContext languageContext = new LanguageContext(
                InterpreterVisitorTest.createTerminalNode("fr")
        );
        when(languageContext.getText()).thenReturn("fr");
        interpreterVisitor.visitLanguage(languageContext);
        assertEquals("fr", interpreterVisitor.getCurrentLanguage());
    }

    @Test
    public void testVisitQuery() {
        final QueryContext queryContext = new QueryContext(
                InterpreterVisitorTest.createTerminalNode("testQuery"),
                new ArrayList<>(),
                new ArrayList<>()
        );
        interpreterVisitor.visitQuery(queryContext);

        assertEquals(1, queries.size());
        assertEquals("testQuery", queries.get(0).name());
    }
}