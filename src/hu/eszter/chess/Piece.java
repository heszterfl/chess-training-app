package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

// Világos -> NAGY BETŰ, sötét -> kis betű
public abstract class Piece {

    public List<int[]> legalMoves;
    public List<int[]> legalMovesSimple;
    public List<int[]> legalCaptures;

    public int[] startingPosition;
    public int[] currentPosition;

    public String color;

    public Piece() { }

    public Piece(String color) {
        System.out.println("Piece constructor --> ");
        this.color = color;
    }

    public List<int[]> getMoves(int[] currentPosition, int[] newPosition) { return new ArrayList<>(); }     // placeholder

    public abstract List<int[]> getLegalMoves(Piece[][] board, int[] position);

    public abstract List<int[]> getLegalMovesSimple(int[][] board, int[] position);

    public abstract List<int[]> getLegalCaptures(Piece[][] board, int[] position);

    public void move() {

    }

    public void moveSimple() {

    }

    public void capture() {

    }

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
