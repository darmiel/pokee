package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.visitor.Visitor;

public class IdentifierExpressionNode extends ExpressionNode {

    private final NamespacedFieldNode target;

    public IdentifierExpressionNode(final NamespacedFieldNode target) {
        this.target = target;

        this.addChild(target);
    }

    public NamespacedFieldNode getTarget() {
        return target;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitIdentifierExpressionNode(this);
    }

    @Override
    public String toString() {
        return this.target.getText();
    }

}
