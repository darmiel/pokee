package com.github.pokee.psql.editor;

import com.github.pokee.psql.domain.token.Token;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TokenListPanel extends JPanel {

    private final JList<Token> tokenList;
    private final DefaultListModel<Token> tokenListModel;

    private EditorPane editorPane;

    public TokenListPanel() {
        this.setLayout(new BorderLayout());

        this.tokenListModel = new DefaultListModel<>();
        this.tokenList = new JList<>(tokenListModel);
        this.add(new JScrollPane(tokenList), BorderLayout.CENTER);

        this.tokenList.addListSelectionListener(e -> {
            if (tokenList.getSelectedValue() == null) {
                return;
            }
            if (editorPane != null) {
                editorPane.setSelectedToken(tokenList.getSelectedValue());
            }
        });
    }

    public void setEditorPane(EditorPane editorPane) {
        this.editorPane = editorPane;
    }

    void updateTokens(final List<Token> tokens) {
        this.tokenListModel.clear();
        tokens.forEach(this.tokenListModel::addElement);
    }

    public void reset() {
        this.tokenListModel.clear();
    }

}
