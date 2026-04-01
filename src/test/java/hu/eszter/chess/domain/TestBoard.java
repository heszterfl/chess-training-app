package hu.eszter.chess.domain;

import static hu.eszter.chess.domain.PieceColor.BLACK;
import static hu.eszter.chess.domain.PieceColor.WHITE;

final class TestBoard {

    private TestBoard() {}

    static Board empty() {
        Board b = new Board();
        Piece[][] grid = b.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                grid[r][c] = null;
            }
        }
        b.whiteKingPosition = null;
        b.blackKingPosition = null;
        return b;
    }

    static void place(Board b, Piece p, Position pos) {

        if (p instanceof King) {
            if (p.getColor() == WHITE) {
                b.whiteKingPosition = pos;
            } else {
                b.blackKingPosition = pos;
            }
        }

        b.getBoard()[pos.row()][pos.col()] = p;
        p.setCurrentPosition(pos);
    }

    static void placeDefaultKings(Board b) {

        if (b.getPieceAt(new Position(7, 4)) != null ||
                b.getPieceAt(new Position(0, 4)) != null) {
            throw new IllegalStateException("Default king squares are occupied");
        }

        place(b, new King(WHITE), new Position(7, 4));
        place(b, new King(BLACK), new Position(0, 4));
    }

    static void placeKings(Board b, Position whitePos, Position blackPos) {

        if (b.getPieceAt(whitePos) != null || b.getPieceAt(blackPos) != null) {
            throw new IllegalStateException("Squares are occupied");
        }

        place(b, new King(WHITE), whitePos);
        place(b, new King(BLACK), blackPos);
    }
}
