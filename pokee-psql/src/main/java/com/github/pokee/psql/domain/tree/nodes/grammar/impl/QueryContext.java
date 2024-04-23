package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.ExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

import java.util.List;

public class QueryContext extends ParserRuleContext {

    private final TerminalNode queryName;
    private final List<ProjectionNode> projectionNodes;
    private final List<ExpressionNode> filters;

    public QueryContext(final TerminalNode queryName,
                        final List<ProjectionNode> projectionNodes,
                        final List<ExpressionNode> filters) {
        this.queryName = queryName;
        this.projectionNodes = projectionNodes;
        this.filters = filters;

        this.addChild(queryName);
        projectionNodes.forEach(this::addChild);
        filters.forEach(this::addChild);
    }

    public TerminalNode getQueryName() {
        return this.queryName;
    }

    public List<ProjectionNode> getProjectionNodes() {
        return this.projectionNodes;
    }

    public List<ExpressionNode> getFilters() {
        return this.filters;
    }

    @Override
    public <T> T accept(Visitor<? extends T> visitor) {
        return visitor.visitQuery(this);
    }

    @Override
    public String toString() {
        return "QueryContext{\n" +
                "\tqueryName=" + queryName +
                ",\n\tprojectionNodes=" + projectionNodes +
                ",\n\tfilters=" + filters +
                "\n}";
    }
}
