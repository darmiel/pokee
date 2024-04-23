package com.github.pokee.psql.domain.tree.nodes.grammar.impl;

import com.github.pokee.psql.domain.tree.nodes.common.TerminalNode;
import com.github.pokee.psql.domain.tree.nodes.grammar.ParserRuleContext;
import com.github.pokee.psql.visitor.Visitor;

public class LanguageContext extends ParserRuleContext {

    private final TerminalNode language;

    public LanguageContext(final TerminalNode language) {
        this.language = language;
        this.addChild(language);
    }

    public TerminalNode getLanguage() {
        return language;
    }

    @Override
    public <T> T accept(final Visitor<? extends T> visitor) {
        return visitor.visitLanguage(this);
    }

    @Override
    public String toString() {
        return "LanguageContext{" +
                "language=" + language.getText() +
                '}';
    }

}
