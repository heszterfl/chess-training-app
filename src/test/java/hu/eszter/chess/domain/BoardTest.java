package hu.eszter.chess.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void king_in_check() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(0, 0));  // a8
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(0, 6)); // g8

        List<Position> captures = queen.getLegalCaptures(b.getBoard(), queen.getCurrentPosition());

        assertTrue(captures.contains(new Position(0, 0)));
    }


    @Test
    void kingInCheck_piece_can_block_on_e3() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteBishop, new Position(6, 3));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 3), new Position(5, 4)));
    }

    @Test
    void king_in_check_piece_cannot_play_non_blocking_move() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteBishop, new Position(6, 3));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(6, 3), new Position(5, 2)));
    }

    @Test
    void king_in_check_bishop_can_capture_attacker() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));  // e1
        TestBoard.place(b, whiteBishop, new Position(5, 3));    // d3

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(6, 4));  // e2

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(5, 3), new Position(6, 4)));
    }

    @Test
    void king_in_check_king_can_capture_attacker() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));  // e1

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(6, 4));  // e2

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(6, 4)));
    }

    @Test
    void king_in_check_king_can_move_away() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));  // e1

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));  // e8

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(7, 5)));
    }

    @Test
    void king_in_double_check_only_king_move_saves() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));

        Piece blackRook = new Rook(PieceColor.BLACK);
        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));
        TestBoard.place(b, blackBishop, new Position(4, 1));

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(7, 4), new Position(6, 5)));
    }

    @Test
    void king_in_double_check_bishop_cannot_capture_an_attacker() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteBishop, new Position(1, 3));

        Piece blackRook = new Rook(PieceColor.BLACK);
        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));
        TestBoard.place(b, blackBishop, new Position(4, 1));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(1, 3), new Position(0, 4)));
    }

    @Test
    void king_in_double_check_rook_cannot_capture_an_attacker() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteRook, new Position(4, 2));

        Piece blackRook = new Rook(PieceColor.BLACK);
        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));
        TestBoard.place(b, blackBishop, new Position(4, 1));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 2), new Position(4, 1)));
    }

    @Test
    void king_in_double_check_bishop_cannot_block_an_attack() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteBishop, new Position(1, 3));

        Piece blackRook = new Rook(PieceColor.BLACK);
        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));
        TestBoard.place(b, blackBishop, new Position(4, 1));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(1, 3), new Position(2, 4)));
    }

    @Test
    void king_in_double_check_rook_cannot_block_an_attack() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteRook, new Position(4, 2));

        Piece blackRook = new Rook(PieceColor.BLACK);
        Piece blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));
        TestBoard.place(b, blackBishop, new Position(4, 1));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 2), new Position(5, 2)));
    }


    @Test
    void pinned_piece_cannot_move_aside() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteKnight = new Knight(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteKnight, new Position(6, 4));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(6, 4), new Position(4, 5)));
    }

    @Test
    void pinned_piece_can_move_if_it_still_blocks() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteRook, new Position(6, 4));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 4), new Position(1, 4)));
    }

    @Test
    void pinned_piece_can_capture_attacker() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        Piece whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));
        TestBoard.place(b, whiteRook, new Position(6, 4));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(6, 4), new Position(0, 4)));
    }

    @Test
    void king_cannot_move_into_attack() {
        Board b = TestBoard.empty();

        Piece whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 3));

        Piece blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 4));

        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(7, 3), new Position(7, 4)));
    }

    @Test
    void king_checkmated() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(0, 0));
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(1, 6));
        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(2, 7));

        Move lastMove = new Move(king, PieceColor.WHITE, new Position(1, 1), new Position(0, 0));
        b.setLastMove(lastMove);
        b.whiteToMove = false;
        b.tryMove(new Position(2, 7), new Position(0, 7));

        assertTrue(b.isCheckmated(king.getCurrentPosition()));
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
        King whiteKing = new King(PieceColor.WHITE);
        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        King blackKing = new King(PieceColor.BLACK);
        Pawn blackPawn = new Pawn(PieceColor.BLACK);

        TestBoard.place(b, whiteKing, new Position(5, 2));
        TestBoard.place(b, blackKing, new Position(3, 2));
        TestBoard.place(b, whitePawn, new Position(4, 4));
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

        Piece queen = new Queen(PieceColor.WHITE);
        Piece pawn = new Pawn(PieceColor.WHITE);

        TestBoard.place(b, queen, new Position(4, 4));
        TestBoard.place(b, pawn, new Position(5, 4));

        assertFalse(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void tryMove_returns_true_if_correct_side_wants_to_move() {
        Board b = TestBoard.empty();

        Piece queen = new Queen(PieceColor.WHITE);

        TestBoard.place(b, queen, new Position(4, 4));
        b.whiteToMove = true;

        assertTrue(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void tryMove_returns_false_if_wrong_side_wants_to_move() {
        Board b = TestBoard.empty();

        Piece queen = new Queen(PieceColor.BLACK);

        TestBoard.place(b, queen, new Position(4, 4));
        b.whiteToMove = true;

        assertFalse(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void tryMove_returns_false_if_from_equals_to() {
        Board b = TestBoard.empty();

        Piece queen = new Queen(PieceColor.BLACK);

        TestBoard.place(b, queen, new Position(4, 4));

        assertFalse(b.tryMove(new Position(4, 4), new Position(4, 4)));
    }

    @Test
    void tryMove_returns_false_if_no_piece_on_currentPos() {
        Board b = TestBoard.empty();

        assertFalse(b.tryMove(new Position(4, 4), new Position(5, 4)));
    }

    @Test
    void illegal_move_does_not_change_board_state() {
        Board b = TestBoard.empty();

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
}
