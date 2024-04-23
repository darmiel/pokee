package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.common.NamespacedFieldNode;
import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

/**
 * Node representing a projection in the query.
 * <pre>
 *     query all_full
 *         Pokemon::{id, name as n}
 *         │^^^^^^   │^  │^^^    │
 *         │         │   │       └▶ Field Alias
 *         │         └───┴─▶ Fields
 *         └─▶ Namespace
 */
public class ProjectionNode extends ParserRuleContext {

    private final NamespacedFieldNode field;
    private final TerminalNode alias;

    /**
     * Constructor for a projection node.
     *
     * @param field the namespace + field name of the projection.
     * @param alias the alias of the projection. this can be null.
     */
    public ProjectionNode(final NamespacedFieldNode field,
                          final TerminalNode alias) {
        this.field = field;
        this.alias = alias;

        this.addChild(field);
        if (alias != null) {
            this.addChild(alias);
        }
    }

    /**
     * The namespace + field name of the projection.
     * <pre>
     *     query all Pokemon::{name as n};
     *               ^^^^^^^&&&^^^^
     * </pre>
     *
     * @return the namespace and fieldz of the projection.
     */
    public NamespacedFieldNode getField() {
        return field;
    }

    /**
     * The alias of the projection.
     * <pre>
     *     query all Pokemon::{name as n};
     *                                 ^
     * </pre>
     *
     * @return the alias of the projection.
     */
    public TerminalNode getAlias() {
        return alias;
    }

    /**
     * Check if the projection is a wildcard.
     *
     * @return true if the projection is a wildcard, false otherwise.
     */
    public boolean isAll() {
        return this.field.isWildcard();
    }

    /**
     * Check if the projection has an alias.
     *
     * @return true if the projection has an alias, false otherwise.
     */
    public boolean hasAlias() {
        return this.alias != null;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitProjection(this);
    }

    @Override
    public String toString() {
        return "ProjectionNode{" +
                "\n, field=" + this.field +
                "\n, alias=" + (alias != null ? alias.getText() : "none") +
                "\n, isAll=" + this.isAll() +
                "\n}";
    }

}
