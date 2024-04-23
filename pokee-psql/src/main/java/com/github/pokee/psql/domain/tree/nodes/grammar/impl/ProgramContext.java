package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

import java.util.List;

public class ProgramContext extends ParserRuleContext {

    private final List<StatementContext> statements;

    public ProgramContext(final List<StatementContext> statements) {
        this.statements = statements;

        statements.forEach(this::addChild);
    }

    public List<StatementContext> getStatements() {
        return statements;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitProgram(this);
    }

}
