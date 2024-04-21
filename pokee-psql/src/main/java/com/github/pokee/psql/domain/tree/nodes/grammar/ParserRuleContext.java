package com.github.pokee.psql.domain.tree.nodes.grammar;

import com.github.pokee.psql.domain.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public abstract class ParserRuleContext implements ParseTree {

    private ParseTree parent;
    private List<ParseTree> children;

    @Override
    public ParseTree getParent() {
        return this.parent;
    }

    @Override
    public void setParent(final ParseTree parent) {
        this.parent = parent;
    }

    @Override
    public String getText() {
        if (this.getChildCount() == 0) {
            return "";
        }
        final StringBuilder bob = new StringBuilder();
        for (int i = 0; i < this.getChildCount(); i++) {
            bob.append(this.getChild(i).getText());
        }
        return bob.toString();
    }

    @Override
    public Object getPayload() {
        return this;
    }

    @Override
    public void addChild(final ParseTree child) {
        child.setParent(this);
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    @Override
    public ParseTree getChild(final int index) {
        if (this.children == null) {
            return null;
        }
        return this.children.get(index);
    }

    @Override
    public int getChildCount() {
        return this.children != null ? this.children.size() : 0;
    }

    @Override
    public String toStringTree() {
        if (this.getChildCount() == 0) {
            return "";
        }

        final StringBuilder bob = new StringBuilder();
        bob.append("( ").append(this.getClass().getSimpleName()).append("(");
        for (int i = 0; i < this.getChildCount(); i++) {
            bob.append(" ").append(this.getChild(i).toStringTree()).append(" ");
        }
        return bob.append(") )").toString();
    }
}
