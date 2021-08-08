package com.chess.cochess;

import com.chess.cochess.engine.Stockfish;
import com.chess.cochess.engine.Variation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2005 - 2020 Catalysoft Limited. All rights reserved.
 * Created: 03/08/2021 at 10:39
 */
class StockfishTest {

    @Test
    public void testBestMove() {
        Stockfish stockfish = new Stockfish();
        stockfish.startEngine();
        String bestMove = stockfish.getBestMove("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 500);
        System.out.println(bestMove);
        assertNotNull(bestMove);
    }

    @Test
    public void testBestMoveFromCheckmate() {
        Stockfish stockfish = new Stockfish();
        stockfish.startEngine();
        String bestMove = stockfish.getBestMove("rnbqkbnr/ppppp2p/5p2/6pQ/4P3/2N5/PPPP1PPP/R1B1KBNR b KQkq - 1 3", 500);
        System.out.println(bestMove);
        assertNull(bestMove);
    }

    @Test
    public void getNBestMovesWithoutStartingEngine() {
        Stockfish stockfish = new Stockfish();
        Assertions.assertThrows(IllegalStateException.class, () ->
        stockfish.getNBestMoves(3, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 500));
    }

    @Test
    public void getNBestMoves() {
        Stockfish stockfish = new Stockfish();
        stockfish.startEngine();
        List<Variation> variations = stockfish.getNBestMoves(3, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 500);
        Collections.sort(variations);
        for (Variation v : variations) {
            System.out.println(v);
        }
    }

}