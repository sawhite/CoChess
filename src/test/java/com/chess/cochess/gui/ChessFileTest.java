package com.chess.cochess.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChessFileTest {

    @Test
    public void testNext() {
        assertEquals(ChessFile.b, ChessFile.a.next());
        assertEquals(ChessFile.c, ChessFile.b.next());
        assertEquals(ChessFile.d, ChessFile.c.next());
        assertEquals(ChessFile.e, ChessFile.d.next());
        assertEquals(ChessFile.f, ChessFile.e.next());
        assertEquals(ChessFile.g, ChessFile.f.next());
        assertEquals(ChessFile.h, ChessFile.g.next());
    }

    @Test
    public void testNextFromFileH() {
        Assertions.assertThrows(NoSuchElementException.class, ChessFile.h::next);
    }

}