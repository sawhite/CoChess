package com.chess.cochess.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessAnalysisPanel extends JPanel {

    private static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    private final JButton setupButton = new JButton("Setup");
    private final JButton playButton = new JButton("Play");

    private JTextArea reportArea;

    private final ChessBoardPanel chessBoardPanel;

    private volatile boolean playing = false;
    private Thread player = null;


    public ChessAnalysisPanel(ChessBoardPanel chessBoard) {
        super(new BorderLayout());
        this.chessBoardPanel = chessBoard;

        reportArea = new JTextArea(10, 40);
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);

        setupButton.setEnabled(false);
        setupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chessBoardPanel.setPosition(START_POSITION);
                setupButton.setEnabled(false);
                playButton.setEnabled(true);
            }
        });

        final Runnable run = new Runnable() {
            public void run() {
                ChessGame game = new ChessGame();
                playMoves(game);
            }
        };

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!playing) {
                    if (player == null) {
                        player = new Thread(run);
                    }
                    playButton.setText("Stop");
                    playing = true;
                    if (!player.isAlive()) {
                        player.start();
                    }
                } else {
                    playButton.setText("Play");
                    playing = false;
                }
            }
        });

        JLabel header = new JLabel("<html><b>Analysis</b></html>", JLabel.CENTER);
        header.setBorder(new EmptyBorder(5,10,5,10));
        add(header, BorderLayout.NORTH);
        add(reportArea, BorderLayout.CENTER);
        // Leave gaps around the edge of the panel to match the shape of the board
        setBorder(new EmptyBorder(0, 0, 40, 40));

        setupButton.setEnabled(true);
        playButton.setText("Play");
        playButton.setEnabled(false);
    }

    public void setPositionReport(String text) {
        reportArea.setText(text);
    }

    public void playMoves(ChessGame game) {
        assert !SwingUtilities.isEventDispatchThread();
        for (String move : game.getMoves()) {
            assert move.length() == 5;
            if ("O-O B".equals(move)) { // Black castling king's side
                chessBoardPanel.moveAndAnimate(ChessSquare.fromString("e8"), ChessSquare.fromString("g8"), null);
                waitForAnimation();
                chessBoardPanel.moveAndAnimate(ChessSquare.fromString("h8"), ChessSquare.fromString("f8"), null);
            } else if ("O-O W".equals(move)) { // White castling king's side
                chessBoardPanel.moveAndAnimate(ChessSquare.fromString("e1"), ChessSquare.fromString("g1"), null);
                waitForAnimation();
                chessBoardPanel.moveAndAnimate(ChessSquare.fromString("h1"), ChessSquare.fromString("f1"), null);
            } else {
                ChessFile fromFile = ChessFile.valueOf(move.substring(0, 1));
                int fromRank = Integer.parseInt(move.substring(1, 2));
                ChessFile toFile = ChessFile.valueOf(move.substring(3, 4));
                int toRank = Integer.parseInt(move.substring(4, 5));
                chessBoardPanel.moveAndAnimate(new ChessSquare(fromFile, fromRank), new ChessSquare(toFile, toRank), null);
            }
            waitForAnimation();
            while (!playing) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
            }
        }

        playing = false;
        player = null;
    }

    private void waitForAnimation() {
        while (chessBoardPanel.isAnimating()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
        }
    }
}
