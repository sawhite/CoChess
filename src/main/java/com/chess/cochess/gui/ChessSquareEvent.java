package com.chess.cochess.gui;

import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * This event object describes only that something happened to a square on the chess board.
 * It does not try to interpret the meaning of that in terms of the chess game.
 */
public class ChessSquareEvent extends EventObject {

    private final ChessSquare square;

    private final int mouseEventType;

    public ChessSquareEvent(Object source, ChessSquare square, int mouseEventType) {
        super(source);
        this.mouseEventType = mouseEventType;
        this.square = square;
    }

    public ChessSquare getSquare() {
        return square;
    }

    // Methods that allow the enquiring class to find out what happened in that square of the chess board
    // The idea is also to hide the details of the MouseEvent type from the application logic

    public boolean isMousePressed() {
        return mouseEventType == MouseEvent.MOUSE_PRESSED;
    }

    public boolean isMouseReleased() {
        return mouseEventType == MouseEvent.MOUSE_RELEASED;
    }

    public boolean isMouseClicked() {
        return mouseEventType == MouseEvent.MOUSE_CLICKED;
    }
}
