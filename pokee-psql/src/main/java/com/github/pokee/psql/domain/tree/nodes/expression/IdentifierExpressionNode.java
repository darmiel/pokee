package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.visitor.Visitor;

public class IdentifierExpressionNode extends ExpressionNode {

    private final TerminalNode namespace;
    private final TerminalNode literal;

    public IdentifierExpressionNode(final TerminalNode namespace,
                                    final TerminalNode literal) {
        this.namespace = namespace;
        this.literal = literal;

        this.addChild(namespace);
        this.addChild(literal);
    }

    public TerminalNode getNamespace() {
        return namespace;
    }

    public TerminalNode getLiteral() {
        return literal;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitIdentifierExpressionNode(this);
    }

    @Override
    public String toString() {
        return this.namespace + "::" + this.literal;
    }

}
