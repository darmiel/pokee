package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class UseStatementContext extends ParserRuleContext {

    private final TerminalNode original;
    private final TerminalNode alias;

    public UseStatementContext(final TerminalNode original, final TerminalNode alias) {
        this.original = original;
        this.alias = alias;

        this.addChild(original);
        if (alias != null) {
            this.addChild(alias);
        }
    }

    public TerminalNode getOriginal() {
        return original;
    }

    public TerminalNode getAlias() {
        return alias;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitUseStatement(this);
    }

    @Override
    public String toString() {
        return "UseAliasContext{" +
                "original=" + original.getText() +
                ", alias=" + (alias != null ? alias.getText() : "none") +
                '}';
    }

}
