package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.util.SquareNotation;

import java.util.ArrayList;
import java.util.List;

public class SanMoveGenerator {

    public String toSan(Board boardBeforeMove, Move move) {

        String sanToken = "";
        String targetSquare = getTargetSquare(move.to());

        if (isKingsideCastlingMove(move)) {
            sanToken = "O-O";
        } else if (isQueensideCastlingMove(move)) {
            sanToken = "O-O-O";
        } else if (move.piece() instanceof Pawn) {
            String sourceFile = toFileLetter(move.from().col());

            targetSquare = getTargetSquare(move.to());

            if (isCaptureMove(boardBeforeMove, move)) {
                sanToken = sourceFile + "x" + targetSquare;
            } else {
                sanToken = targetSquare;
            }

            if (isPromotionMove(move)) {
                sanToken = sanToken + "=Q";
            }
        } else {
            String pieceLetter = getPieceLetter(move.piece());
            String disambiguation = getDisambiguation(boardBeforeMove, move);

            if (isCaptureMove(boardBeforeMove, move)) {
                sanToken = pieceLetter + disambiguation + "x" + targetSquare;
            } else {
                sanToken = pieceLetter + disambiguation + targetSquare;
            }
        }

        Board copy = new Board(boardBeforeMove);
        Piece pieceCopy = copy.getPieceAt(move.from());
        copy.applyMove(pieceCopy, move.from(), move.to());

        PieceColor sideToMove = copy.isWhiteToMove() ? PieceColor.WHITE : PieceColor.BLACK;

        if (copy.isCheckmate(sideToMove)) {
            sanToken = sanToken + "#";
        } else if (copy.isInCheck(sideToMove)) {
            sanToken = sanToken + "+";
        }

        return sanToken;
    }

    private boolean isKingsideCastlingMove(Move move) {

        if (move.piece() instanceof King) {
            int fromRow = move.from().row();
            int fromCol = move.from().col();
            int toRow = move.to().row();
            int toCol = move.to().col();

            if (move.color() == PieceColor.WHITE) {
                if (fromRow == 7 && fromCol == 4 && toRow == 7 && toCol == 6) {
                    return true;
                }
            } else {
                if (fromRow == 0 && fromCol == 4 && toRow == 0 && toCol == 6) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isQueensideCastlingMove(Move move) {

        if (move.piece() instanceof King) {
            int fromRow = move.from().row();
            int fromCol = move.from().col();
            int toRow = move.to().row();
            int toCol = move.to().col();

            if (move.color() == PieceColor.WHITE) {
                if (fromRow == 7 && fromCol == 4 && toRow == 7 && toCol == 2) {
                    return true;
                }
            } else {
                if (fromRow == 0 && fromCol == 4 && toRow == 0 && toCol == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPromotionMove(Move move) {

        PieceColor color = move.color();
        int fromRow = move.from().row();
        int toRow = move.to().row();

        if (move.piece() instanceof Pawn) {
            if (color == PieceColor.WHITE) {
                if (fromRow == 1 && toRow == 0) {
                    return true;
                }
            } else {
                if (fromRow == 6 && toRow == 7) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isCaptureMove(Board boardBeforeMove, Move move) {

        if (boardBeforeMove.getPieceAt(move.to()) != null) {
            return true;
        }
        return boardBeforeMove.isEnPassant(move);
    }

    private String getPieceLetter(Piece piece) {

        PieceKind kind = piece.getPieceKind();
        return switch (kind) {
            case KING -> "K";
            case QUEEN -> "Q";
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN -> "";
        };
    }

    private String getTargetSquare(Position to) {

        return SquareNotation.toSquare(to);
    }

    private String getDisambiguation(Board boardBeforeMove, Move move) {

        Piece thisPiece = move.piece();
        Piece[][] b = boardBeforeMove.getBoard();
        List<Piece> rivals = new ArrayList<>();

        if (thisPiece instanceof Pawn || thisPiece instanceof King) {
            return "";
        }

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {

                Piece otherPiece = b[i][j];

                if (otherPiece == null) {
                    continue;
                }

                if (thisPiece.getPieceKind() == otherPiece.getPieceKind() &&
                    thisPiece.getColor() == otherPiece.getColor() &&
                    !(otherPiece.getCurrentPosition().equals(move.from())) &&
                        boardBeforeMove.getIsLegalMove(otherPiece.getCurrentPosition(), move.to())) {
                    rivals.add(otherPiece);
                }
            }
        }

        if (rivals.isEmpty()) {
            return "";
        }

        boolean sameFileExists = false;

        for (Piece rival : rivals) {
            if (rival.getCurrentPosition().col() == move.from().col()) {
                sameFileExists = true;
                break;
            }
        }

        if (sameFileExists) {
            return Integer.toString(8 - move.from().row());
        }

        return toFileLetter(move.from().col());
    }

    private String toFileLetter(int col) {
        return  switch (col) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> throw new IllegalArgumentException();
        };
    }
}
