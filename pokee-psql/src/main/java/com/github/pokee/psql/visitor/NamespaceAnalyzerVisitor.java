package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseStatementContext;
import com.github.pokee.psql.exception.SemanticException;

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

    public NamespaceAnalyzerVisitor(final List<String> availableNamespaces,
                                    final Map<String, String> importedNamespaces) {
        this.availableNamespaces = availableNamespaces;
        this.importedNamespaces = importedNamespaces;
    }

    /**
     * Visit a use statement and update the namespace imports
     *
     * @param useStatementContext the use statement context
     */
    @Override
    public Void visitUseStatement(final UseStatementContext useStatementContext) {
        // check if the imported namespace is valid
        final String originalNamespace = useStatementContext.getOriginal().getText();
        if (!this.availableNamespaces.contains(originalNamespace)) {
            throw new SemanticException("Unknown namespace: " + originalNamespace);
        }

        // check if the alias is already used
        final String importName = useStatementContext.getAlias() != null
                ? useStatementContext.getAlias().getText()
                : originalNamespace;
        if (this.importedNamespaces.containsKey(importName)) {
            throw new SemanticException("Alias " + importName + " is already used");
        }

        // update the namespace aliases
        // e.g. `use Pokémon;` will import Pokémon to the namespace Pokémon
        //           ^^^^^^^
        //           └▶ The namespace to import
        // │
        // and `use Pokémon as P;` will import Pokémon add the alias P to the namespace Pokémon
        //          ^^^^^^^    ^
        //          │          └▶ The alias to use
        //          └▶ The namespace to import
        //
        this.importedNamespaces.put(importName, originalNamespace);

        return super.visitUseStatement(useStatementContext);
    }

    @Override
    public Void visitNamespacedFieldNode(final NamespacedFieldNode namespacedFieldNode) {
        // check if the namespace is imported
        final String namespace = namespacedFieldNode.getNamespace().getText();
        if (!this.importedNamespaces.containsKey(namespace)) {
            throw new SemanticException("Namespace " + namespace + " is not imported");
        }
        return super.visitNamespacedFieldNode(namespacedFieldNode);
    }

}
