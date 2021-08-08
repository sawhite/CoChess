package com.chess.cochess.gui;

import javax.swing.*;
import java.awt.*;

public class ChessControlPanel extends JPanel {

    private JLabel label = new JLabel("FEN:");
    private JTextField textField = new JTextField(40);

    public ChessControlPanel() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT));
        textField.setEditable(false);
        add(label);
        add (textField);
        label.setLabelFor(textField);
    }

    public void setPosition(String fen) {
        textField.setText(fen);
    }
}
