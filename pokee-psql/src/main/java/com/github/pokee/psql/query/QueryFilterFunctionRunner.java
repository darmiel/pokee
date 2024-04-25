package com.github.pokee.psql.query;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;

import java.util.List;

public interface QueryFilterFunctionRunner {

    boolean execute(final Object value, final List<TerminalNode> arguments);

}