package hu.eszter.chess.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    @Test
    void whitePawn_first_move_can_move_one_or_two_if_clear() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 4); // e-file
        TestBoard.place(b, pawn, new Position(6, 4)); // e2

        List<Position> moves = pawn.getLegalMoves(b.getBoard(), pawn.getCurrentPosition());

        assertTrue(moves.contains(new Position(5, 4))); // e3
        assertTrue(moves.contains(new Position(4, 4))); // e4
    }

    @Test
    void whitePawn_first_move_blocked_cannot_move() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 4);
        TestBoard.place(b, pawn, new Position(6, 4)); // e2

        Pawn blocker = new Pawn(PieceColor.WHITE, 4);
        TestBoard.place(b, blocker, new Position(5, 4)); // e3

        List<Position> moves = pawn.getLegalMoves(b.getBoard(), pawn.getCurrentPosition());

        assertFalse(moves.contains(new Position(5, 4)));
        assertFalse(moves.contains(new Position(4, 4)));
    }

    @Test
    void whitePawn_capture_diagonal_only() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 4);
        TestBoard.place(b, pawn, new Position(6, 4)); // e2

        Pawn enemyLeft = new Pawn(PieceColor.BLACK, 3);
        TestBoard.place(b, enemyLeft, new Position(5, 3)); // d3

        Pawn enemyFront = new Pawn(PieceColor.BLACK, 5);
        TestBoard.place(b, enemyFront, new Position(5, 4)); // e3

        Pawn enemyRight = new Pawn(PieceColor.BLACK, 5);
        TestBoard.place(b, enemyRight, new Position(5, 5)); // f3

        List<Position> captures = pawn.getLegalCaptures(b.getBoard(), pawn.getCurrentPosition());

        assertTrue(captures.contains(new Position(5, 3)));
        assertFalse(captures.contains(new Position(5, 4)));
        assertTrue(captures.contains(new Position(5, 5)));
    }

    @Test
    void whitePawn_on_edge_legal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 7);
        TestBoard.place(b, pawn, new Position(3, 7)); // h5

        Pawn enemyG = new Pawn(PieceColor.BLACK, 6);
        TestBoard.place(b, enemyG, new Position(3, 6)); // g5

        Move lastMove = new Move(enemyG, PieceColor.BLACK, new Position(1, 6), new Position(3, 6));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertEquals(new Position(2, 6), enPassantCapture);
    }

    @Test
    void whitePawn_in_middle_legal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 4);
        TestBoard.place(b, pawn, new Position(3, 4));   // e5

        Pawn enemyD = new Pawn(PieceColor.BLACK, 3);
        TestBoard.place(b, enemyD, new Position(3, 3)); // d5

        Pawn enemyF = new Pawn(PieceColor.BLACK, 5);
        TestBoard.place(b, enemyF, new Position(3, 5)); // f5

        Move lastMove = new Move(enemyD, PieceColor.BLACK, new Position(1, 3), new Position(3, 3));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertEquals(new Position(2, 3), enPassantCapture);
        assertNotEquals(new Position(2, 5), enPassantCapture);
    }

    @Test
    void white_Pawn_illegal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn(PieceColor.WHITE, 7);
        TestBoard.place(b, pawn, new Position(3, 7)); // h4

        Pawn enemyG = new Pawn(PieceColor.BLACK, 6);
        TestBoard.place(b, enemyG, new Position(3, 6));

        Move lastMove = new Move(enemyG, PieceColor.BLACK, new Position(2, 6), new Position(3, 6));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertNull(enPassantCapture);
    }

    @Test
    void white_Pawn_capture_within_boundaries() {
        Board b = TestBoard.empty();
        Pawn pawnRight = new Pawn(PieceColor.WHITE, 7);
        Pawn pawnMiddle = new Pawn(PieceColor.WHITE, 4);
        Pawn pawnLeft = new Pawn(PieceColor.WHITE, 0);
        TestBoard.place(b, pawnRight, new Position(3, 7)); // h5
        TestBoard.place(b, pawnMiddle, new Position(3, 4)); // e5
        TestBoard.place(b, pawnLeft, new Position(3, 0)); // a5

        Queen queenOnG = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queenOnG, new Position(2, 6));  // g6
        Queen queenOnD = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queenOnD, new Position(2, 3));  // d6
        Queen queenOnF = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queenOnF, new Position(2, 5));  // f6
        Queen queenOnB = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queenOnB, new Position(2, 1));  // b6

        List<Position> capturesRight = pawnRight.getLegalCaptures(b.getBoard(), pawnRight.getCurrentPosition());
        List<Position> capturesMiddle = pawnMiddle.getLegalCaptures(b.getBoard(), pawnMiddle.getCurrentPosition());
        List<Position> capturesLeft = pawnLeft.getLegalCaptures(b.getBoard(), pawnLeft.getCurrentPosition());

        assertTrue(capturesLeft.contains(new Position(2, 1)));
        assertTrue(capturesMiddle.contains(new Position(2, 3)));
        assertFalse(capturesLeft.contains(new Position(2, 5)));
        assertTrue(capturesRight.contains(new Position(2, 6)));

    }
}
