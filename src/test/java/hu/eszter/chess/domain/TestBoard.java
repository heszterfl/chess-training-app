package hu.eszter.chess.domain;

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
        return b;
    }

    static void place(Board b, Piece p, Position pos) {
        b.getBoard()[pos.row()][pos.col()] = p;
        p.setCurrentPosition(pos);
    }
}
