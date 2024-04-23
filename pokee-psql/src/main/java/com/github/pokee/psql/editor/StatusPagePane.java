package com.github.pokee.psql.editor;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.tree.nodes.grammar.impl.StatementContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusPagePane extends JPanel {

    private final JTabbedPane tabbedPane;

    private final List<StatementContext> statements;
    private final Map<String, String> namespaces;

    private final TokenListPanel tokenListPanel;

    private boolean semanticOk;
    private boolean interpreterOk;

    private EditorPane editorPane;

    public StatusPagePane() {
        this.statements = new ArrayList<>();
        this.namespaces = new HashMap<>();
        this.semanticOk = false;
        this.interpreterOk = false;

        this.setLayout(new BorderLayout());

        this.tokenListPanel = new TokenListPanel();

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.addTab("Tokens", this.tokenListPanel);
        this.tabbedPane.addTab("Statements", new JPanel());
        this.tabbedPane.setPreferredSize(new Dimension(400, (int) this.tabbedPane.getPreferredSize().getHeight()));
        this.add(this.tabbedPane, BorderLayout.CENTER);
    }

    public void updateStatements(final List<StatementContext> statements) {
        this.statements.addAll(statements);
    }

    public void updateNamespaces(final Map<String, String> namespaces) {
        this.namespaces.putAll(namespaces);
    }

    public void setSemanticOk(boolean semanticOk) {
        this.semanticOk = semanticOk;
    }

    public void setInterpreterOk(boolean interpreterOk) {
        this.interpreterOk = interpreterOk;
    }

    public void updateTokens(final List<Token> tokens) {
        if (tokens == null) {
            this.tokenListPanel.reset();
            this.tabbedPane.setTitleAt(0, "Tokens (0)");
        } else {
            this.tokenListPanel.updateTokens(tokens);
            this.tabbedPane.setTitleAt(0, "Tokens (" + tokens.size() + ")");
        }
    }

    public void reset() {
        this.updateTokens(null);
        this.statements.clear();
        this.namespaces.clear();
        this.semanticOk = false;
        this.interpreterOk = false;
    }

    public void setEditorPane(EditorPane editorPane) {
        this.tokenListPanel.setEditorPane(editorPane);
    }

}
