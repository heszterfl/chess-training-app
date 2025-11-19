package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(String color, int column) {
        super(color);
        if (color.equals("white")) {
            this.startingPosition = switch (column) {
                case 0 -> new int[]{6, 0};
                case 1 -> new int[]{6, 1};
                case 2 -> new int[]{6, 2};
                case 3 -> new int[]{6, 3};
                case 4 -> new int[]{6, 4};
                case 5 -> new int[]{6, 5};
                case 6 -> new int[]{6, 6};
                case 7 -> new int[]{6, 7};
                default -> null;
            };
        } else if (color.equals("black")) {
            this.startingPosition = switch (column) {
                case 0 -> new int[]{1, 0};
                case 1 -> new int[]{1, 1};
                case 2 -> new int[]{1, 2};
                case 3 -> new int[]{1, 3};
                case 4 -> new int[]{1, 4};
                case 5 -> new int[]{1, 5};
                case 6 -> new int[]{1, 6};
                case 7 -> new int[]{1, 7};
                default -> null;
            };
        }
        this.currentPosition = startingPosition;
    }

    @Override
    public List<int[]> getLegalMoves(Piece[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        // Check if black or white
        int x = position[0];
        int y = position[1];
        boolean isFirstMove = true;

        // ha világos és x < 6 vagy sötét és x > 1 --> !isFirstMove
        if ((color.equals("white") && x < 6) || (color.equals("black") && x > 1)) {
            isFirstMove = false;
        }

        if (color.equals("white")) {
            if (isFirstMove) {
                if (board[x-1][y] == null) {
                    moves.add(new int[]{x-1, y});
                }
                if (board[x-2][y] == null) {
                    moves.add(new int[]{x-2, y});
                }
            } else {
                if (board[x-1][y] == null) {
                    moves.add(new int[]{x-1, y});
                }
            }
        } else {
            if (isFirstMove) {
                if (board[x+1][y] == null) {
                    moves.add(new int[]{x+1, y});
                }
                if (board[x+2][y] == null) {
                    moves.add(new int[]{x+2, y});
                }
            } else {
                if (board[x+1][y] == null) {
                    moves.add(new int[]{x+1, y});
                }
            }
        }
        return moves;
    }

    @Override
    public List<int[]> getLegalMovesSimple(int[][] board, int[] position) {
        List<int[]> moves = new ArrayList<>();

        // Check if black or white
        int x = position[0];
        int y = position[1];
        boolean isFirstMove = true;

        // ha világos és x < 6 vagy sötét és x > 1 --> !isFirstMove
        if ((color.equals("white") && x < 6) || (color.equals("black") && x > 1)) {
            isFirstMove = false;
        }

        if (color.equals("white")) {
            if (isFirstMove) {
                moves.add(new int[]{x-1, y});
                moves.add(new int[]{x-2, y});
            } else {
                moves.add(new int[]{x-1, y});
            }
        } else {
            if (isFirstMove) {
                moves.add(new int[]{x+1, y});
                moves.add(new int[]{x+2, y});
            } else {
                moves.add(new int[]{x+1, y});
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color == "white" ? "WhitePawn" : "BlackPawn";
    }

    @Override
    public List<int[]> getLegalCaptures(Piece[][] board, int[] position) {

        List<int[]> captures = new ArrayList<>();
        int x = position[0];
        int y = position[1];

        if (color.equals("white")) {
            if (board[x-1][y-1] != null && !(board[x-1][y-1].color).equals(this.color)) {
                captures.add(new int[]{x-1,y-1});
            }
            if (board[x-1][y+1] != null && !(board[x-1][y+1].color).equals(this.color)) {
                captures.add(new int[]{x-1,y+1});
            }
        } else {
            if (board[x+1][y-1] != null && !(board[x+1][y-1].color).equals(this.color)) {
                captures.add(new int[]{x+1,y-1});
            }
            if (board[x+1][y+1] != null && !(board[x+1][y+1].color).equals(this.color)) {
                captures.add(new int[]{x+1,y+1});
            }
        }

        return captures;
    }

    @Override
    public int[] getCurrentPosition() {
        return new int[]{this.currentPosition[0], this.currentPosition[1]};
    }

    @Override
    public int[] getStartingPosition() {
        return new int[]{this.startingPosition[0], this.startingPosition[1]};
    }

    @Override
    public void setCurrentPosition(int[] position) {
        this.currentPosition = position;
    }
}
