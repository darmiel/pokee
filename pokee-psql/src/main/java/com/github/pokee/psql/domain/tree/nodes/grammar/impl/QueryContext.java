package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class QueryContext extends ParserRuleContext {

    @Override
    public <T> T accept(Visitor<? extends T> visitor) {
        return visitor.visitQuery(this);
    }

}
