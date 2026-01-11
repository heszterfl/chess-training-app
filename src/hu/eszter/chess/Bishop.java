package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(String color, String side) {
        super(color);
        if (side.equals("queen") && color.equals("white")) {
            this.startingPosition = new Position(7, 2);
        }
        if (side.equals("king") && color.equals("white")) {
            this.startingPosition = new Position(7, 5);
        }
        if (side.equals("queen") && color.equals("black")) {
            this.startingPosition = new Position(0, 2);
        }
        if (side.equals("king") && color.equals("black")) {
            this.startingPosition = new Position(0, 5);
        }
        this.currentPosition = startingPosition;
    }

    @Override
    public List<Position> getLegalMoves(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        // Up-left
        int currentX = x-1;
        int currentY = y-1;
        while (currentX >= 0 && currentY >= 0 && board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX--;
            currentY--;
        }

        // Down-right
        currentX = x + 1;
        currentY = y + 1;
        while (currentX < board.length && currentY < board[0].length && board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX++;
            currentY++;
        }

        // Up-right
        currentX = x - 1;
        currentY = y + 1;
        while (currentX >= 0 && currentY < board[0].length && board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX--;
            currentY++;
        }

        // Down-left
        currentX = x + 1;
        currentY = y - 1;
        while (currentX < board.length && currentY >= 0 && board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX++;
            currentY--;
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
                if (Math.abs(x - i) == Math.abs(y - j) && !(x == i && y == j)) {
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

        // Up-left
        int currentX = x-1;
        int currentY = y-1;
        while (currentX >= 0 && currentY >= 0) {
            Piece p = board[currentX][currentY];
            if (p == null) {
                currentX--;
                currentY--;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX, currentY));
            }
            break;
        }

        // Down-right
        currentX = x + 1;
        currentY = y + 1;
        while (currentX < board.length && currentY < board[0].length) {
            Piece p = board[currentX][currentY];
            if (p == null) {
                currentX++;
                currentY++;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX, currentY));
            }
            break;
        }

        // Up-right
        currentX = x - 1;
        currentY = y + 1;
        while (currentX >= 0 && currentY < board[0].length) {
            Piece p = board[currentX][currentY];
            if (p == null) {
                currentX--;
                currentY++;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX, currentY));
            }
            break;
        }

        // Down-left
        currentX = x + 1;
        currentY = y - 1;
        while (currentX < board.length && currentY >= 0) {
            Piece p = board[currentX][currentY];
            if (p == null) {
                currentX++;
                currentY--;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX, currentY));
            }
            break;
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteBishop" : "BlackBishop";
    }

}
