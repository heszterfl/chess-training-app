package hu.eszter.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(String color, int column) {
        super(color);
        if (color.equals("white")) {
            this.startingPosition = switch (column) {
                case 0 -> new Position(6, 0);
                case 1 -> new Position(6, 1);
                case 2 -> new Position(6, 2);
                case 3 -> new Position(6, 3);
                case 4 -> new Position(6, 4);
                case 5 -> new Position(6, 5);
                case 6 -> new Position(6, 6);
                case 7 -> new Position(6, 7);
                default -> null;
            };
        } else if (color.equals("black")) {
            this.startingPosition = switch (column) {
                case 0 -> new Position(1, 0);
                case 1 -> new Position(1, 1);
                case 2 -> new Position(1, 2);
                case 3 -> new Position(1, 3);
                case 4 -> new Position(1, 4);
                case 5 -> new Position(1, 5);
                case 6 -> new Position(1, 6);
                case 7 -> new Position(1, 7);
                default -> null;
            };
        }
        this.currentPosition = startingPosition;
    }

    @Override
    public List<Position> getLegalMoves(Piece[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        // Check if black or white
        int x = position.row();
        int y = position.col();
//        boolean isFirstMove = (!color.equals("white") || x >= 6) && (!color.equals("black") || x <= 1);
        boolean isFirstMove = (color.equals("white") && x == 6) || (color.equals("black") && x == 1);

        if (color.equals("white")) {
            if (isFirstMove) {
                if (board[x-1][y] == null) {
                    moves.add(new Position(x-1, y));
                    if (board[x-2][y] == null) {
                        moves.add(new Position(x-2, y));
                    }
                }
            } else {
                if (board[x-1][y] == null) {
                    moves.add(new Position(x-1, y));
                }
            }
        } else {
            if (isFirstMove) {
                if (board[x+1][y] == null) {
                    moves.add(new Position(x+1, y));
                    if (board[x+2][y] == null) {
                        moves.add(new Position(x+2, y));
                    }
                }
            } else {
                if (board[x+1][y] == null) {
                    moves.add(new Position(x+1, y));
                }
            }
        }
        return moves;
    }

    @Override
    public List<Position> getLegalMovesSimple(int[][] board, Position position) {
        List<Position> moves = new ArrayList<>();

        // Check if black or white
        int x = position.row();
        int y = position.col();
//        boolean isFirstMove = (!color.equals("white") || x >= 6) && (!color.equals("black") || x <= 1);
        boolean isFirstMove = (color.equals("white") && x == 6) || (color.equals("black") && x == 1);

        if (color.equals("white")) {
            if (isFirstMove) {
                moves.add(new Position(x-1, y));
                moves.add(new Position(x-2, y));
            } else {
                moves.add(new Position(x-1, y));
            }
        } else {
            if (isFirstMove) {
                moves.add(new Position(x+1, y));
                moves.add(new Position(x+2, y));
            } else {
                moves.add(new Position(x+1, y));
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return this.color.equals("white") ? "WhitePawn" : "BlackPawn";
    }

    @Override
    public List<Position> getLegalCaptures(Piece[][] board, Position position) {

        List<Position> captures = new ArrayList<>();
        int x = position.row();
        int y = position.col();

        if (color.equals("white")) {
            if (y-1 >= 0 && board[x-1][y-1] != null && !(board[x-1][y-1].color).equals(this.color)) {
                captures.add(new Position(x-1,y-1));
            }
            if (y+1 < board[0].length && board[x-1][y+1] != null && !(board[x-1][y+1].color).equals(this.color)) {
                captures.add(new Position(x-1,y+1));
            }
        } else {
            if (y-1 >= 0 && board[x+1][y-1] != null && !(board[x+1][y-1].color).equals(this.color)) {
                captures.add(new Position(x+1,y-1));
            }
            if (y+1 < board[0].length && board[x+1][y+1] != null && !(board[x+1][y+1].color).equals(this.color)) {
                captures.add(new Position(x+1,y+1));
            }
        }

        return captures;
    }

}
