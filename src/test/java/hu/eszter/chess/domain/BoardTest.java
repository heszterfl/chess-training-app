package hu.eszter.chess.domain;

import hu.eszter.chess.testutil.TestBoard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void king_in_check() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(0, 0), new Position(0, 7));

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(0, 6)); // g8

        List<Position> captures = queen.getLegalCaptures(b.getBoard(), queen.getCurrentPosition());

        assertTrue(captures.contains(new Position(0, 0)));
    }


    @Test
    void king_in_check_piece_can_block_on_e3() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(6, 3));    // d2

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 3), new Position(5, 4)));
    }

    @Test
    void king_in_check_piece_cannot_play_non_blocking_move() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(6, 3));    // d2

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(6, 3), new Position(5, 2)));
    }

    @Test
    void king_in_check_bishop_can_capture_attacker() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(5, 3));    // d3

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(6, 4));  // e2

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(5, 3), new Position(6, 4)));
    }

    @Test
    void king_in_check_king_can_capture_attacker() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(6, 4));  // e2

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(6, 4)));
    }

    @Test
    void king_in_check_king_can_move_away() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(7, 5)));
    }

    @Test
    void king_in_double_check_only_king_move_saves() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 1));    // b4

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(6, 5)));
    }

    @Test
    void king_in_double_check_bishop_cannot_capture_an_attacker() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(1, 3));    // d7

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 1));    // b4

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(1, 3), new Position(0, 4)));
    }

    @Test
    void king_in_double_check_rook_cannot_capture_an_attacker() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(4, 2));  // c4

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 1));    //b4

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 2), new Position(4, 1)));
    }

    @Test
    void king_in_double_check_bishop_cannot_block_an_attack() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(1, 3));    // d7

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 1));    // b4

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(1, 3), new Position(2, 4)));
    }

    @Test
    void king_in_double_check_rook_cannot_block_an_attack() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(4, 2));  // c4

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 1));    // b4

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 2), new Position(5, 2)));
    }


    @Test
    void pinned_piece_cannot_move_aside() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteKnight = new Knight(PieceColor.WHITE);
        TestBoard.place(b, whiteKnight, new Position(6, 4));    // e2

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(6, 4), new Position(4, 5)));
    }

    @Test
    void pinned_piece_can_move_if_it_still_blocks() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(6, 4));  // e2

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 4), new Position(5, 4)));
    }

    @Test
    void pinned_piece_can_capture_attacker() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(6, 4));  // e2

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e8

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 4), new Position(1, 4)));
    }

    @Test
    void king_cannot_move_into_attack() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 3), new Position(0, 4));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));  // e7

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 3), new Position(7, 4)));
    }

    @Test
    void king_checkmated() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(0, 0), new Position(7, 7));

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(1, 6));  // g7

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(2, 7));   // h6

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(2, 7), new Position(0, 7)));
        assertTrue(b.isCheckmated(PieceColor.WHITE));
    }

    @Test
    void king_stalemated() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(0, 0), new Position(7, 7));

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(7, 1));  // b1

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(2, 7));   // h6

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(2, 7), new Position(1, 7)));
        assertTrue(b.isStalemated(PieceColor.WHITE));
    }

    @Test
    void white_kingside_castling_succeeds() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7, 7));   // h1

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(7, 6)));
        assertNull(b.getPieceAt(new Position(7, 4)));
        assertNull(b.getPieceAt(new Position(7, 7)));
        assertEquals(PieceKind.KING, b.getPieceAt(new Position(7, 6)).getPieceKind());
        assertEquals(PieceKind.ROOK, b.getPieceAt(new Position(7, 5)).getPieceKind());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(7, 6)).getColor());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(7, 5)).getColor());
    }

    @Test
    void white_queenside_castling_succeeds() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7, 0));

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(7, 2)));
        assertNull(b.getPieceAt(new Position(7, 4)));
        assertNull(b.getPieceAt(new Position(7, 0)));
        assertEquals(PieceKind.KING, b.getPieceAt(new Position(7, 2)).getPieceKind());
        assertEquals(PieceKind.ROOK, b.getPieceAt(new Position(7, 3)).getPieceKind());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(7, 2)).getColor());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(7, 3)).getColor());
    }

    @Test
    void white_castling_fails_if_king_is_in_check() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        Rook blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(1, 4));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_castling_fails_if_path_square_is_attacked() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        Knight blackKnight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, blackKnight, new Position(5, 6));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));

    }

    @Test
    void white_castling_fails_if_destination_is_attacked() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        Bishop blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(4, 3));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_castling_fails_if_piece_is_between_king_and_rook() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        Bishop bishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, bishop, new Position(7, 5));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_queenside_castling_fails_if_b1_is_occupied() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 0));

        Knight knight = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight, new Position(7, 1));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 2)));
    }

    @Test
    void white_castling_fails_if_rook_is_missing() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_castling_fails_if_king_has_already_moved() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        b.whiteToMove = true;
        b.setWhiteKingMovedForTest(true);

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_castling_fails_if_rook_has_already_moved() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        b.whiteToMove = true;
        b.setWhiteKingsideRookMovedForTest(true);

        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void white_castling_fails_if_rook_has_been_removed() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 7));

        Bishop whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(6, 6));

        Rook blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(5, 7));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(5, 7), new Position(7, 7)));
        assertTrue(b.tryMove(new Position(6, 6), new Position(7, 7)));
        assertTrue(b.tryMove(new Position(0, 4), new Position(1, 4)));
        assertTrue(b.tryMove(new Position(7, 7), new Position(6, 6)));
        assertTrue(b.tryMove(new Position(1, 4), new Position(2, 4)));
        assertFalse(b.tryMove(new Position(7, 4), new Position(7, 6)));
    }

    @Test
    void black_kingside_castling_succeeds() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(0, 7));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(0, 4), new Position(0, 6)));
        assertNull(b.getPieceAt(new Position(0, 4)));
        assertNull(b.getPieceAt(new Position(0, 7)));
        assertEquals(PieceKind.KING, b.getPieceAt(new Position(0, 6)).getPieceKind());
        assertEquals(PieceKind.ROOK, b.getPieceAt(new Position(0, 5)).getPieceKind());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(0, 6)).getColor());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(0, 5)).getColor());
    }

    @Test
    void black_queenside_castling_succeeds() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(0, 0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(0, 4), new Position(0, 2)));
        assertNull(b.getPieceAt(new Position(0, 4)));
        assertNull(b.getPieceAt(new Position(0, 0)));
        assertEquals(PieceKind.KING, b.getPieceAt(new Position(0, 2)).getPieceKind());
        assertEquals(PieceKind.ROOK, b.getPieceAt(new Position(0, 3)).getPieceKind());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(0, 2)).getColor());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(0, 3)).getColor());
    }

    @Test
    void get_squares_between_attacker_and_king_down_right() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(0, 1));  // b8

        List<Position> squaresBetween = b.getSquaresBetween(queen.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(1, 2)));
        assertTrue(squaresBetween.contains(new Position(2, 3)));
        assertFalse(squaresBetween.contains(new Position(0, 1)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_down_left() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(0, 7));  // h8

        List<Position> squaresBetween = b.getSquaresBetween(bishop.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(1, 6)));
        assertTrue(squaresBetween.contains(new Position(2, 5)));
        assertFalse(squaresBetween.contains(new Position(0, 7)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_up_left() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(6, 7));  // h2

        List<Position> squaresBetween = b.getSquaresBetween(queen.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(5, 6)));
        assertTrue(squaresBetween.contains(new Position(4, 5)));
        assertFalse(squaresBetween.contains(new Position(6, 7)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_up_right() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(7, 0));  // a1

        List<Position> squaresBetween = b.getSquaresBetween(bishop.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(6, 1)));
        assertTrue(squaresBetween.contains(new Position(5, 2)));
        assertTrue(squaresBetween.contains(new Position(4, 3)));
        assertFalse(squaresBetween.contains(new Position(7, 0)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_down() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(0, 4));  // e8

        List<Position> squaresBetween = b.getSquaresBetween(queen.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(1, 4)));
        assertTrue(squaresBetween.contains(new Position(2, 4)));
        assertFalse(squaresBetween.contains(new Position(0, 4)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_left() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(3, 7));  // h5

        List<Position> squaresBetween = b.getSquaresBetween(rook.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(3, 6)));
        assertTrue(squaresBetween.contains(new Position(3, 5)));
        assertFalse(squaresBetween.contains(new Position(3, 7)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_up() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(7, 4));  // e1

        List<Position> squaresBetween = b.getSquaresBetween(rook.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(6, 4)));
        assertTrue(squaresBetween.contains(new Position(5, 4)));
        assertTrue(squaresBetween.contains(new Position(4, 4)));
        assertFalse(squaresBetween.contains(new Position(7, 4)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void get_squares_between_attacker_and_king_right() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(3, 0));  // a5

        List<Position> squaresBetween = b.getSquaresBetween(queen.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(3, 1)));
        assertTrue(squaresBetween.contains(new Position(3, 2)));
        assertTrue(squaresBetween.contains(new Position(3, 3)));
        assertFalse(squaresBetween.contains(new Position(3, 0)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }

    @Test
    void custom_position_first_legal_move_succeeds() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(5, 2), new Position(3, 2));

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(4, 4));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(3, 0));

        assertTrue(b.tryMove(new Position(4, 4), new Position(3, 4)));
        assertSame(whitePawn, b.getPieceAt(new Position(3, 4)));
        assertNull(b.getPieceAt(new Position(4, 4)));
    }

    @Test
    void initial_position_first_legal_move_succeeds() {
        Board b = new Board();

        var piece = b.getPieceAt(new Position(6, 4));

        assertTrue(b.tryMove(new Position(6, 4), new Position(4, 4)));
        assertSame(piece, b.getPieceAt(new Position(4, 4)));
        assertNull(b.getPieceAt(new Position(6, 4)));
    }

    @Test
    void piece_does_not_capture_own_piece() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(0, 3));

        Piece queen = new Queen(PieceColor.WHITE);
        Piece pawn = new Pawn(PieceColor.WHITE);

        TestBoard.place(b, queen, new Position(4, 4));
        TestBoard.place(b, pawn, new Position(5, 4));

        assertFalse(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void tryMove_returns_true_if_correct_side_wants_to_move() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece queen = new Queen(PieceColor.WHITE);

        TestBoard.place(b, queen, new Position(4, 3));
        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(4, 3), new Position(6, 3)));
    }

    @Test
    void tryMove_returns_false_if_wrong_side_wants_to_move() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece queen = new Queen(PieceColor.BLACK);

        TestBoard.place(b, queen, new Position(4, 3));
        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 3), new Position(1, 3)));
    }

    @Test
    void tryMove_returns_false_if_from_equals_to() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Piece queen = new Queen(PieceColor.BLACK);

        TestBoard.place(b, queen, new Position(4, 3));

        assertFalse(b.tryMove(new Position(4, 3), new Position(1, 3)));
    }

    @Test
    void tryMove_returns_false_if_no_piece_on_currentPos() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        assertFalse(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void illegal_move_does_not_change_board_state() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(3, 7), new Position(3, 1));

        Piece queen = new Queen(PieceColor.WHITE);
        Piece pawn = new Pawn(PieceColor.WHITE);

        TestBoard.place(b, queen, new Position(4, 4));
        TestBoard.place(b, pawn, new Position(5, 4));

        int pastMovesCountBefore = b.getPastMoves().size();

        boolean notMoved = b.tryMove(new Position(4, 4), new Position(5, 4));

        int pastMovesCountAfter = b.getPastMoves().size();

        assertFalse(notMoved);
        assertEquals(new Position(4, 4), queen.getCurrentPosition());
        assertEquals(new Position(5, 4), pawn.getCurrentPosition());
        assertEquals(pastMovesCountBefore, pastMovesCountAfter);
    }

    @Test
    void simulate_and_apply_castling_produce_same_board_state() {
        Board original = TestBoard.empty();
        TestBoard.placeDefaultKings(original);

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(original, rook, new Position(7, 7));

        original.whiteToMove = true;

        Board simulated = new Board(original);
        Piece simKing = simulated.getPieceAt(new Position(7, 4));

        simulated.simulateMove(simKing, new Position(7, 4), new Position(7, 6));

        original.tryMove(new Position(7, 4), new Position(7, 6));

        assertEquals(
                original.getPieceAt(new Position(7, 6)).getPieceKind(),
                simulated.getPieceAt(new Position(7, 6)).getPieceKind()
        );

        assertEquals(
                original.getPieceAt(new Position(7, 5)).getPieceKind(),
                simulated.getPieceAt(new Position(7, 5)).getPieceKind()
        );

        assertNull(simulated.getPieceAt(new Position(7, 4)));
        assertNull(simulated.getPieceAt(new Position(7, 7)));
    }

    @Test
    void en_passant_is_executable_move() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(3, 1));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(1, 0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(1, 0), new Position(3, 0)));
        assertTrue(b.tryMove(new Position(3, 1), new Position(2, 0)));
    }

    @Test
    void en_passant_removes_captured_pawn() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(3, 1));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(1, 0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(1, 0), new Position(3, 0)));
        assertTrue(b.tryMove(new Position(3, 1), new Position(2, 0)));
        assertNull(b.getPieceAt(new Position(3, 1)));
        assertNull(b.getPieceAt(new Position(3, 0)));
        assertEquals(PieceKind.PAWN, b.getPieceAt(new Position(2, 0)).getPieceKind());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(2, 0)).getColor());
    }

    @Test
    void en_passant_is_illegal_without_immediate_previous_double_step() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(3, 1));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(1, 0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(1, 0), new Position(3, 0)));
        assertTrue(b.tryMove(new Position(7, 4), new Position(6, 4)));
        assertTrue(b.tryMove(new Position(0, 4), new Position(0, 3)));

        assertFalse(b.tryMove(new Position(3, 1), new Position(2, 0)));
    }

    @Test
    void en_passant_is_illegal_due_to_pin() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(5, 4), new Position(0, 7));

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(0, 4));

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(3, 4));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(1, 3));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(1, 3), new Position(3, 3)));
        assertFalse(b.tryMove(new Position(3, 4), new Position(2, 3)));
    }

    @Test
    void white_pawn_promotes_to_queen() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 1));

        assertTrue(b.tryMove(new Position(1, 1), new Position(0, 1)));
        assertEquals(PieceKind.QUEEN, b.getPieceAt(new Position(0, 1)).getPieceKind());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(0, 1)).getColor());
    }

    @Test
    void black_pawn_promotes_to_queen() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(6, 4), new Position(0, 4));

        Pawn pawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, pawn, new Position(6, 1));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(6, 1), new Position(7, 1)));
        assertEquals(PieceKind.QUEEN, b.getPieceAt(new Position(7, 1)).getPieceKind());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(7, 1)).getColor());
    }

    @Test
    void promotion_replaces_pawn() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 1));

        assertTrue(b.tryMove(new Position(1, 1), new Position(0, 1)));
        assertNull(b.getPieceAt(new Position(1, 1)));
    }

    @Test
    void white_pawn_promotion_via_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1, 1));

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(0,0));

        assertTrue(b.tryMove(new Position(1, 1), new Position(0, 0)));
        assertEquals(PieceKind.QUEEN, b.getPieceAt(new Position(0, 0)).getPieceKind());
        assertEquals(PieceColor.WHITE, b.getPieceAt(new Position(0, 0)).getColor());
        assertNull(b.getPieceAt(new Position(1, 1)));
    }

    @Test
    void black_pawn_promotion_via_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(6, 4), new Position(0, 4));

        Pawn pawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, pawn, new Position(6, 1));

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7,0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(6, 1), new Position(7, 0)));
        assertEquals(PieceKind.QUEEN, b.getPieceAt(new Position(7, 0)).getPieceKind());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(7, 0)).getColor());
        assertNull(b.getPieceAt(new Position(6, 1)));
    }

    @Test
    void checkmate_after_black_pawn_promotion_via_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 7), new Position(0, 4));

        Pawn whitePawn1 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn1, new Position(6, 7));

        Pawn whitePawn2 = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn2, new Position(6, 6));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(6, 1));

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7,0));

        b.whiteToMove = false;

        assertTrue(b.tryMove(new Position(6, 1), new Position(7, 0)));
        assertEquals(PieceKind.QUEEN, b.getPieceAt(new Position(7, 0)).getPieceKind());
        assertEquals(PieceColor.BLACK, b.getPieceAt(new Position(7, 0)).getColor());
        assertTrue(b.isCheckmate);
    }
}
