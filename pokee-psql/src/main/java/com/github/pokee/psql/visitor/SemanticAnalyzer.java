package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProjectionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseAliasContext;
import com.github.pokee.psql.exception.SemanticException;

import java.util.*;

public class SemanticAnalyzer extends SimplerLangBaseVisitor<Void> {

    public static final Map<String, List<String>> EXAMPLE_NAMESPACE_PROJECTIONS = Map.of(
            "Pokemon", List.of("id", "name", "type", "hp"),
            "Sprites", List.of("id", "pokemon_id", "sprite")
    );

    private final Map<String, String> namespaceImports;
    private final Set<String> queryNames;
    private final Map<String, List<String>> namespaceProjections;

    public SemanticAnalyzer(final Map<String, List<String>> namespaceProjections) {
        super();

        this.namespaceProjections = namespaceProjections;

        this.namespaceImports = new HashMap<>();
        this.queryNames = new HashSet<>();
    }

    @Override
    public Void visitUseAlias(final UseAliasContext useAliasContext) {
        // check if the imported namespace is valid
        final String originalNamespace = useAliasContext.getOriginal().getText();
        if (!this.namespaceProjections.containsKey(originalNamespace)) {
            throw new SemanticException("Namespace " + originalNamespace + " is not valid");
        }

        // check if the alias is already used
        final String importName = useAliasContext.getAlias() != null
                ? useAliasContext.getAlias().getText()
                : originalNamespace;
        if (this.namespaceImports.containsKey(importName)) {
            throw new SemanticException("Alias " + importName + " is already used");
        }
        this.namespaceImports.put(importName, originalNamespace);

        return super.visitUseAlias(useAliasContext);
    }

    @Override
    public Void visitQuery(final QueryContext queryContext) {
        // check if query name was not already defined
        final String queryName = queryContext.getQueryName().getText();
        if (this.queryNames.contains(queryName)) {
            throw new SemanticException("Query name " + queryName + " is already used");
        }
        this.queryNames.add(queryName);

        // make sure projections are valid
        for (final ProjectionNode projectionNode : queryContext.getProjectionNodes()) {
            final String namespace = projectionNode.getNamespace().getText();

            // map original namespace to alias
            final String targetNamespace = this.namespaceImports.get(namespace);
            if (targetNamespace == null) {
                throw new SemanticException("Namespace " + namespace + " is not imported for query " + queryName + " in projection " + projectionNode);
            }
            final List<String> validFieldNames = this.namespaceProjections.get(targetNamespace);
            if (validFieldNames == null) {
                throw new SemanticException("Namespace " + namespace + " is not valid for query " + queryName + " in projection " + projectionNode);
            }

            // skip if projection is *
            if (projectionNode.isAll()) {
                continue;
            }

            // check if the field is valid
            final String fieldName = projectionNode.getField().getText();
            if (!validFieldNames.contains(fieldName)) {
                throw new SemanticException("Field " + fieldName + " is not valid for namespace " + namespace + " in query " + queryName + " in projection " + projectionNode);
            }
        }

        return super.visitQuery(queryContext);
    }

}
