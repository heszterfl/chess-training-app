package hu.eszter.chess.domain;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(PieceColor color) {
        super(color);
    }

    public Rook(PieceColor color, String side) {
        super(color);
        if (side.equals("queen") && color == PieceColor.WHITE) {
            this.startingPosition = new Position(7, 0);
        }
        if (side.equals("king") && color == PieceColor.WHITE) {
            this.startingPosition = new Position(7, 7);
        }
        if (side.equals("queen") && color == PieceColor.BLACK) {
            this.startingPosition = new Position(0, 0);
        }
        if (side.equals("king") && color == PieceColor.BLACK) {
            this.startingPosition = new Position(0, 7);
        }
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

        return moves;
    }

    @Override
    public List<Position> getLegalMovesSimple(int[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((i == x) ^ (j == y)) {
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
        int currentY = y;
        while (currentX >= 0) {
            Piece p = board[currentX][currentY];
            if (p == null) {
                currentX--;
                continue;
            }
            if (!(p.getColor()).equals(this.getColor())) {
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
            if (!(p.getColor()).equals(this.getColor())) {
                moves.add(new Position(currentX, y));
            }
            break;
        }

        // Left
        currentY = y - 1;
        while (currentY >= 0) {
            Piece p = board[x][currentY];
            if (p == null) {
                currentY--;
                continue;
            }
            if (!(p.getColor()).equals(this.getColor())) {
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
            if (!(p.getColor()).equals(this.getColor())) {
                moves.add(new Position(x, currentY));
            }
            break;
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.BLACK ? "WR" : "BR";
    }

}
