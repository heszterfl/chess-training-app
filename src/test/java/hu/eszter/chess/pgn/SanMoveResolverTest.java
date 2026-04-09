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
    void resolve_pawn_capture_with_two_options_chooses_correct_pawn_on_e_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn1 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn1, new Position(4, 2));

        Pawn pawn2 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn2, new Position(4, 4));

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
    void resolve_pawn_capture_with_two_options_chooses_correct_pawn_on_c_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn1 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn1, new Position(4, 2));

        Pawn pawn2 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn2, new Position(4, 4));

        Knight knight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight, new Position(3, 3));

        String token = "cxd5";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(4, 2), move.from());
        assertEquals(new Position(3, 3), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
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
    void resolve_white_kingside_castling() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7,7));

        String token = "O-O";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 4), move.from());
        assertEquals(new Position(7, 6), move.to());
        assertEquals(PieceKind.KING, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_white_queenside_castling() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7,0));

        String token = "O-O-O";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 4), move.from());
        assertEquals(new Position(7, 2), move.to());
        assertEquals(PieceKind.KING, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_throws_if_white_kingside_castling_not_legal() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7,7));

        Bishop whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(7, 5));

        String token = "O-O";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_ignores_check_suffix_for_castling() {
        Board b = TestBoard.empty();

        King whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7,7));

        King blackKing = new King(PieceColor.BLACK);
        TestBoard.place(b, blackKing, new Position(0, 5));

        String token = "O-O+";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 4), move.from());
        assertEquals(new Position(7, 6), move.to());
        assertEquals(PieceKind.KING, move.piece().getPieceKind());
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

    @Test
    void resolve_throws_if_pawn_token_invalid() {
        Board b = new Board();

        String token = "i4";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_if_piece_token_invalid() {
        Board b = new Board();

        String token = "A3";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_Nbd2_chooses_knight_on_b_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Knight knight1 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight1, new Position(7, 1));

        Knight knight2 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight2,new Position(5, 5));

        String token = "Nbd2";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 1), move.from());
        assertEquals(new Position(6, 3), move.to());
        assertEquals(PieceKind.KNIGHT, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_Nfd2_chooses_knight_on_f_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Knight knight1 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight1, new Position(7, 1));

        Knight knight2 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight2,new Position(5, 5));

        String token = "Nfd2";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(5, 5), move.from());
        assertEquals(new Position(6, 3), move.to());
        assertEquals(PieceKind.KNIGHT, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_Raxc2_chooses_rook_on_a_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook1 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(6, 0));

        Rook rook2 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook2, new Position(6, 7));

        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(6, 2));

        String token = "Raxc2";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(6, 0), move.from());
        assertEquals(new Position(6, 2), move.to());
        assertEquals(PieceKind.ROOK, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_throws_if_no_legal_piece_exists_on_source_file() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook1 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(6, 1));

        Rook rook2 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook2, new Position(6, 7));

        String token = "Rac2";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_N1d2_chooses_knight_on_first_rank() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Knight knight1 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight1, new Position(7, 1));

        Knight knight2 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight2,new Position(5, 1));

        String token = "N1d2";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(7, 1), move.from());
        assertEquals(new Position(6, 3), move.to());
        assertEquals(PieceKind.KNIGHT, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_N3d2_chooses_knight_on_third_rank() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Knight knight1 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight1, new Position(7, 1));

        Knight knight2 = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight2,new Position(5, 1));

        String token = "N3d2";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(5, 1), move.from());
        assertEquals(new Position(6, 3), move.to());
        assertEquals(PieceKind.KNIGHT, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_R2xa5_chooses_rook_on_second_rank() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook1 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(6, 0));

        Rook rook2 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook2, new Position(1, 0));

        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(3, 0));

        String token = "R2xa5";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(6, 0), move.from());
        assertEquals(new Position(3, 0), move.to());
        assertEquals(PieceKind.ROOK, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_throws_if_no_legal_piece_exists_on_source_rank() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook1 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook1, new Position(6, 1));

        Rook rook2 = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook2, new Position(1, 1));

        String token = "R3c4";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_white_pawn_promotion() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 0));

        String token = "a8=Q";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(1, 0), move.from());
        assertEquals(new Position(0, 0), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_white_capture_promotion() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 0));

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(0, 1));

        String token = "axb8=Q";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(1, 0), move.from());
        assertEquals(new Position(0, 1), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_promotion_ignores_check_suffix() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 0));

        String token = "a8=Q+";

        Move move = sanMoveResolver.resolve(token, b);

        assertNotNull(move);
        assertEquals(new Position(1, 0), move.from());
        assertEquals(new Position(0, 0), move.to());
        assertEquals(PieceKind.PAWN, move.piece().getPieceKind());
        assertEquals(PieceColor.WHITE, move.color());
    }

    @Test
    void resolve_promotion_throws_if_no_candidate_exists() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 1));

        String token = "a8=Q+";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }

    @Test
    void resolve_throws_for_non_queen_promotion() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 0));

        String token = "a8=N";

        assertThrows(IllegalArgumentException.class, () -> sanMoveResolver.resolve(token, b));
    }
}
