package com.github.pokee.psql.editor;

import javax.swing.*;
import java.awt.*;

public class CodeEditorFrame extends JFrame {

    private final StatusPagePane statusPage;
    private final EditorPane editorPane;
    private final ErrorPane errorPane;

    public CodeEditorFrame() {
        super("PSQL IDE");

        this.setLayout(new BorderLayout());

        this.errorPane = new ErrorPane();
        this.add(this.errorPane, BorderLayout.SOUTH);

        this.statusPage = new StatusPagePane();
        this.add(statusPage, BorderLayout.EAST);

        this.editorPane = new EditorPane(this.statusPage, this.errorPane);
        this.add(this.editorPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null); // Center the window
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CodeEditorFrame::new);
    }

}
