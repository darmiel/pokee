package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.expression.BinaryExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.FunctionCallExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.IdentifierExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.expression.LiteralExpressionNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.*;

public interface Visitor<T> {

    T visitTerminalNode(final TerminalNode terminalNode);

    T visitUseStatement(final UseStatementContext useStatementContext);

    T visitStatement(final StatementContext statementContext);

    T visitQuery(final QueryContext queryContext);

    T visitProjection(final ProjectionNode projectionNode);

    T visitBinaryExpressionNode(final BinaryExpressionNode binaryExpressionNode);

    T visitFunctionCallExpressionNode(final FunctionCallExpressionNode functionCallExpressionNode);

    T visitLiteralExpressionNode(final LiteralExpressionNode literalExpressionNode);

    T visitIdentifierExpressionNode(final IdentifierExpressionNode identifierExpressionNode);

    T visitProgram(final ProgramContext programContext);

    T visitNamespacedFieldNode(final NamespacedFieldNode namespacedFieldNode);

    T visitLanguage(final LanguageContext languageContext);
}
