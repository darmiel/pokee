package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.visitor.Visitor;

public class BinaryExpressionNode extends ExpressionNode {

    private final ExpressionNode lhs;
    private final ExpressionNode rhs;
    private final TokenType operator;

    public BinaryExpressionNode(final ExpressionNode lhs,
                                final ExpressionNode rhs,
                                final TokenType operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;

        this.addChild(lhs);
        this.addChild(rhs);
    }

    public ExpressionNode getRhs() {
        return rhs;
    }

    public ExpressionNode getLhs() {
        return lhs;
    }

    public TokenType getOperator() {
        return operator;
    }

    @Override
    public <T> T accept(Visitor<? extends T> visitor) {
        return visitor.visitBinaryExpressionNode(this);
    }

    @Override
    public String toString() {
        return "Bin[" + this.lhs + " " + this.operator + " " + this.rhs + "]";
    }

}
