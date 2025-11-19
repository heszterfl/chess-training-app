package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(String color, String side) {
        super(color);
        if (side.equals("queen") && color.equals("white")) {
            this.startingPosition = new int[]{7, 1};
        }
        if (side.equals("king") && color.equals("white")) {
            this.startingPosition = new int[]{7, 6};
        }
        if (side.equals("queen") && color.equals("black")) {
            this.startingPosition = new int[]{0, 1};
        }
        if (side.equals("king") && color.equals("black")) {
            this.startingPosition = new int[]{0, 6};
        }
        this.currentPosition = startingPosition;
    }

    @Override
    public List<int[]> getLegalMoves(Piece[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        int x = position[0];
        int y = position[1];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) &&
                        (board[i][j] == null)) {
                    moves.add(new int[]{i,j});
                }
            }
        }
        return moves;
    }

    @Override
    public List<int[]> getLegalMovesSimple(int[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        int x = position[0];
        int y = position[1];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) {
                    moves.add(new int[]{i,j});
                }
            }
        }
        return moves;
    }

    @Override
    public List<int[]> getLegalCaptures(Piece[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        int x = position[0];
        int y = position[1];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (((Math.abs(x-i) == 1 && Math.abs(y-j) == 2) ||
                        (Math.abs(x-i) == 2 && Math.abs(y-j) == 1)) &&
                        (board[i][j] != null) && !(board[i][j].color).equals(this.color)) {     // *****
                    moves.add(new int[]{i,j});
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteKnight" : "BlackKnight";
    }

    public int[] getStartingPosition() {
        return new int[]{this.startingPosition[0], this.startingPosition[1]};
    }

    @Override
    public int[] getCurrentPosition() {
        return new int[]{this.currentPosition[0], this.currentPosition[1]};
    }

    @Override
    public void setCurrentPosition(int[] position) {
        this.currentPosition = position;
    }
}
