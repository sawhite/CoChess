package com.chess.cochess.gui;

/*
 *
 * Adapted from code first written 24-Jul-2010
 *
 * Copyright 2005 - 2020 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.RectangularRegionMarker;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.render.PointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.Positionable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.awt.Color.yellow;

@SuppressWarnings("serial")
public class ChessBoardPanel extends JPanel {
    private static final Color highlightColor = new Color(0, 255, 0, 128);
    private final Integer[] ranks = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
    private final CategoryRange<ChessFile> fileRange = new CategoryRange<ChessFile>(ChessFile.values());
    private final CategoryRange<Integer> rankRange = new CategoryRange<Integer>(ranks);
    private final Color light = new Color(255, 207, 144);
    private final Color dark = new Color(143, 96, 79);
    private final Chart chart;
    private ChartPoint3D movingPoint = null;
    private int updatedPointIndex = -1;
    final ChartStyle pointsOnly = new ChartStyle().withPoints();
    private boolean animating = false;

    private ChessSquare selectedSquare = null;
    private List<ChessSquare> highlights = new ArrayList<>();
    private List<ChessSquareListener> chessSquareListeners = new CopyOnWriteArrayList<>();

    private FenParser fenParser = new FenParser();

    public ChessBoardPanel() {
        super();
        setLayout(new BorderLayout());

        chart = new Chart();
        Dimension size = new Dimension(500, 500);
        chart.setBorder(new EmptyBorder(25,25,25,25));
        chart.setPreferredSize(size);
        chart.setShadowVisible(true);
        chart.setMaximumSize(size);
        chart.setMinimumSize(size);
        CategoryAxis<ChessFile> xAxis = new CategoryAxis<>(fileRange);
        CategoryAxis<Integer> yAxis = new CategoryAxis<Integer>(rankRange);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setTickLength(0);
        chart.setChartBackground(light);
        chart.setChartBorder(new LineBorder(Color.DARK_GRAY));
        // Adjust the minima and maxima to tidy up unnecessary space around the edge of the board
        fileRange.setMinimum(0.5);
        fileRange.setMaximum(8.5);
        rankRange.setMinimum(0.5);
        rankRange.setMaximum(8.5);
        setupSquares();

        chart.setPointRenderer(new PointRenderer() {
            public Shape renderPoint(Graphics g, Chart chart, ChartModel m, Chartable point, boolean isSelected,
                                     boolean hasRollover, boolean hasFocus, int x, int y) {
                ChartPoint3D p;
                if (point instanceof ChartPoint3D) {
                    p = (ChartPoint3D) point;
                } else {
                    ModelMorpher.TransitionPoint tp = (ModelMorpher.TransitionPoint) point;
                    p = (ChartPoint3D) tp.getSource();
                }
                ChessPiece piece = (ChessPiece) p.getZ();
                ChessPieceType pieceType = piece.getPieceType();
                int pieceSize = pieceType.getSize();
                g.drawImage(pieceType.getImage(), x - pieceSize / 2, y - pieceSize / 2, chart);
                return null;
            }
        });

        chart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireSelection(e, MouseEvent.MOUSE_CLICKED);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                fireSelection(e, MouseEvent.MOUSE_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                fireSelection(e, MouseEvent.MOUSE_RELEASED);
            }

            private void fireSelection(MouseEvent e, int eventType) {
                Point p = e.getPoint();
                Point2D userPoint = chart.calculateUserPoint(p);
                ChessFile file = ChessFile.fromInt(getChessCoordinate(userPoint.getX()));
                int rank = getChessCoordinate(userPoint.getY());
                ChessSquareEvent event = new ChessSquareEvent(this, new ChessSquare(file, rank), eventType);
                fireSquareSelectionEvent(event);
            }

            // Converts from a double to an int that is in the range [1..8]
            private int getChessCoordinate(double chartCoordinate) {
                int coordinate = (int) Math.round(chartCoordinate);
                // These conditions shouldn't happen, but it is defensive programming
                // to make sure the values are in the expected range
                if (coordinate < 1) {
                    coordinate = 1;
                }
                if (coordinate > 8) {
                    coordinate = 8;
                }
                return coordinate;
            }
        });

        add(chart, BorderLayout.CENTER);
    }

    protected void fireSquareSelectionEvent(ChessSquareEvent event) {
        for (ChessSquareListener listener : chessSquareListeners) {
            listener.handleSquare(event);
        }
    }

    private void setupSquares() {
        boolean[][] highlighted = new boolean[8][8];
        for (ChessSquare square : this.highlights) {
            int x = square.getFile().ordinal();
            int y = square.getRank() - 1;
            highlighted[x][y] = true;
        }
        chart.removeDrawables();
        // Set up the black and white squares
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Color fill = (i + j) % 2 == 0 ? dark : light;

                if (fill != light) {
                    chart.addDrawable(new RectangularRegionMarker(chart, i - 0.5, i + 0.5, j - 0.5, j + 0.5, fill));
                }

                // Set up any highlighted squares
                if (highlighted[i-1][j-1]) {
                    fill = highlightColor;
                    float highlightSize = 0.5f; // Cover half the width and half the height of a square (quarter area) for a highlight
                    chart.addDrawable(new RectangularRegionMarker(chart, i - highlightSize/2, i + highlightSize/2, j - highlightSize/2, j + highlightSize/2, fill));
                } else if (selectedSquare != null && selectedSquare.isAt(i, j)) {
                    fill = yellow;
                    float highlightSize = 0.9f; // Cover half the width and half the height of a square (quarter area) for a highlight
                    chart.addDrawable(new RectangularRegionMarker(chart, i - highlightSize/2, i + highlightSize/2, j - highlightSize/2, j + highlightSize/2, fill));
                }
            }
        }
    }

    public void moveAndAnimate(ChessSquare from, ChessSquare to, String endFen) {
        moveAndAnimatePiece((DefaultChartModel) chart.getModel(), from, to, endFen);
    }

    private DefaultChartModel moveAndAnimatePiece(final DefaultChartModel model, ChessSquare from, ChessSquare to) {
        return moveAndAnimatePiece(model, from, to, null);
    }

    private DefaultChartModel moveAndAnimatePiece(final DefaultChartModel model, ChessSquare from, ChessSquare to, String endFen) {
        final DefaultChartModel newModel = new DefaultChartModel(model);
        DefaultChartModel finalModel = endFen == null ? newModel : fenParser.convert(endFen);
        final ChartPoint3D pieceToTake = pieceAt(model, to.getFile(), to.getRank());
        movePiece(newModel, from, to);
        animating = true;
        final ModelMorpher morpher = new ModelMorpher(chart, 10, 40);
        morpher.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (ModelMorpher.PROPERTY_MORPH_ENDED.equals(evt.getPropertyName())) {
                    assert SwingUtilities.isEventDispatchThread();
                    if (pieceToTake != null) {
                        int sizeBefore = newModel.getPointCount();
                        newModel.removePoint(pieceToTake);
                        assert newModel.getPointCount() + 1 == sizeBefore;
                    }
                    chart.setModel(finalModel, pointsOnly);
                    animating = false;
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chart.removeModel(model);
                morpher.morph(model, pointsOnly, newModel, pointsOnly);
            }
        });

        return newModel;
    }

    public boolean isAnimating() {
        return animating;
    }

    private void movePiece(final DefaultChartModel model, ChessSquare from, ChessSquare to) {
        ChessFile fromFile = from.getFile();
        int fromRank = from.getRank();
        for (int i = 0; i < model.getPointCount(); i++) {
            Chartable c = model.getPoint(i);
            ChartPoint3D p = (ChartPoint3D) c;
            Positionable xPos = p.getX();
            Positionable yPos = p.getY();
            if (xPos.position() == fromFile.ordinal() + 1 && yPos.position() == fromRank) {
                movingPoint = p;
                updatedPointIndex = i;
            }
        }
        if (movingPoint == null) {
            throw new IllegalStateException("Piece not found at " + fromFile + fromRank);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChessFile toFile = to.getFile();
                int toRank = to.getRank();
                ChartPoint3D p2 = new ChartPoint3D();
                p2.setX(fileRange.getCategoryValues().get(toFile.ordinal()));
                p2.setY(new RealPosition(toRank));
                p2.setZ(movingPoint.getZ());
                model.replacePoint(updatedPointIndex, p2);
            }
        });
    }

    private ChartPoint3D pieceAt(DefaultChartModel model, ChessFile f, int rank) {
        for (Chartable c : model) {
            ChartPoint3D point = (ChartPoint3D) c;
            Positionable xPos = point.getX();
            Positionable yPos = point.getY();
            if (xPos.position() == f.ordinal() + 1 && yPos.position() == rank) {
                return point;
            }
        }
        return null;
    }

    /**
     * Creates a ChartModel that represents a chess board position from a FEN string
     * @see <a http="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Forsyth Edwards Notation</a>
     * @param fen a chess board position in Forsyth Edwards Notation
     * @return a Chart Model that captures the provided position
     */
    DefaultChartModel createBoard(String fen) {
        return fenParser.convert(fen);
    }

    public void setPosition(String fen) {
        DefaultChartModel model = createBoard(fen);
        chart.setModel(model, new ChartStyle().withPoints());
    }

    public void setSelectedSquare(ChessSquare square) {
        this.selectedSquare = square;
        setupSquares();
        repaint();
    }

    public void addHighlight(ChessSquare square) {
        highlights.add(square);
        setupSquares();
        repaint();
    }

    public void removeHighlight(ChessSquare square) {
        highlights.remove(square);
        setupSquares();
        repaint();
    }

    public void removeHighlights() {
        highlights.clear();
        setupSquares();
        repaint();
    }

    public void addSquareSelectionListener(ChessSquareListener listener) {
        chessSquareListeners.add(listener);
    }

    public void removeSquareSelectionListener(ChessSquareListener listener) {
        chessSquareListeners.remove(listener);
    }

}
