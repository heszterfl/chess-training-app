package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(String color, String side) {
        super(color);
        if (side.equals("queen") && color.equals("white")) {
            this.startingPosition = new Position(7, 1);
        }
        if (side.equals("king") && color.equals("white")) {
            this.startingPosition = new Position(7, 6);
        }
        if (side.equals("queen") && color.equals("black")) {
            this.startingPosition = new Position(0, 1);
        }
        if (side.equals("king") && color.equals("black")) {
            this.startingPosition = new Position(0, 6);
        }
        this.currentPosition = startingPosition;
    }

    @Override
    public List<Position> getLegalMoves(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) &&
                        (board[i][j] == null)) {
                    moves.add(new Position(i,j));
                }
            }
        }
        return moves;
    }

    @Override
    public List<Position> getLegalMovesSimple(int[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) {
                    moves.add(new Position(i,j));
                }
            }
        }
        return moves;
    }

    @Override
    public List<Position> getLegalCaptures(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) &&
                        (board[i][j] != null) && !(board[i][j].color).equals(this.color)) {     // *****
                    moves.add(new Position(i,j));
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteKnight" : "BlackKnight";
    }

}
