package com.github.pokee.psql.visitor.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.query.NamespaceValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.pokee.psql.visitor.impl.SemanticAnalyzerVisitorTest.createTerminalNode;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterVisitorTest {

    private InterpreterVisitor interpreterVisitor;
    private List<InterpreterVisitor.Query> queries;

    @BeforeEach
    public void setup() {
        queries = new ArrayList<>();
        final Map<String, NamespaceValues> namespaceValues = new HashMap<>();
        interpreterVisitor = new InterpreterVisitor(queries, InterpreterVisitor.DEFAULT_LANGUAGE, new HashMap<>(), namespaceValues);
    }

    @Test
    public void testVisitLanguage() {
        final LanguageContext languageContext = new LanguageContext(
                createTerminalNode("fr")
        );
        interpreterVisitor.visitLanguage(languageContext);
        assertEquals("fr", interpreterVisitor.getCurrentLanguage());
    }

    @Test
    public void testVisitQuery() {
        final QueryContext queryContext = new QueryContext(
                createTerminalNode("testQuery"),
                new ArrayList<>(),
                new ArrayList<>()
        );
        interpreterVisitor.visitQuery(queryContext);

        assertEquals(1, queries.size());
        assertEquals("testQuery", queries.get(0).name());
    }
}