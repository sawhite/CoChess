/*
 * Copyright (c) 2005 - 2021 Catalysoft Limited. All rights reserved.
 * Created: 24th July 2010
 */

package com.chess.cochess.gui;

import java.util.NoSuchElementException;

public enum ChessFile {
    a, b, c, d, e, f, g, h;

    private static ChessFile[] values = ChessFile.values();

    public ChessFile next() {
        int thisFile = ordinal();
        int nextOrdinal = thisFile + 1;
        if (nextOrdinal < values.length) {
            return values[nextOrdinal];
        } else {
            throw new NoSuchElementException("No more files on the board");
        }
    }

    public static ChessFile fromInt(int x) {
        assert x > 0;
        assert x <= 8;
        return values[x-1];
    }

    public static ChessFile fromChar(char ch) {
        switch (ch) {
            case 'a' : return a;
            case 'b' : return b;
            case 'c' : return c;
            case 'd' : return d;
            case 'e' : return e;
            case 'f' : return f;
            case 'g' : return g;
            case 'h' : return h;
            default: throw new IllegalArgumentException("Unexpected value '"+ch+"'");
        }
    }
}
