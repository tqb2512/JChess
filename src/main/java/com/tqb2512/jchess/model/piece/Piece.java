package com.tqb2512.jchess.model.piece;

import lombok.Getter;
import com.tqb2512.jchess.model.Board;

import java.util.ArrayList;

public abstract class Piece {
    @Getter
    private final String name;
    @Getter
    private final String color;

    public Piece(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public abstract ArrayList<int[][]> getValidMoves(int currentCol, int currentRow, Board board);
}
