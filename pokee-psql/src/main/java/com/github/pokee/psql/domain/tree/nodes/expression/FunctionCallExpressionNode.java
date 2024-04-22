package com.github.pokee.psql.domain.tree.nodes.expression;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.visitor.Visitor;

import java.util.List;

public class FunctionCallExpressionNode extends ExpressionNode {

    private final TerminalNode targetNamespace;
    private final TerminalNode target;
    private final String functionName;
    private final List<TerminalNode> arguments;

    public FunctionCallExpressionNode(final TerminalNode targetNamespace,
                                      final TerminalNode target,
                                      final String functionName,
                                      final List<TerminalNode> arguments) {
        this.targetNamespace = targetNamespace;
        this.target = target;
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public TerminalNode getTargetNamespace() {
        return targetNamespace;
    }

    public TerminalNode getTarget() {
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
        return this.targetNamespace + "::" + this.target + "." + this.functionName + "(" + this.arguments + ")";
    }

}

