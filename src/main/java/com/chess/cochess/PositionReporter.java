package com.chess.cochess;

import com.github.bhlangonijr.chesslib.Board;

import java.io.IOException;

public interface PositionReporter {
    String analyse(Board board);
    void shutdown() throws IOException;
}
