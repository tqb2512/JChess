package com.tqb2512.jchess.model;

import com.tqb2512.jchess.model.piece.Piece;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class Game {

    private String id;
    User player1;
    User player2;
    GameStatus status;
    Board board;
    User currentPlayer;

    public Game(User player1) {
        this.setId(UUID.randomUUID().toString());
        this.player1 = player1;
        this.status = GameStatus.WAITING_FOR_PLAYER;
        this.board = new Board();
    }

    public ArrayList<int[][]> getValidMoves(int selectCol, int selectRow) {
        return board.getSquare(selectCol, selectRow).getPiece().getValidMoves(selectCol, selectRow, board);
    }

    public void move(int selectCol, int selectRow, int targetCol, int targetRow) {
        getValidMoves(selectCol, selectRow).stream()
                .filter(move -> move[0][0] == targetCol && move[0][1] == targetRow)
                .findFirst()
                .ifPresent(move -> {
                    board.movePiece(selectCol, selectRow, targetCol, targetRow);
                    currentPlayer = currentPlayer == player1 ? player2 : player1;
                });
    }


    public boolean isCheckMate() {
        boolean isWhiteKingAlive = false;
        boolean isBlackKingAlive = false;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Piece piece = board.getPiece(col, row);
                if (piece != null) {
                    if (piece.getName().equals("King")) {
                        if (piece.getColor().equals("white")) {
                            isWhiteKingAlive = true;
                        } else {
                            isBlackKingAlive = true;
                        }
                    }
                }
            }
        }
        return !isWhiteKingAlive || !isBlackKingAlive;
    }

}
