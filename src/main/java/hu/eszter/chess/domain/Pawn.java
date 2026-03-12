package hu.eszter.chess.domain;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    public Pawn(PieceColor color, int column) {
        super(color);
        if (color == PieceColor.WHITE) {
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
        } else if (color == PieceColor.BLACK) {
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
        boolean isFirstMove = (this.getColor() == PieceColor.WHITE && x == 6) || (this.getColor() == PieceColor.BLACK && x == 1);

        if (this.getColor() == PieceColor.WHITE) {
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
        boolean isFirstMove = (this.getColor() == PieceColor.WHITE && x == 6) || (this.getColor() == PieceColor.BLACK && x == 1);

        if (this.getColor() == PieceColor.WHITE) {
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
        return this.getColor() == PieceColor.WHITE ? "wp" : "bp";
    }

    @Override
    public List<Position> getLegalCaptures(Piece[][] board, Position position) {

        List<Position> captures = new ArrayList<>();
        int x = position.row();
        int y = position.col();

        if (this.getColor() == PieceColor.WHITE) {    // White side
            if (y-1 >= 0) { //  if capture is in bounds on the left
                if (board[x-1][y-1] != null && !(board[x - 1][y - 1].getColor()).equals(this.getColor())) { // if piece to capture exists AND piece is opposite color
                    captures.add(new Position(x-1,y-1));
                }
            }
            if (y+1 < board[0].length) {    // if capture is in bounds on the right
                if (board[x-1][y+1] != null && !(board[x - 1][y + 1].getColor()).equals(this.getColor())) {  // if piece to capture exists AND piece is opposite color
                    captures.add(new Position(x-1,y+1));
                }
            }
        } else {    // Black side
            if (y-1 >= 0) {
                if (board[x+1][y-1] != null && !(board[x + 1][y - 1].getColor()).equals(this.getColor())) {
                    captures.add(new Position(x+1,y-1));
                }
            }
            if (y+1 < board[0].length) {
                if (board[x+1][y+1] != null && !(board[x + 1][y + 1].getColor()).equals(this.getColor())) {
                    captures.add(new Position(x+1,y+1));
                }
            }
        }

        return captures;
    }

    @Override
    public PieceKind getPieceKind() {
        return PieceKind.PAWN;
    }

    // ellenőrzi, hogy enPassant lehetséges és visszaadja a mezőt, ahova en passant-tal lehet lépni
    public Position getEnPassant(Move lastMove) {

        if (lastMove == null) {
            return null;
        }

        int columnOfP = this.getCurrentPosition().col();
        int rowOfP = this.getCurrentPosition().row();
        int columnDiff = Math.abs(columnOfP - lastMove.to().col());

        if (this.getColor() == PieceColor.WHITE && rowOfP == 3 &&
                lastMove.color() == PieceColor.BLACK && lastMove.from().row() == 1 &&
                lastMove.to().row() == 3 && columnDiff == 1) {
            return new Position(rowOfP - 1, lastMove.to().col());
        } else if (this.getColor() == PieceColor.BLACK && rowOfP == 4 &&
                lastMove.color() == PieceColor.WHITE && lastMove.from().row() == 6 &&
                lastMove.to().row() == 4 && columnDiff == 1) {
            return new Position(rowOfP + 1, lastMove.to().col());
        } else {
            return null;
        }
    }
}
