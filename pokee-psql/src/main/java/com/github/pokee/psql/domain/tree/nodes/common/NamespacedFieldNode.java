package com.github.pokee.psql.domain.tree.nodes.common;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class NamespacedFieldNode extends ParserRuleContext {

    private final TerminalNode namespace;
    private final TerminalNode field;

    public NamespacedFieldNode(final TerminalNode namespace, final TerminalNode field) {
        this.namespace = namespace;
        this.field = field;

        this.addChild(namespace);
        this.addChild(field);
    }

    public TerminalNode getNamespace() {
        return namespace;
    }

    public TerminalNode getField() {
        return field;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitNamespacedFieldNode(this);
    }

}
