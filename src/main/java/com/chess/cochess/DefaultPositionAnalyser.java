package com.chess.cochess;

import com.chess.cochess.engine.Stockfish;
import com.chess.cochess.engine.Variation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

import java.io.IOException;
import java.util.List;

public class DefaultPositionAnalyser implements PositionAnalyser {

    private final Stockfish stockfish;
    private boolean engineRunning = false;

    public DefaultPositionAnalyser() {
        super();
        stockfish = new Stockfish();
        engineRunning = stockfish.startEngine();
    }

    public Move bestMove(Board board) {
        if (engineRunning) {
            String bestMove = stockfish.getBestMove(board.getFen(), 300);
            if (bestMove == null) {
                return null;
            }
            return new Move(bestMove, board.getSideToMove());
        } else {
            return null;
        }
    }

    public List<Variation> bestNMoves(int n, Board board) {
        if (engineRunning) {
            return stockfish.getNBestMoves(n, board.getFen(), 500);
        } else {
            return null;
        }
    }

    public boolean isCheckmate(Board board) {
        return board.isMated();
    }

    public boolean isDraw(Board board) {
        return board.isDraw();
    }

    public boolean isCheck(Board board) {
        return board.isKingAttacked();
    }

    @Override
    public void shutdown() throws IOException {
        stockfish.stopEngine();
        engineRunning = false;
    }
}
