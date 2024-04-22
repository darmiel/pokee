package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.visitor.Visitor;

public class LiteralExpressionNode extends ExpressionNode {

    private final TokenType type;
    private final TerminalNode literal;

    public LiteralExpressionNode(final TokenType type,
                                 final TerminalNode literal) {
        this.type = type;
        this.literal = literal;
    }

    public TokenType getType() {
        return type;
    }

    public TerminalNode getLiteral() {
        return literal;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitLiteralExpressionNode(this);
    }

    @Override
    public String toString() {
        return this.literal.toString();
    }

}
