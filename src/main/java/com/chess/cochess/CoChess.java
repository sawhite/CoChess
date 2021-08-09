package com.chess.cochess;

import com.chess.cochess.gui.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CoChess implements ChessSquareListener {

    private static final Logger logger = Logger.getLogger(CoChess.class.getName());

    private ChessDisplay display;

    private Board board;

    private Piece selectedPiece = Piece.NONE;

    private Square selectedSquare = null;

    private List<Move> legalMoves = Collections.emptyList();

    private PositionReporter positionReporter;

    public CoChess() {
        super();
        board = new Board();
        display = new ChessDisplay("Co-Chess");
        display.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Display closing");
                try {
                    positionReporter.shutdown();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });
        display.addSquareSelectionListener(this);
        display.setVisible(true);
        positionReporter = new SimplePositionReporter();
        String fen = board.getFen();
        setPosition(fen);
    }

    /**
     * Something happened on a square of the chess board - such as a piece being selected or released -
     * which we need to handle
     *
     * @param event the event containing the information about what happened
     */
    @Override
    public void handleSquare(ChessSquareEvent event) {
        ChessSquare eventSquare = event.getSquare();
        Square square = Square.valueOf(eventSquare.name().toUpperCase());
        if (event.isMousePressed()) {

            display.removeHighlights();
            Move thisMove = new Move(selectedSquare, square);

            Piece currentPiece = board.getPiece(square);
            // If you click on the same square twice it cancels the selection
            if (legalMoves.contains(thisMove)) {
                System.out.println("Move to " + square);
                board.doMove(thisMove);
                String to = board.getFen();
                display.moveAndAnimate(ChessSquare.fromString(thisMove.getFrom().name()), ChessSquare.fromString(thisMove.getTo().name()), to);
                updatePositionReport();
                selectedPiece = Piece.NONE;
                selectedSquare = null;
                display.setSelectedSquare(null);
            } else if (selectedPiece == Piece.NONE /*|| (currentPiece != null && currentPiece != Piece.NONE && selectedPiece != currentPiece)*/) {
                // Show legal moves if there is no currently selected piece
                legalMoves = legalMovesFromSquare(square);
                if (legalMoves.isEmpty()) {
                    selectedSquare = null;
                    selectedPiece = Piece.NONE;
                } else {
                    for (Move move : legalMoves) {
                        Square toSquare = move.getTo();
                        ChessSquare highlightSquare = ChessSquare.fromString(toSquare.name());
                        logger.info("Possible move to " + toSquare);
                        display.addHighlight(highlightSquare);
                    }
                    System.out.println("Selected " + currentPiece);
                    selectedPiece = currentPiece;
                    selectedSquare = square;
                    display.setSelectedSquare(ChessSquare.fromString(square.name()));
                }
            } else {
                // Cancel the selection
                selectedSquare = null;
                selectedPiece = Piece.NONE;
                legalMoves = Collections.emptyList();
                System.out.println("Selection cancelled");
                display.setSelectedSquare(null);
            }
        }
    }

    private void setPosition(String fen) {
        display.setPosition(fen);
        display.setPositionReport(positionReporter.analyse(board));
    }

    /**
     * Update the position report in a background thread, so the GUI is not delayed
     */
    private void updatePositionReport() {
        new BackgroundPositionReporter().execute();
    }

    // A little SwingWorker class to update the position report in a background thread
    class BackgroundPositionReporter extends SwingWorker<String, Void> {
        @Override
        protected String doInBackground() throws Exception {
            return positionReporter.analyse(board);
        }

        @Override
        protected void done() {
            try {
                String report = get();
                display.setPositionReport(report);
            } catch (Exception e) {
               // ignore
            }
        }
    }

    /**
     * Returns the legal moves for the piece on the given starting square (if any)
     */
    private List<Move> legalMovesFromSquare(Square square) {
        List<Move> allLegalMoves = board.legalMoves();
        return allLegalMoves.stream().filter(m -> m.getFrom().equals(square)).collect(Collectors.toList());
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        File file = new File("CoChess.properties");
        try (FileReader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error while loading licence: "+e.getMessage());
        }
        return properties;
    }

    public static void main(String[] args) {
        Properties props = loadProperties();
        String application = props.getProperty("Application");
        String licenceCode = props.getProperty("Licence");
        if (application != null && licenceCode != null) {
            com.jidesoft.utils.Lm.verifyLicense("Catalysoft Ltd.", application, licenceCode);
        }
        CoChess chess = new CoChess();
    }
}
