package com.github.pokee.psql.visitor.impl;

import com.github.pokee.common.fielder.Fielder;
import com.github.pokee.psql.domain.tree.nodes.expression.ExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProjectionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.exception.ExpressionException;
import com.github.pokee.psql.query.NamespaceValues;
import com.github.pokee.psql.visitor.AliasNamespacedBasePsqlVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class InterpreterVisitor extends AliasNamespacedBasePsqlVisitor<Void> {

    public static final String DEFAULT_LANGUAGE = "en";

    private final List<Query> queries;
    private final Map<String, NamespaceValues> namespaceValues;


    private String currentLanguage;

    public InterpreterVisitor(final List<Query> queries,
                              final String defaultLanguage,
                              final Map<String, String> importedNamespaces,
                              final Map<String, NamespaceValues> namespaceValues) {
        super(importedNamespaces);

        this.queries = queries;
        this.currentLanguage = defaultLanguage;
        this.namespaceValues = namespaceValues;
    }

    // update the current language
    @Override
    public Void visitLanguage(final LanguageContext languageContext) {
        if (this.currentLanguage == null || !this.currentLanguage.equals(languageContext.getText())) {
            this.currentLanguage = languageContext.getText();
        }
        return super.visitLanguage(languageContext);
    }

    @Override
    public Void visitQuery(final QueryContext queryContext) {
        final String queryName = queryContext.getQueryName().getText();
        final List<Predicate<Fielder>> queryFilters = new ArrayList<>();

        final ExpressionVisitor expressionVisitor = new ExpressionVisitor(
                super.importedNamespaces,
                this.namespaceValues,
                this.currentLanguage
        );

        for (final ExpressionNode filter : queryContext.getFilters()) {
            final Predicate<Fielder> fielderPredicate = filter.accept(expressionVisitor);
            if (fielderPredicate == null) {
                throw new ExpressionException("Filter expression is null");
            }
            queryFilters.add(fielderPredicate);
        }

        this.queries.add(new Query(queryName, queryContext.getProjectionNodes(), queryFilters, this.currentLanguage));
        return null;
    }

    public record Query(String name,
                        List<ProjectionNode> projections,
                        List<Predicate<Fielder>> predicate,
                        String language) {
    }

}
