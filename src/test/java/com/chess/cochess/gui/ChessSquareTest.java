package com.chess.cochess.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2005 - 2020 Catalysoft Limited. All rights reserved.
 * Created: 01/08/2021 at 11:57
 */
class ChessSquareTest {

    @Test
    public void testNext() {
        ChessSquare a8 = new ChessSquare(ChessFile.a, 8);
        ChessSquare b8 = new ChessSquare(ChessFile.b, 8);
        ChessSquare h8 = new ChessSquare(ChessFile.h, 8);
        ChessSquare a7 = new ChessSquare(ChessFile.a, 7);
        assertEquals(b8, a8.next());
        // Check the wrap-around works
        assertEquals(a7, h8.next());
    }

    @Test
    public void testIsAt() {
        ChessSquare a1 = new ChessSquare(ChessFile.a, 1);
        assertTrue(a1.isAt(1,1));
    }

    @Test
    public void testName() {
        assertEquals("e4", new ChessSquare(ChessFile.e, 4).name());
    }

    @Test
    public void testFromString() {
        ChessSquare s = ChessSquare.fromString("e4");
        assertEquals(ChessFile.e, s.getFile());
        assertEquals(4, s.getRank());
        ChessSquare s2 = ChessSquare.fromString("G8");
        assertEquals(ChessFile.g, s2.getFile());
        assertEquals(8, s2.getRank());
    }


}