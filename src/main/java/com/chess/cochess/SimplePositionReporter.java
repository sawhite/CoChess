package com.chess.cochess;

import com.chess.cochess.engine.Variation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.io.IOException;
import java.util.List;

public class SimplePositionReporter implements PositionReporter {

    private PositionAnalyser positionAnalyser;

    public SimplePositionReporter() {
        super();
        positionAnalyser = new DefaultPositionAnalyser();
    }

    @Override
    public String analyse(Board board) {
        StringBuilder builder = new StringBuilder();
        Integer moveNumber = board.getMoveCounter();
        builder.append("Move ").append(moveNumber).append('\n');
        Side side = board.getSideToMove();
        builder.append(side).append(" to move\n\n");
        builder.append(gameStatusString(board));
        if (board.legalMoves().isEmpty()) {
            builder.append("Game Over");
        } else {
            Move bestMove = positionAnalyser.bestMove(board);
            if (bestMove != null) {
                builder.append("Consider: ").append(bestMove);
                builder.append('\n');
                List<Variation> variations = positionAnalyser.bestNMoves(3, board);
                int variationCount = 1;
                for (Variation v : variations) {
                    StringBuilder b = new StringBuilder();
                    b.append(variationCount);
                    b.append(". ");
                    List<String> moves = v.getMoves();
                    for (String move : moves) {
                        b.append(move);
                        b.append(' ');
                    }
                    b.append('\n');
                    variationCount++;
                    builder.append(b);
                }
            }
        }
        return builder.toString();
    }

    private String gameStatusString(Board board) {
        if (positionAnalyser.isCheckmate(board)) {
            return "+++ Checkmate! +++\n\n";
        } else if (positionAnalyser.isDraw(board)) {
            return "+++ Draw! +++\n\n";
        }
        else if (positionAnalyser.isCheck(board)) {
            return "+-+ Check! +-+\n\n";
        }
        return "";
    }

    @Override
    public void shutdown() throws IOException {
        positionAnalyser.shutdown();
    }
}
