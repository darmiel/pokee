package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.LanguageContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseStatementContext;

import java.util.HashMap;
import java.util.Map;

public class InterpreterVisitor extends BasePsqlVisitor<Void> {

    public static final String DEFAULT_LANGUAGE = "en";

    private final Map<String, String> namespaceAliases;
    private final Map<String, Map<String, Class<?>>> namespaceProjections;

    private String currentLanguage;

    public InterpreterVisitor(final String defaultLanguage, final Map<String, Map<String, Class<?>>> namespaceProjections) {
        super();

        this.currentLanguage = defaultLanguage;

        this.namespaceAliases = new HashMap<>();
        this.namespaceProjections = namespaceProjections;
    }

    // update the imported namespaces
    // TODO: move to namespace analyzer step
    @Override
    public Void visitUseStatement(final UseStatementContext useStatementContext) {
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
        SemanticAnalyzerVisitor.visitUseStatement(this.namespaceProjections, this.namespaceAliases, useStatementContext);
        return super.visitUseStatement(useStatementContext);
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
        return super.visitQuery(queryContext);
    }

}
