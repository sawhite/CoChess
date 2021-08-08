package com.chess.cochess.gui;

import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;

public class ChessDisplay extends JFrame {

    static {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
    }

    private final ChessBoardPanel chessBoardPanel;
    private final ChessAnalysisPanel analysisPanel;
    private final ChessControlPanel controlPanel;

    public ChessDisplay(String frameName) throws HeadlessException {
        super(frameName);
        chessBoardPanel = new ChessBoardPanel();
        analysisPanel = new ChessAnalysisPanel(chessBoardPanel);
        controlPanel = new ChessControlPanel();

        JPanel panel = new JPanel();
        panel.add(chessBoardPanel);
        panel.add(analysisPanel);
        panel.add(controlPanel);
        GridBagLayout layout = new GridBagLayout();
        layout.setConstraints(chessBoardPanel, new GridBagConstraints(1,1,1,1,0.5,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
        layout.setConstraints(analysisPanel, new GridBagConstraints(2,1,1,1,0.5,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
        layout.setConstraints(controlPanel, new GridBagConstraints(1,2,2,1,1.0,0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));
        panel.setLayout(layout);
        setContentPane(panel);
        setSize(1150, 575);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    // Expose and Delegate important methods to the display panel

    public void setPosition(String fen) {
        chessBoardPanel.setPosition(fen);
        controlPanel.setPosition(fen);
    }

    public void addHighlight(ChessSquare square) {
        chessBoardPanel.addHighlight(square);
    }

    public void removeHighlights() {
        chessBoardPanel.removeHighlights();
    }

    public void removeHighlight(ChessSquare square) {
        chessBoardPanel.removeHighlight(square);
    }

    public void addSquareSelectionListener(ChessSquareListener listener) {
        chessBoardPanel.addSquareSelectionListener(listener);
    }

    public void removeSquareSelectionListener(ChessSquareListener listener) {
        chessBoardPanel.removeSquareSelectionListener(listener);
    }

    public void setSelectedSquare(ChessSquare square) {
        chessBoardPanel.setSelectedSquare(square);
    }

    public void moveAndAnimate(ChessSquare from, ChessSquare to, String endFen) {
        chessBoardPanel.moveAndAnimate(from, to, endFen);
        controlPanel.setPosition(endFen);
    }

    public void setPositionReport(String text) {
        analysisPanel.setPositionReport(text);
    }

}
