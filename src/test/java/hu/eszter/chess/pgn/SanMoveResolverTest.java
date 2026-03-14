package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
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
    void resolve_ignores_check_suffix() {
        Board b = TestBoard.empty();
        Queen queen = new Queen(PieceColor.WHITE);
        King king = new King(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(4, 3));
        TestBoard.place(b, king, new Position(7, 4));
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
        Rook rook1 = new Rook(PieceColor.WHITE);
        Rook rook8 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(7,7));
        TestBoard.place(b, rook8, new Position(0, 7));
        String token = "Rh5";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_if_no_candidate_exists() {
        Board b = TestBoard.empty();
        Queen queen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queen, new Position(4, 3));
        String token = "Qb3";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_if_pawn_captures() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(4, 4));
        Knight knight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight, new Position(3, 5));
        String token = "exf5";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }
}
