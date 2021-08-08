package com.chess.cochess.gui;

import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.ChartPoint3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenParserTest {
    private static final String startPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private FenParser parser;

    @BeforeEach
    public void setup() {
        parser = new FenParser();
    }

    @Test
    public void testGetPosition() {
        String position = parser.getPosition(startPos);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", position);
    }

    @Test
    public void testCreateBoard1() {
        ChartModel model = parser.convert(startPos);
        assertNotNull(model);
        ChartPoint3D blackRook = (ChartPoint3D) model.getPoint(0);
        assertEquals(1, blackRook.getX().position(), 0.01);
        assertEquals(8, blackRook.getY().position(), 0.01);
    }

    @Test
    public void testEmptySquares() {
        assertEquals(1, FenParser.emptySquares('1'));
        assertEquals(2, FenParser.emptySquares('2'));
        assertEquals(3, FenParser.emptySquares('3'));
        assertEquals(4, FenParser.emptySquares('4'));
        assertEquals(5, FenParser.emptySquares('5'));
        assertEquals(6, FenParser.emptySquares('6'));
        assertEquals(7, FenParser.emptySquares('7'));
        assertEquals(8, FenParser.emptySquares('8'));
    }

}