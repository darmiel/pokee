package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;

import java.util.Map;

public class InterpreterVisitor extends BasePsqlVisitor<Void> {

    public static final String DEFAULT_LANGUAGE = "en";

    private final Map<String, String> namespaceAliases;
    private final Map<String, Map<String, Class<?>>> namespaceProjections;

    private String currentLanguage;

    public InterpreterVisitor(final String defaultLanguage,
                              final Map<String, String> namespaceAliases,
                              final Map<String, Map<String, Class<?>>> namespaceProjections) {
        super();

        this.currentLanguage = defaultLanguage;
        this.namespaceAliases = namespaceAliases;
        this.namespaceProjections = namespaceProjections;
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
        // TODO: parse query
        return super.visitQuery(queryContext);
    }

}
