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
                    int[] newSquare = new int[]{newRow, newCol};
                    if (!isProtected(board, newSquare)) {
                        moves.add(new int[]{newRow, newCol});
                    }
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
                        !(board[newRow][newCol].color).equals(this.color)) {
                    int[] newSquare = new int[]{newRow, newCol};
                    if (!isProtected(board, newSquare)) {
                        moves.add(new int[]{newRow, newCol});
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

    public boolean isProtected(Piece[][] board, int[] square) {

        int x = square[0];
        int y = square[1];

        // Up
        int currentX = x - 1;
        while (currentX >= 0) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX--;
                continue;
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
                continue;
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
                continue;
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
                continue;
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
                Piece p = board[x + v][y + w];
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
