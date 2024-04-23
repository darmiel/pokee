package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.ParseTree;
import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.BinaryExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.IdentifierExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.LiteralExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.*;

public class BasePsqlVisitor<T> implements Visitor<T> {

    @Override
    public T visitTerminalNode(final TerminalNode terminalNode) {
        return this.visitChildren(terminalNode);
    }

    @Override
    public T visitUseStatement(final UseStatementContext useStatementContext) {
        return this.visitChildren(useStatementContext);
    }

    @Override
    public T visitStatement(final StatementContext statementContext) {
        return this.visitChildren(statementContext);
    }

    @Override
    public T visitQuery(final QueryContext queryContext) {
        return this.visitChildren(queryContext);
    }

    @Override
    public T visitProjection(final ProjectionNode projectionNode) {
        return this.visitChildren(projectionNode);
    }

    @Override
    public T visitBinaryExpressionNode(final BinaryExpressionNode binaryExpressionNode) {
        return this.visitChildren(binaryExpressionNode);
    }

    @Override
    public T visitFunctionCallExpressionNode(final FunctionCallExpressionNode functionCallExpressionNode) {
        return this.visitChildren(functionCallExpressionNode);
    }

    @Override
    public T visitLiteralExpressionNode(final LiteralExpressionNode literalExpressionNode) {
        return this.visitChildren(literalExpressionNode);
    }

    @Override
    public T visitIdentifierExpressionNode(final IdentifierExpressionNode identifierExpressionNode) {
        return this.visitChildren(identifierExpressionNode);
    }

    @Override
    public T visitProgram(final ProgramContext programContext) {
        return this.visitChildren(programContext);
    }

    @Override
    public T visitNamespacedFieldNode(final NamespacedFieldNode namespacedFieldNode) {
        return this.visitChildren(namespacedFieldNode);
    }

    @Override
    public T visitLanguage(final LanguageContext languageContext) {
        return this.visitChildren(languageContext);
    }

    public T visitChildren(final ParseTree node) {
        T result = defaultResult();
        for (int i = 0; i < node.getChildCount(); i++) {
            final ParseTree child = node.getChild(i);
            result = child.accept(this);
        }
        return result; // return the last accept result of the children list.
    }

    protected T defaultResult() {
        return null;
    }

}
