package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.util.SquareNotation;

import java.util.ArrayList;
import java.util.List;

public class SanMoveResolver {

    public Move resolve(String sanToken, Board board) {

        if (sanToken == null || sanToken.isBlank()) {
            throw new IllegalArgumentException("Invalid sanToken");
        }

        if (board == null) {
            throw new IllegalArgumentException("Invalid board");
        }

        String normalized = clean(sanToken);

        if ((normalized.contains("O")) ||
                (normalized.contains("=")) ||
                (normalized.length() > 3 && !normalized.contains("x"))) {
            throw new IllegalArgumentException("Invalid token");
        }

        List<Piece> candidates = new ArrayList<>();
        PieceKind pieceKind = getPieceKind(normalized);
        String targetSquare = getTargetSquare(normalized);
        Position targetPosition = SquareNotation.fromSquare(targetSquare);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getBoard()[i][j];
                if ((piece != null) &&
                        (piece.getPieceKind() == pieceKind)) {
                    if ((board.isWhiteToMove() && piece.getColor() == PieceColor.WHITE) ||
                            (!board.isWhiteToMove() && piece.getColor() == PieceColor.BLACK)) {
                        if (board.getIsLegalMove(piece.getCurrentPosition(), targetPosition)) {
                            if (!normalized.contains("x")) {
                                candidates.add(piece);
                            } else {
                                Piece targetPiece = board.getPieceAt(targetPosition);
                                if ((targetPiece != null) &&
                                        (targetPiece.getColor() != piece.getColor())) {
                                    candidates.add(piece);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (candidates.size() == 1) {
            Piece piece = candidates.get(0);
            return new Move(piece, piece.getColor(), piece.getCurrentPosition(), targetPosition);
        } else {
            throw new IllegalArgumentException("Move cannot be created");
        }
    }

    private String clean(String sanToken) {
        int tokenLength = sanToken.length();
        String normalized = sanToken;
        if ((sanToken.charAt(tokenLength-1) == '+') ||
                (sanToken.charAt(tokenLength-1) == '#')) {
            normalized = sanToken.substring(0, tokenLength-1);
        }
        return normalized;
    }

    private PieceKind getPieceKind(String sanToken) {
        char first = sanToken.charAt(0);
        if (Character.isLowerCase(first)) {
            if (!sanToken.contains("x")) {
                return PieceKind.PAWN;
            } else {
                throw new IllegalArgumentException("Invalid token");
            }
        }

        return switch (first) {
            case 'R' -> PieceKind.ROOK;
            case 'N' -> PieceKind.KNIGHT;
            case 'B' -> PieceKind.BISHOP;
            case 'Q' -> PieceKind.QUEEN;
            case 'K' -> PieceKind.KING;
            default -> throw new IllegalStateException();
        };
    }

    private String getTargetSquare(String sanToken) {
        int length = sanToken.length();
        return sanToken.substring(length-2, length);
    }

}
