package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(String color) {
        super(color);
        this.startingPosition = color.equals("white") ? new Position(7, 4) : new Position(0, 4);
        this.currentPosition = startingPosition;
    }

    @Override
    public List<Position> getLegalMoves(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        int x = position.row();
        int y = position.col();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length && board[newRow][newCol] == null) {
                    Position newSquare = new Position(newRow, newCol);
                    if (!isProtected(board, newSquare)) {
                        moves.add(new Position(newRow, newCol));
                    }
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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length) {
                    moves.add(new Position(newRow, newCol));
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

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( i == 0 && j == 0) continue;
                int newRow = x + i;
                int newCol = y + j;

                // Check if within board bounds
                if (newRow >= 0 && newRow < board.length &&
                        newCol >= 0 && newCol < board[0].length && board[newRow][newCol] != null &&
                        !(board[newRow][newCol].color).equals(this.color)) {
                    Position newSquare = new Position(newRow, newCol);
                    if (!isProtected(board, newSquare)) {
                        moves.add(new Position(newRow, newCol));
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteKing" : "BlackKing";
    }

    public boolean isProtected(Piece[][] board, Position square) {

        int x = square.row();
        int y = square.col();

        // Up
        int currentX = x - 1;
        while (currentX >= 0) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX--;
            } else if (currentX == x - 1 && !((p.color).equals(this.color)) && (p instanceof Queen || p instanceof Rook || p instanceof King)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
        }

        // Down
        currentX = x + 1;
        while (currentX < board.length) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX++;
            } else if (currentX == x + 1 && !(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook || p instanceof King)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
        }

        // Left
        int currentY = y - 1;
        while (currentY >= 0) {
            Piece p = board[x][currentY];
            if (p == null) {
                currentY--;
            } else if (currentY == y + 1 && !(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook || p instanceof King)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
        }

        // Right
        currentY = y + 1;
        while (currentY < board[0].length) {
            Piece p = board[x][currentY];
            if (p == null) {
                currentY++;
            } else if (currentY == y + 1 && !(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook || p instanceof King)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!(p.color).equals(this.color) && (p instanceof Queen || p instanceof Rook)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
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
            if (currentX == x - 1 && currentY == y - 1 && this.color.equals("white") && !((p.color).equals(this.color)) && (p instanceof Pawn)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!((p.color).equals(this.color)) && (p instanceof Queen || p instanceof Bishop)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
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
            if (currentX == x + 1 && currentY == y + 1 && this.color.equals("black") && !((p.color).equals(this.color)) && (p instanceof Pawn)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!((p.color).equals(this.color)) && (p instanceof Queen || p instanceof Bishop)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
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
            if (currentX == x - 1 && currentY == y + 1 && this.color.equals("white") && !((p.color).equals(this.color)) && (p instanceof Pawn)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!((p.color).equals(this.color)) && (p instanceof Queen || p instanceof Bishop)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
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
            if (currentX == x + 1 && currentY == y - 1 && this.color.equals("black") && !((p.color).equals(this.color)) && (p instanceof Pawn)) {
                System.out.println("isProtected() --> true");
                return true;
            } else if (!((p.color).equals(this.color)) && (p instanceof Queen || p instanceof Bishop)) {
                System.out.println("isProtected() --> true");
                return true;
            } else {
                break;
            }
        }

        // Knight distance
        int[] xValues = {-2, -1, 1, 2};
        int[] yValues = {-2, -1, 1, 2};
        for (int v : xValues) {
            for (int w : yValues) {
                if (Math.abs(v) == Math.abs(w)) {
                    continue;
                }
                int targetRow = x + v;
                int targetCol = y + w;
                Piece p;
                if (targetRow >= 0 && targetRow <= 7 && targetCol >= 0 && targetCol <= 7) {
                    p = board[x + v][y + w];
                } else {
                    continue;
                }

                if (p == null) {
                    continue;
                }
                if (!p.color.equals(this.color) && p instanceof Knight) {
                    System.out.println("isProtected() --> true");
                    return true;
                }
            }
        }
        System.out.println("isProtected() --> false");
        return false;
    }

}
