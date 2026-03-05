package hu.eszter.chess.domain;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(String color) {
        super(color);
        this.startingPosition = color.equals("white") ? new Position(7, 3) : new Position(0, 3);
        this.currentPosition = startingPosition;
    }

    @Override
    public List<Position> getLegalMoves(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        // Up
        int currentX = x - 1;
        while (currentX >= 0 && board[currentX][y] == null) {
            moves.add(new Position(currentX,y));
            currentX--;
        }

        // Down
        currentX = x + 1;
        while (currentX < board.length && board[currentX][y] == null) {
            moves.add(new Position(currentX, y));
            currentX++;
        }

        // Left
        int currentY = y - 1;
        while (currentY >= 0 && board[x][currentY] == null) {
            moves.add(new Position(x, currentY));
            currentY--;
        }

        // Right
        currentY = y + 1;
        while (currentY < board[0].length && board[x][currentY] == null) {
            moves.add(new Position(x, currentY));
            currentY++;
        }

        // Up-left
        currentX = x-1;
        currentY = y-1;
        while (currentX >= 0 && currentY >= 0 &&
                board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX--;
            currentY--;
        }

        // Down-right
        currentX = x + 1;
        currentY = y + 1;
        while (currentX < board.length && currentY < board[0].length &&
                board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX++;
            currentY++;
        }

        // Up-right
        currentX = x - 1;
        currentY = y + 1;
        while (currentX >= 0 && currentY < board[0].length &&
                board[currentX][currentY] == null) {
            moves.add(new Position(currentX, currentY));
            currentX--;
            currentY++;
        }

        // Down-left
        currentX = x + 1;
        currentY = y - 1;
        while (currentX < board.length && currentY >= 0 &&
                board[currentX][currentY] == null) {
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
                if ((Math.abs(x - i) == Math.abs(y - j) && !(x == i && y == j))
                        || ((i == x) ^ (j == y))) {
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

        // Up
        int currentX = x - 1;
        while (currentX >= 0) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX--;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX,y));
            }
            break;
        }

        // Down
        currentX = x + 1;
        while (currentX < board.length) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX++;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(currentX, y));
            }
            break;
        }

        // Left
        int currentY = y - 1;
        while (currentY >= 0) {
            Piece p = board[x][currentY];
            if (p == null) {
                currentY--;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(x, currentY));
            }
            break;
        }

        // Right
        currentY = y + 1;
        while (currentY < board[0].length) {
            Piece p = board[x][currentY];
            if (p == null) {
                currentY++;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new Position(x, currentY));
            }
            break;
        }

        // Up-left
        currentX = x-1;
        currentY = y-1;
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
            if ( p == null) {
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
        return this.color.equals("white") ? "WQ" : "BQ";
    }

}
