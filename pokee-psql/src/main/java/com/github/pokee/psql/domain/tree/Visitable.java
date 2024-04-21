package com.github.pokee.psql.domain.tree;

import com.github.pokee.psql.visitor.Visitor;

public interface Visitable {

    <T> T accept(final Visitor<? extends T> visitor);

}
