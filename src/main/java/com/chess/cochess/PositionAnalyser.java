package com.chess.cochess;

import com.chess.cochess.engine.Variation;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface PositionAnalyser {

    @Nullable
    Move bestMove(Board board);

    List<Variation> bestNMoves(int n, Board board);

    boolean isCheckmate(Board board);
    boolean isCheck(Board board);
    boolean isDraw(Board board);

    void shutdown() throws IOException;
}
