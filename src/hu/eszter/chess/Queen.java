package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(String color) {
        super(color);
        this.startingPosition = color.equals("white") ? new int[]{7, 3} : new int[]{0, 3};
        this.currentPosition = startingPosition;
    }

    @Override
    public List<int[]> getLegalMoves(Piece[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        int x = position[0];
        int y = position[1];

        // Up
        int currentX = x - 1;
        int currentY = y;
        while (currentX >= 0 && board[currentX][y] == null) {
            moves.add(new int[]{currentX,y});
            currentX--;
        }

        // Down
        currentX = x + 1;
        while (currentX < board.length && board[currentX][y] == null) {
            moves.add(new int[]{currentX, y});
            currentX++;
        }

        // Left
        currentY = y - 1;
        while (currentY >= 0 && board[x][currentY] == null) {
            moves.add(new int[]{x, currentY});
            currentY--;
        }

        // Right
        currentY = y + 1;
        while (currentY < board[0].length && board[x][currentY] == null) {
            moves.add(new int[]{x, currentY});
            currentY++;
        }

        // Up-left
        currentX = x-1;
        currentY = y-1;
        while (currentX >= 0 && currentY >= 0 &&
                board[currentX][currentY] == null) {
            moves.add(new int[]{currentX, currentY});
            currentX--;
            currentY--;
        }

        // Down-right
        currentX = x + 1;
        currentY = y + 1;
        while (currentX < board.length && currentY < board[0].length &&
                board[currentX][currentY] == null) {
            moves.add(new int[]{currentX, currentY});
            currentX++;
            currentY++;
        }

        // Up-right
        currentX = x - 1;
        currentY = y + 1;
        while (currentX >= 0 && currentY < board[0].length &&
                board[currentX][currentY] == null) {
            moves.add(new int[]{currentX, currentY});
            currentX--;
            currentY++;
        }

        // Down-left
        currentX = x + 1;
        currentY = y - 1;
        while (currentX < board.length && currentY >= 0 &&
                board[currentX][currentY] == null) {
            moves.add(new int[]{currentX, currentY});
            currentX++;
            currentY--;
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
                if ((Math.abs(x - i) == Math.abs(y - j) && !(x == i && y == j))
                        || ((i == x) ^ (j == y))) {
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

        // Up
        int currentX = x - 1;
        while (currentX >= 0) {
            Piece p = board[currentX][y];
            if (p == null) {
                currentX--;
                continue;
            }
            if (!(p.color).equals(this.color)) {
                moves.add(new int[]{currentX,y});
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
                moves.add(new int[]{currentX, y});
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
                moves.add(new int[]{x, currentY});
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
                moves.add(new int[]{x, currentY});
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
                moves.add(new int[]{currentX, currentY});
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
                moves.add(new int[]{currentX, currentY});
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
                moves.add(new int[]{currentX, currentY});
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
                moves.add(new int[]{currentX, currentY});
            }
            break;
        }

        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhiteQueen" : "BlackQueen";
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
