/*
 * Copyright (c) 2005 - 2021 Catalysoft Limited. All rights reserved.
 * Created: 24th July 2010
 */

package com.chess.cochess.gui;

import java.util.Arrays;
import java.util.List;

public class ChessGame {

    private String[] moves = {
            "e2-e4", "c7-c6",
            "d2-d4", "d7-d5",
            "b1-c3", "d5xe4",
            "f2-f3", "e7-e5",
            "c1-e3", "d8-b6",
            "d1-d2", "b6xb2",
            "a1-b1", "b2-a3",
            "f1-c4", "b7-b5",
            "c3xb5", "c6xb5",
            "c4-d5", "a7-a5",
            "d5xa8", "f8-b4",
            "b1xb4", "a5xb4",
            "a8xe4", "g8-f6",
            "e4-d3", "f6-d5",
            "d4xe5", "d5xe3",
            "d2xe3", "O-O B",
            "g1-e2", "c8-e6",
            "O-O W", "b8-c6",
            "e3-e4", "f8-c8",
            "e4xh7", "g8-f8",
            "h7-h8", "f8-e7",
            "h8xg7", "a3xa2",
            "g7-f6", "e7-e8",
            "e2-f4", "c6-d4",
            "f1-d1", "b4-b3",
            "f4xe6", "b3xc2",
            "d3xb5", "d4xb5", // Actually Black resigned here, because mate follows
            "d1-d8", "c8xd8",
            "f6xd8"
    };

    public List<String> getMoves() {
        return Arrays.asList(moves);
    }
}
