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

        if (isKingsideCastlingToken(normalized) || isQueensideCastlingToken(normalized)) {
            return resolveCastling(normalized, board);
        }

        if ((normalized.contains("O")) ||
                (normalized.contains("=")) ||
                (normalized.length() > 3 && !normalized.contains("x"))) {
            throw new IllegalArgumentException("Invalid token");
        }

        boolean isPawnMove = isPawnMoveToken(normalized);
        boolean isPawnCapture = isPawnCaptureToken(normalized);
        boolean isPieceMove = isPieceMoveToken(normalized);
        boolean isPieceCapture = isPieceCaptureToken(normalized);

        if (!isPawnMove && !isPawnCapture &&
        !isPieceMove && !isPieceCapture) {
            throw new IllegalArgumentException("Invalid token");
        }

        List<Piece> candidates = new ArrayList<>();
        PieceKind pieceKind = getPieceKind(normalized);
        String targetSquare = getTargetSquare(normalized);
        Position targetPosition = SquareNotation.fromSquare(targetSquare);
        int sourceFileCol = -1;

        if (isPawnCapture) {
            sourceFileCol = getPawnCaptureSourceFile(normalized);
        }

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
                                    if (isPawnCapture && sourceFileCol != piece.getCurrentPosition().col()) {
                                        continue;
                                    }
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

    private Move resolveCastling(String sanToken, Board b) {

        PieceColor sideToMove = b.isWhiteToMove() ? PieceColor.WHITE : PieceColor.BLACK;
        Position from = sideToMove == PieceColor.WHITE ? new Position(7, 4) : new Position(0, 4);

        Piece piece = b.getPieceAt(from);

        if (piece == null || (!(piece instanceof King) || piece.getColor() != sideToMove)) {
            throw new IllegalArgumentException("Invalid piece/color");
        }

        if (isKingsideCastlingToken(sanToken)) {
            Position to = sideToMove == PieceColor.WHITE ? new Position(7, 6) : new Position(0, 6);
            if (b.getIsLegalMove(from, to)) {
                return new Move(piece, sideToMove, from, to);
            }
        } else if (isQueensideCastlingToken(sanToken)) {
            Position to = sideToMove == PieceColor.WHITE ? new Position(7, 2) : new Position(0, 2);
            if (b.getIsLegalMove(from, to)) {
                return new Move(piece, sideToMove, from, to);
            }
        }

        throw new IllegalArgumentException("Move cannot be created");
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
            return PieceKind.PAWN;
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

    private int getPawnCaptureSourceFile(String token) {

        char first = token.charAt(0);
        return switch (first) {
          case 'a' -> 0;
          case 'b' -> 1;
          case 'c' -> 2;
          case 'd' -> 3;
          case 'e' -> 4;
          case 'f' -> 5;
          case 'g' -> 6;
          case 'h' -> 7;
          default -> throw new IllegalStateException();
        };
    }

    private boolean isPawnMoveToken(String token) {

        return token.matches("[a-h][1-8]");
    }

    private boolean isPawnCaptureToken(String token) {

        return token.matches("[a-h]x[a-h][1-8]");
    }

    private boolean isPieceMoveToken(String token) {

        return token.matches("[BKNQR][a-h][1-8]");
    }

    private boolean isPieceCaptureToken(String token) {

        return token.matches("[BKNQR]x[a-h][1-8]");
    }

    private boolean isKingsideCastlingToken(String token) {

        return token.equals("O-O");
    }

    private boolean isQueensideCastlingToken(String token) {

        return token.equals("O-O-O");
    }
}
