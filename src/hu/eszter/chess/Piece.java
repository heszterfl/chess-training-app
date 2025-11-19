package hu.eszter.chess;

import java.util.List;

public abstract class Piece {

    public int[] startingPosition;
    public int[] currentPosition;

    public String color;

    public Piece(String color) {
        System.out.println("Piece constructor --> ");
        this.color = color;
    }

    public abstract List<int[]> getLegalMoves(Piece[][] board, int[] position);

    public abstract List<int[]> getLegalMovesSimple(int[][] board, int[] position);

    public abstract List<int[]> getLegalCaptures(Piece[][] board, int[] position);

    public int[] getStartingPosition() {
        return new int[]{this.startingPosition[0], this.startingPosition[1]};
    }

    public int[] getCurrentPosition() {
        return new int[]{this.currentPosition[0], this.currentPosition[1]};
    }

    public void setCurrentPosition(int[] position) {
        this.currentPosition = position;
    }
}
