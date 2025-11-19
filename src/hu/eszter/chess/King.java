package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(String color) {
        super(color);
        this.startingPosition = color.equals("white") ? new int[]{7, 4} : new int[]{0, 4};
        this.currentPosition = startingPosition;
    }

    @Override
    public List<int[]> getLegalMoves(Piece[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        int x = position[0];
        int y = position[1];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length && board[newRow][newCol] == null) {
                    moves.add(new int[]{newRow, newCol});
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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length) {
                    moves.add(new int[]{newRow, newCol});
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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length && board[newRow][newCol] != null &&
                        !(board[newRow][newCol].color).equals(this.color)) {        // *****
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteKing" : "BlackKing";
    }

    @Override
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
