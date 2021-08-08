package com.chess.cochess.gui;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ChessSquare {
    private ChessFile file;
    private int rank;

    public ChessSquare(@Nonnull ChessFile file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public ChessFile getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public boolean isAt(int file, int rank) {
        return (this.file.ordinal()+1) == file && this.rank == rank;
    }

    /**
     * Returns the name of the chess square, such as 'e4'
     */
    public String name() {
        return file.name() + rank;
    }

    public static ChessSquare fromString(@Nonnull String str) {
        if (str.length() != 2) {
            throw new IllegalArgumentException("Expected a string of length 2 to specify the square, e.g. 'E4'");
        }
        char fileChar = str.toLowerCase().charAt(0);
        char rankChar = str.charAt(1);
        ChessFile file = ChessFile.fromChar(fileChar);
        int rank = rankChar - '1' + 1;
        return new ChessSquare(file, rank);
    }

    /**
     * Given a chess square (this object), return the next square in the same rank,
     * or if at the end of the rank, the first square in the next rank. Ranks are
     * iterated from top to bottom (8 to 1) as in the FEN position format.
     * @return a ChessSquare to follow this one
     */
    public ChessSquare next() {
        ChessFile newFile;
        int newRank = rank;
        if (file == ChessFile.h) {
            newFile = ChessFile.a;
            newRank -= 1;
            if (newRank < 1) {
                newRank = 8;
            }
        } else {
            newFile = file.next();
        }
        return new ChessSquare(newFile, newRank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessSquare that = (ChessSquare) o;
        return rank == that.rank && file == that.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return "ChessSquare{" +
                "file=" + file +
                ", rank=" + rank +
                '}';
    }
}
