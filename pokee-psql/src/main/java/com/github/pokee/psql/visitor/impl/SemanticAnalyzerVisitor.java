package com.github.pokee.psql.visitor.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.exception.SemanticException;
import com.github.pokee.psql.visitor.BasePsqlVisitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SemanticAnalyzerVisitor extends BasePsqlVisitor<Void> {

    private final Set<String> queryNames;

    private final List<String> availableLanguages;

    public SemanticAnalyzerVisitor(final List<String> availableLanguages) {
        super();

        this.availableLanguages = availableLanguages;

        this.queryNames = new HashSet<>();
    }

    @Override
    public Void visitQuery(final QueryContext queryContext) {
        // check if query name was not already defined
        final String queryName = queryContext.getQueryName().getText();
        if (this.queryNames.contains(queryName)) {
            throw new SemanticException("Query name " + queryName + " is already used");
        }
        this.queryNames.add(queryName);
        return super.visitQuery(queryContext);
    }

    @Override
    public Void visitLanguage(final LanguageContext languageContext) {
        // check if language is valid
        if (!this.availableLanguages.contains(languageContext.getLanguage().getText())) {
            throw new SemanticException("Language " + languageContext.getLanguage().getText() + " is not available");
        }
        return super.visitLanguage(languageContext);
    }
}
