package hu.eszter.chess.testutil;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.King;
import hu.eszter.chess.domain.Piece;
import hu.eszter.chess.domain.Position;

import static hu.eszter.chess.domain.PieceColor.BLACK;
import static hu.eszter.chess.domain.PieceColor.WHITE;

public final class TestBoard {

    private TestBoard() {}

    public static Board empty() {
        Board b = new Board();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                b.clearSquareForSetup(new Position(r, c));
            }
        }

        return b;
    }

    public static void place(Board b, Piece p, Position pos) {

        b.placePieceForSetup(p, pos);
    }

    public static void placeDefaultKings(Board b) {

        if (b.getPieceAt(new Position(7, 4)) != null ||
                b.getPieceAt(new Position(0, 4)) != null) {
            throw new IllegalStateException("Default king squares are occupied");
        }

        place(b, new King(WHITE), new Position(7, 4));
        place(b, new King(BLACK), new Position(0, 4));
    }

    public static void placeKings(Board b, Position whitePos, Position blackPos) {

        if (b.getPieceAt(whitePos) != null || b.getPieceAt(blackPos) != null) {
            throw new IllegalStateException("Squares are occupied");
        }

        place(b, new King(WHITE), whitePos);
        place(b, new King(BLACK), blackPos);
    }
}
