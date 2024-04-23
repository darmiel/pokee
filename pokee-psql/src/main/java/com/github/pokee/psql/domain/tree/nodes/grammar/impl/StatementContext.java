package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class StatementContext extends ParserRuleContext {

    private final UseStatementContext useStatementContext;
    private final QueryContext queryContext;

    private final LanguageContext languageContext;

    public StatementContext(final UseStatementContext useStatementContext, final QueryContext queryContext, final LanguageContext languageContext) {
        this.useStatementContext = useStatementContext;
        this.queryContext = queryContext;
        this.languageContext = languageContext;

        if (this.useStatementContext != null) {
            this.addChild(this.useStatementContext);
        } else if (this.queryContext != null) {
            this.addChild(this.queryContext);
        } else if (this.languageContext != null) {
            this.addChild(this.languageContext);
        } else {
            throw new IllegalArgumentException("Statement must have either a use statement or a query context");
        }
    }

    public UseStatementContext getUseAliasContext() {
        return useStatementContext;
    }

    public QueryContext getQueryContext() {
        return queryContext;
    }

    public LanguageContext getLanguageContext() {
        return languageContext;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitStatement(this);
    }

    @Override
    public String toString() {
        return "StatementContext{" +
                "useStatementContext=" + useStatementContext +
                ", queryContext=" + queryContext +
                ", languageContext=" + languageContext +
                '}';
    }

}
