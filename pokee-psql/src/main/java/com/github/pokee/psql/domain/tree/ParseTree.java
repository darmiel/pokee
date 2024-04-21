package com.github.pokee.psql.domain.tree;

public interface ParseTree extends Visitable {

    ParseTree getParent();

    void setParent(final ParseTree parent);

    String getText();

    Object getPayload();

    void addChild(final ParseTree child);

    ParseTree getChild(final int index);

    int getChildCount();

    String toStringTree();

}
