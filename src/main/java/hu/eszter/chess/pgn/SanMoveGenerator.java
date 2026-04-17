package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.util.SquareNotation;

public class SanMoveGenerator {

    public String toSan(Board boardBeforeMove, Move move) {

        if (isKingsideCastlingMove(move)) {
            return "O-O";
        }

        if (isQueensideCastlingMove(move)) {
            return "O-O-O";
        }

        if (move.piece() instanceof Pawn) {

            String sanToken = "";

            String sourceFile = switch (move.from().col()) {
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

            String targetSquare = getTargetSquare(move.to());

            if (isCaptureMove(boardBeforeMove, move)) {
                sanToken = sourceFile + "x" + targetSquare;
            } else {
                sanToken = targetSquare;
            }

            if (isPromotionMove(move)) {
                sanToken = sanToken + "=Q";
            }

            return sanToken;
        }

        String pieceLetter = getPieceLetter(move.piece());
        String targetSquare = getTargetSquare(move.to());

        if (isCaptureMove(boardBeforeMove, move)) {
            return pieceLetter + "x" + targetSquare;
        } else {
            return pieceLetter + targetSquare;
        }
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

        Piece target = boardBeforeMove.getPieceAt(move.to());
        return target != null;
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
}
