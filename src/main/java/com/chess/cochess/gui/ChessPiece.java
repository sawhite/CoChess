/*
 * Copyright (c) 2005 - 2021 Catalysoft Limited. All rights reserved.
 * This class is adapted from code first written July 2010
 */

package com.chess.cochess.gui;

import com.jidesoft.range.Positionable;

/**
 * The type of chess piece will be used as the value for the z axis
 * This class wraps an ChessPieceType value for the type of the piece and
 * implements Positionable so it can be placed on a Chart
 */
class ChessPiece implements Positionable {
    private ChessPieceType piece;

    public ChessPiece(ChessPieceType piece) {
        this.piece = piece;
    }

    public ChessPieceType getPieceType() {
        return piece;
    }

    public double position() {
        return piece.ordinal();
    }

    public int compareTo(Positionable o) {
        double pos = position();
        double oPos = o.position();
        if (pos == oPos) {
            return 0;
        } else {
            return pos < oPos ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessPiece that = (ChessPiece) o;

        if (this.piece != that.piece) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return piece != null ? piece.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "piece=" + piece +
                '}';
    }

}
