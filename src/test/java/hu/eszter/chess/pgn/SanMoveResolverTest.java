package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.testutil.TestBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SanMoveResolverTest {

    SanMoveResolver sanMoveResolver;

    @BeforeEach
    void setup() {
        sanMoveResolver = new SanMoveResolver();
    }

    @Test
    void resolve_pawn_move_from_initial_position() {
        Board b = new Board();
        String token = "e4";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(6, 4), move.from());
        assertEquals(new Position(4, 4), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_knight_move_from_initial_position() {
        Board b = new Board();
        String token = "Nf3";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 6), move.from());
        assertEquals(new Position(5, 5), move.to());
        assertEquals(PieceKind.KNIGHT, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_piece_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Queen queen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queen, new Position(4, 3));

        Knight knight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight, new Position(3, 3));

        String token = "Qxd5";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(4, 3), move.from());
        assertEquals(new Position(3, 3), move.to());
        assertEquals(PieceKind.QUEEN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_pawn_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(4, 4));

        Knight knight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight, new Position(3, 3));

        String token = "exd5";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(4, 4), move.from());
        assertEquals(new Position(3, 3), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_pawn_capture_throws_if_no_candidate_exists() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(4, 4));

        String token = "exd5";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_ignores_check_suffix() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Queen queen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queen, new Position(4, 3));

        String token = "Qe4+";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(4, 3), move.from());
        assertEquals(new Position(4, 4), move.to());
        assertEquals(PieceKind.QUEEN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_throws_for_castling() {
        Board b = TestBoard.empty();
        String token = "O-O";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_for_ambiguous_move() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(6, 4), new Position(1, 4));

        Rook rook1 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(7,7));

        Rook rook8 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook8, new Position(0, 7));

        String token = "Rh5";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_if_no_candidate_exists() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Queen queen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queen, new Position(4, 3));

        String token = "Qb3";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }
}
