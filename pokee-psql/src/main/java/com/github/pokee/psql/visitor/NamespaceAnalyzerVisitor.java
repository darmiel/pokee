package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.expression.IdentifierExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseStatementContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor to analyze the namespace of the query.
 * Checks if all namespaces are valid and if the alias is already used.
 *
 * <pre>
 *     use Pokemon;
 *         │^^^^^^
 *         └──────────────────────┐
 *     query all_full Pokemon::*; │
 *                    ▲^^^^^^     │
 *                    └───────────┘
 * </pre>
 */
public class NamespaceAnalyzerVisitor extends BasePsqlVisitor<Void> {

    public static final List<String> EXAMPLE_NAMESPACES = List.of("Pokemon", "Daniel");

    private final Map<String, String> importedNamespaces;
    private final List<String> availableNamespaces;

    public NamespaceAnalyzerVisitor(final List<String> availableNamespaces) {
        this.availableNamespaces = availableNamespaces;
        this.importedNamespaces = new HashMap<>();
    }

    @Override
    public Void visitUseStatement(UseStatementContext useStatementContext) {
        return super.visitUseStatement(useStatementContext);
    }

    @Override
    public Void visitNamespacedFieldNode(NamespacedFieldNode namespacedFieldNode) {
        return super.visitNamespacedFieldNode(namespacedFieldNode);
    }

    @Override
    public Void visitIdentifierExpressionNode(IdentifierExpressionNode identifierExpressionNode) {
        return super.visitIdentifierExpressionNode(identifierExpressionNode);
    }

    public Map<String, String> getImportedNamespaces() {
        return importedNamespaces;
    }
}
