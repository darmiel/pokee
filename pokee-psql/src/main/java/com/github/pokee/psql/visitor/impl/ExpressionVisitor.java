package com.github.pokee.psql.visitor.impl;

import com.github.pokee.common.fielder.Fielder;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.BinaryExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.NotExpressionNode;
import com.github.pokee.psql.exception.ExpressionException;
import com.github.pokee.psql.query.NamespaceValues;
import com.github.pokee.psql.query.QueryExecutor;
import com.github.pokee.psql.query.QueryFilterFunction;
import com.github.pokee.psql.visitor.AliasNamespacedBasePsqlVisitor;

import java.util.Map;
import java.util.function.Predicate;

public class ExpressionVisitor extends AliasNamespacedBasePsqlVisitor<Predicate<Fielder>> {


    private final Map<String, NamespaceValues> namespaceValues;

    public ExpressionVisitor(final Map<String, String> importedNamespaces,
                             final Map<String, NamespaceValues> namespaceValues) {
        super(importedNamespaces);

        this.namespaceValues = namespaceValues;
    }


    @Override
    public Predicate<Fielder> visitNotExpressionNode(final NotExpressionNode notExpressionNode) {
        return Predicate.not(notExpressionNode.getExpression().accept(this));
    }

    @Override
    public Predicate<Fielder> visitBinaryExpressionNode(final BinaryExpressionNode binaryExpressionNode) {
        final Predicate<Fielder> left = binaryExpressionNode.getLhs().accept(this);
        if (left == null) {
            throw new ExpressionException("Left side of binary expression is null");
        }
        final Predicate<Fielder> right = binaryExpressionNode.getRhs().accept(this);
        if (right == null) {
            throw new ExpressionException("Right side of binary expression is null");
        }

        return switch (binaryExpressionNode.getOperator()) {
            case AND -> left.and(right);
            case OR -> left.or(right);
            default -> throw new ExpressionException("Unsupported operator: " + binaryExpressionNode.getOperator());
        };
    }

    @Override
    public Predicate<Fielder> visitFunctionCallExpressionNode(final FunctionCallExpressionNode functionNode) {
        // check if values for this namespace are passed
        final String namespace = this.resolveNamespace(functionNode.getTarget().getNamespace().getText());
        final NamespaceValues namespaceValues = this.namespaceValues.get(namespace);
        if (namespaceValues == null) {
            throw new ExpressionException("Values for namespace " + namespace + " not passed.");
        }

        // check if the requested field is in the values
        final String field = functionNode.getTarget().getField().getText();
        if (!namespaceValues.dummy().hasField(functionNode.getTarget().getField().getText())) {
            throw new ExpressionException("Field " + field + " not found in namespace " + namespace);
        }

        final Object dummyValue = namespaceValues.dummy().getField(field);
        if (dummyValue == null) {
            throw new ExpressionException("Dummy field " + field + " was null in namespace " + namespace);
        }

        final Map<String, QueryFilterFunction> fieldFunctionMap = QueryExecutor.getFunctions().get(dummyValue.getClass());
        if (fieldFunctionMap == null) {
            throw new ExpressionException("No functions found for class " + dummyValue.getClass());
        }

        final QueryFilterFunction function = fieldFunctionMap.get(functionNode.getFunctionName().toLowerCase());
        if (function == null) {
            throw new ExpressionException("Function " + functionNode.getFunctionName() +
                    " not found for class " + dummyValue.getClass());
        }

        // check if the number of arguments is correct
        if (function.argumentTypes().size() != functionNode.getArguments().size()) {
            throw new ExpressionException("Function " + functionNode.getFunctionName() +
                    " expects " + function.argumentTypes().size() + " arguments, but got " + functionNode.getArguments().size());
        }

        // check if the types of the arguments are correct
        for (int i = 0; i < functionNode.getArguments().size(); i++) {
            final TerminalNode argument = functionNode.getArguments().get(i);
            final TokenType expectedType = function.argumentTypes().get(i);
            final TokenType actualType = argument.getSymbol().type();
            if (expectedType != actualType) {
                throw new ExpressionException("Function " + functionNode.getFunctionName() +
                        " expects argument " + i + " to be of type " + expectedType + ", but got " + actualType);
            }
        }

        return t -> {
            final Object value = t.getField(field);
            if (value == null) {
                return false;
            }
            return function.runner().execute(value, functionNode.getArguments());
        };
    }

}
