package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.visitor.Visitor;

import java.util.List;

public class FunctionCallExpressionNode extends ExpressionNode {

    private final NamespacedFieldNode target;
    private final String functionName;
    private final List<TerminalNode> arguments;

    public FunctionCallExpressionNode(final NamespacedFieldNode target,
                                      final String functionName,
                                      final List<TerminalNode> arguments) {
        this.target = target;
        this.functionName = functionName;
        this.arguments = arguments;

        this.addChild(target);
        arguments.forEach(this::addChild);
    }

    public NamespacedFieldNode getTarget() {
        return target;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<TerminalNode> getArguments() {
        return arguments;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitFunctionCallExpressionNode(this);
    }

    @Override
    public String toString() {
        return "F[" + this.target + "." + this.functionName + "(" + this.arguments + ")]";
    }

}

