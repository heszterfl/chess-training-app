package hu.eszter.chess;

import java.util.List;

public abstract class Piece {

    public Position startingPosition;
    public Position currentPosition;

    public String color;

    public Piece(String color) {
//        System.out.println("Piece constructor --> ");
        this.color = color;
    }

    public abstract List<Position> getLegalMoves(Piece[][] board, Position position);

    public abstract List<Position> getLegalMovesSimple(int[][] board, Position position);

    public abstract List<Position> getLegalCaptures(Piece[][] board, Position position);

    public int[] getStartingPosition() {
        return new int[]{this.startingPosition.row(), this.startingPosition.col()};
    }

    public Position getCurrentPosition() {
        return new Position(this.currentPosition.row(), this.currentPosition.col());
    }

    public void setCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    public String getColor() {
        return color;
    }
}
