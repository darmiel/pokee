package com.github.pokee.psql.visitor.impl;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseStatementContext;
import com.github.pokee.psql.exception.SemanticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.pokee.psql.visitor.impl.SemanticAnalyzerVisitorTest.createTerminalNode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NamespaceAnalyzerVisitorTest {

    private NamespaceAnalyzerVisitor namespaceAnalyzerVisitor;

    @BeforeEach
    void setUp() {
        final Map<String, String> importedNamespaces = new HashMap<>();
        namespaceAnalyzerVisitor = new NamespaceAnalyzerVisitor(NamespaceAnalyzerVisitor.EXAMPLE_NAMESPACES, importedNamespaces);
    }

    @Test
    void visitUseStatement_withUniqueNamespaceAndAlias_doesNotThrowException() {
        UseStatementContext useStatementContext = new UseStatementContext(
                createTerminalNode("Pokemon"),
                createTerminalNode("P")
        );
        assertDoesNotThrow(() -> namespaceAnalyzerVisitor.visitUseStatement(useStatementContext));
    }

    @Test
    void visitUseStatement_withDuplicateAlias_throwsException() {
        final UseStatementContext useStatementContext1 = new UseStatementContext(
                createTerminalNode("Pokemon"),
                createTerminalNode("P")
        );
        namespaceAnalyzerVisitor.visitUseStatement(useStatementContext1);

        final UseStatementContext useStatementContext2 = new UseStatementContext(
                createTerminalNode("Daniel"),
                createTerminalNode("P")
        );
        assertThrows(SemanticException.class, () -> namespaceAnalyzerVisitor.visitUseStatement(useStatementContext2));
    }

    @Test
    void visitUseStatement_withUnknownNamespace_throwsException() {
        UseStatementContext useStatementContext = new UseStatementContext(
                createTerminalNode("Unknown"),
                null
        );
        assertThrows(SemanticException.class, () -> namespaceAnalyzerVisitor.visitUseStatement(useStatementContext));
    }

    @Test
    void visitNamespacedFieldNode_withImportedNamespace_doesNotThrowException() {
        UseStatementContext useStatementContext = new UseStatementContext(
                createTerminalNode("Pokemon"),
                createTerminalNode("P")
        );
        namespaceAnalyzerVisitor.visitUseStatement(useStatementContext);
        NamespacedFieldNode namespacedFieldNode = new NamespacedFieldNode(
                createTerminalNode("P"),
                createTerminalNode("name")
        );
        assertDoesNotThrow(() -> namespaceAnalyzerVisitor.visitNamespacedFieldNode(namespacedFieldNode));
    }

    @Test
    void visitNamespacedFieldNode_withNotImportedNamespace_throwsException() {
        NamespacedFieldNode namespacedFieldNode = new NamespacedFieldNode(
                createTerminalNode("P"),
                createTerminalNode("name")
        );
        assertThrows(SemanticException.class, () -> namespaceAnalyzerVisitor.visitNamespacedFieldNode(namespacedFieldNode));
    }

}