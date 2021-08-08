package com.chess.cochess.gui;

import com.chess.cochess.util.Converter;
import com.jidesoft.chart.model.ChartPoint3D;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.RealPosition;
import com.jidesoft.range.CategoryRange;

/**
 * Translates a FEN string to a ChartModel
 */
public class FenParser implements Converter<String, DefaultChartModel> {

    private final CategoryRange<ChessFile> fileRange = new CategoryRange<ChessFile>(ChessFile.values());

    @Override
    public DefaultChartModel convert(String fen) {
        DefaultChartModel model = new DefaultChartModel();
        if (fen == null) {
            return model;
        }
        String position = getPosition(fen);
        ChessSquare square = new ChessSquare(ChessFile.a, 8);

        for (int i=0; i<position.length(); i++) {
            char ch = position.charAt(i);
            ChessPieceType pieceType = ChessPieceType.fromChar(ch);
            if (pieceType == ChessPieceType.NONE) {
                int emptySquares = emptySquares(ch);
                for (int j=0; j < emptySquares; j++) {
                    square = square.next();
                }
            } else {
                addPiece(model, square, pieceType);
                square = square.next();
            }
        }

        return model;
    }

    /**
     * Translates a character into a number
     * @param ch a character between '1' and '8' (inclusive)
     * @return an int between 1 and 8
     */
    static int emptySquares(char ch) {
        return (ch - '1') + 1;
    }

    /**
     * Adds the given piece to the given square in the given model
     */
    private void addPiece(DefaultChartModel model, ChessSquare square, ChessPieceType pieceType) {
        ChartPoint3D piece = new ChartPoint3D();
        piece.setName(pieceType.toString());
        ChessFile f = square.getFile();
        int rank = square.getRank();
        piece.setX(fileRange.getCategoryValues().get(f.ordinal()));
        piece.setY(new RealPosition(rank));
        piece.setZ(new ChessPiece(pieceType));
        model.addPoint(piece);
    }

    /**
     * Returns the position (first word) of the FEN string
     */
    public String getPosition(String fen) {
        String positionWord = fen.split(" ")[0];
        return positionWord;
    }
}



