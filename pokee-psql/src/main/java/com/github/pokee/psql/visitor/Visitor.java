package com.github.pokee.psql.visitor;

import com.github.pokee.psql.domain.tree.nodes.grammar.impl.*;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;

public interface Visitor<T> {

    T visitTerminal(final TerminalNode terminalNode);

    T visitUseAlias(final UseAliasContext useAliasContext);

    T visitStatement(final StatementContext statementContext);

    T visitQuery(final QueryContext queryContext);

    T visitProjection(final ProjectionNode projectionNode);

    T visitExpression(final FunctionNode functionNode);
}
