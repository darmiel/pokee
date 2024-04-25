package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.visitor.Visitor;

public class NotExpressionNode extends ExpressionNode {

    private final ExpressionNode expression;

    public NotExpressionNode(final ExpressionNode expression) {
        this.expression = expression;
        this.addChild(this.expression);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(Visitor<? extends T> visitor) {
        return visitor.visitNotExpressionNode(this);
    }

    @Override
    public String toString() {
        return "Not[" + this.expression + "]";
    }

}
