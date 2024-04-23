package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class StatementContext extends ParserRuleContext {

    private final UseAliasContext useAliasContext;
    private final QueryContext queryContext;

    public StatementContext(final UseAliasContext useAliasContext, final QueryContext queryContext) {
        this.useAliasContext = useAliasContext;
        this.queryContext = queryContext;

        if (this.useAliasContext != null) {
            this.addChild(this.useAliasContext);
        } else {
            this.addChild(this.queryContext);
        }
    }

    public UseAliasContext getUseAliasContext() {
        return useAliasContext;
    }

    public QueryContext getQueryContext() {
        return queryContext;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitStatement(this);
    }

    @Override
    public String toString() {
        return "StatementContext{" +
                "useAliasContext=" + useAliasContext +
                ", queryContext=" + queryContext +
                '}';
    }
}
