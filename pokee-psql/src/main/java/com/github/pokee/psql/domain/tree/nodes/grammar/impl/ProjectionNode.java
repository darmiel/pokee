package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class ProjectionNode extends ParserRuleContext {

    private final TerminalNode namespace;
    private final TerminalNode field;
    private final TerminalNode alias;
    private final boolean isAll;

    public ProjectionNode(final TerminalNode namespace,
                          final TerminalNode field,
                          final TerminalNode alias,
                          final boolean isAll) {
        this.namespace = namespace;
        this.field = field;
        this.alias = alias;
        this.isAll = isAll;
    }

    public TerminalNode getNamespace() {
        return namespace;
    }

    public TerminalNode getField() {
        return field;
    }

    public TerminalNode getAlias() {
        return alias;
    }

    public boolean isAll() {
        return isAll;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitProjection(this);
    }

    @Override
    public String toString() {
        return "ProjectionNode{" +
                "namespace=" + namespace.getText() +
                ", field=" + (field != null ? field.getText() : "none") +
                ", alias=" + (alias != null ? alias.getText() : "none") +
                ", isAll=" + isAll +
                '}';
    }

}
