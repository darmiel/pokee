package com.github.pokee.psql.domain.tree.nodes.common;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.tree.ParseTree;
import com.github.pokee.psql.visitor.Visitor;

public class TerminalNode implements ParseTree {

    protected ParseTree parent;
    protected Token symbol;

    @Override
    public ParseTree getParent() {
        return this.parent;
    }

    @Override
    public void setParent(final ParseTree parent) {
        this.parent = parent;
    }

    public void setSymbol(final Token symbol) {
        this.symbol = symbol;
    }

    public Token getSymbol() {
        return this.symbol;
    }

    @Override
    public Object getPayload() {
        return this.symbol;
    }

    @Override
    public String getText() {
        if (this.symbol == null) {
            return "";
        }
        return this.symbol.value();
    }

    @Override
    public void addChild(final ParseTree child) {
        // Do nothing
    }

    @Override
    public ParseTree getChild(final int index) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public String toStringTree() {
        return this.getText();
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitTerminalNode(this);
    }

    @Override
    public String toString() {
        return this.getText();
    }

}
