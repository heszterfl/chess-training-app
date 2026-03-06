package hu.eszter.chess.domain;

public class PieceFactory {

    public static Piece createPiece(PieceKind kind, PieceColor color, Position position) {
        int row = position.row();
        int col = position.col();

        return switch (kind) {
            case PAWN -> new Pawn(color, col);
            case ROOK -> new Rook(color, col < 4 ? "queen" : "king");
            case KNIGHT -> new Knight(color, col < 4 ? "queen": "king");
            case BISHOP -> new Bishop(color, col < 4 ? "queen" : "king");
            case QUEEN -> new Queen(color);
            case KING -> new King(color);
        };
    }
}
