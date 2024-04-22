package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.BinaryExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.IdentifierExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.LiteralExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.ProjectionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.QueryContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.StatementContext;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.UseAliasContext;

public interface Visitor<T> {

    T visitTerminal(final TerminalNode terminalNode);

    T visitUseAlias(final UseAliasContext useAliasContext);

    T visitStatement(final StatementContext statementContext);

    T visitQuery(final QueryContext queryContext);

    T visitProjection(final ProjectionNode projectionNode);

    T visitBinaryExpressionNode(final BinaryExpressionNode binaryExpressionNode);

    T visitFunctionCallExpressionNode(final FunctionCallExpressionNode functionCallExpressionNode);

    T visitLiteralExpressionNode(final LiteralExpressionNode literalExpressionNode);

    T visitIdentifierExpressionNode(final IdentifierExpressionNode identifierExpressionNode);
}
