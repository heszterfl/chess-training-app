package hu.eszter.chess.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    @Test
    void white_king_does_not_move_into_check() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE, 3);
        TestBoard.place(b, pawn, new Position(4, 3));
        Queen queen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, queen, new Position(4, 2));
        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(4, 5));
        Knight knight1 = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight1, new Position(2, 3));
        Knight knight2 = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight2, new Position(1, 6));
        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(3, 5));

        List<Position> moves = king.getLegalMoves(b.getBoard(), king.getCurrentPosition());

        assertTrue(moves.contains(new Position(2, 5)));
        assertFalse(moves.contains(new Position(2, 4)));
        assertFalse(moves.contains(new Position(2, 3)));
        assertFalse(moves.contains(new Position(4, 3)));
    }

    @Test
    void white_king_can_not_capture_own_piece() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));  // e5

        Pawn pawn = new Pawn(PieceColor.WHITE, 3);
        TestBoard.place(b, pawn, new Position(4, 3));
        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(4, 5));

        List<Position> captures = king.getLegalCaptures(b.getBoard(), king.getCurrentPosition());

        assertFalse(captures.contains(new Position(4, 3)));  // d4
        assertFalse(captures.contains(new Position(4, 5)));  // f4
    }

    @Test
    void white_king_can_not_capture_protected_enemy_piece() {
        Board b = TestBoard.empty();
        King king = new King(PieceColor.WHITE);
        TestBoard.place(b, king, new Position(3, 4));  // e5

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(4, 5));  // f4
        Knight knight1 = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight1, new Position(2, 3));   // d6
        Knight knight2 = new Knight(PieceColor.BLACK);
        TestBoard.place(b, knight2, new Position(1, 6));   // g7
        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(3, 5));    // f5

        List<Position> captures = king.getLegalCaptures(b.getBoard(), king.getCurrentPosition());

        assertTrue(captures.contains(new Position(4, 5)));
        assertTrue(captures.contains(new Position(2, 3)));
        assertFalse(captures.contains(new Position(3, 5)));
    }

    @Test
    void square_is_protected_by_pawn() {
        Board b = TestBoard.empty();
        King whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(5, 6));

        Queen whiteQueen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, whiteQueen, new Position(5, 4));

        Rook whiteRook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, whiteRook, new Position(7, 2));

        Bishop whiteBishop = new Bishop(PieceColor.WHITE);
        TestBoard.place(b, whiteBishop, new Position(3, 3));

        Knight whiteKnight = new Knight(PieceColor.WHITE);
        TestBoard.place(b, whiteKnight, new Position(4, 6));

        Pawn whitePawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, whitePawn, new Position(4, 2));

        King blackKing = new King(PieceColor.BLACK);
        TestBoard.place(b, blackKing, new Position(1, 6));

        Queen blackQueen = new Queen(PieceColor.BLACK);
        TestBoard.place(b, blackQueen, new Position(0, 3));

        Rook blackRook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, blackRook, new Position(0, 5));

        Bishop blackBishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, blackBishop, new Position(1, 4));

        Knight blackKnight = new Knight(PieceColor.BLACK);
        TestBoard.place(b, blackKnight, new Position(0, 2));

        Pawn blackPawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, blackPawn, new Position(2, 1));

        assertTrue(whiteKing.isProtected(b.getBoard(), new Position(0, 3)));
        assertTrue(blackKing.isProtected(b.getBoard(), new Position(3, 3)));
    }
}
