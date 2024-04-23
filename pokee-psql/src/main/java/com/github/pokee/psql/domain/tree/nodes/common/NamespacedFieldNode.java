package com.github.pokee.psql.domain.tree.nodes.common;

import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class NamespacedFieldNode extends ParserRuleContext {

    private final TerminalNode namespace;
    private final TerminalNode field;
    private final boolean isWildcard;

    public NamespacedFieldNode(final TerminalNode namespace, final TerminalNode field) {
        this.namespace = namespace;
        this.field = field;

        this.addChild(namespace);
        if (this.field != null) {
            this.addChild(field);
            this.isWildcard = false;
        } else {
            this.isWildcard = true;
        }
    }

    public TerminalNode getNamespace() {
        return namespace;
    }

    public TerminalNode getField() {
        return field;
    }

    public boolean isWildcard() {
        return isWildcard;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitNamespacedFieldNode(this);
    }

    @Override
    public String toString() {
        return this.namespace.getText() + "::" + (this.field == null ? "*" : this.field.getText());
    }

}
