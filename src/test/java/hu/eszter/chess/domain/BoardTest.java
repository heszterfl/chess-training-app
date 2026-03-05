package hu.eszter.chess.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    void king_in_check() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(0, 0));  // a8
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(0, 6)); // g8

        List<Position> captures = queen.getLegalCaptures(b.getBoard(), queen.getCurrentPosition());

        assertTrue(captures.contains(new Position(0, 0)));
    }

    @Test
    void king_in_check_can_capture() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(0, 0));
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(1, 0));

        b.whiteToMove = true;
        b.setLastMove(new Move(queen, "black", new Position(2, 1), new Position(1, 0)));

        assertTrue(b.tryMove(new Position(0, 0), new Position(1, 0)));
    }

    @Test
    void king_in_check_can_move_away() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(1, 0));
        b.whiteKingPosition = new Position(1, 0);

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(2, 0));

        b.whiteToMove = true;
        b.setLastMove(new Move(queen, "black", new Position(3, 1), new Position(2, 0)));

        assertTrue(b.tryMove(new Position(1, 0), new Position(0, 1)));
    }

    @Test
    void king_in_check_other_piece_can_capture_attacker() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(0, 0));
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(2, 0));

        Bishop bishop = new Bishop("white");
        TestBoard.place(b, bishop, new Position(0, 2));

        b.whiteToMove = true;
        b.setLastMove(new Move(queen, "black", new Position(3, 1), new Position(2, 0)));

        assertTrue(b.tryMove(new Position(0, 2), new Position(2, 0)));
    }

    @Test
    void king_in_check_other_piece_can_block_attack() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(0, 0));
        b.whiteKingPosition = new Position(0, 0);
        b.kingPosition = b.whiteKingPosition;

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(3, 0));

        Bishop bishop = new Bishop("white");
        TestBoard.place(b, bishop, new Position(3, 2));

        b.whiteToMove = true;
        b.setLastMove(new Move(queen, "black", new Position(3, 1), new Position(3, 0)));

        assertTrue(b.tryMove(new Position(3, 2), new Position(1, 0)));
    }

    @Test
    void king_checkmated() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(0, 0));
        b.whiteKingPosition = new Position(0, 0);

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(1, 6));
        Rook rook = new Rook("black");
        TestBoard.place(b, rook, new Position(2, 7));

        Move lastMove = new Move(king, "white", new Position(1, 1), new Position(0, 0));
        b.setLastMove(lastMove);
        b.whiteToMove = false;
        b.tryMove(new Position(2, 7), new Position(0, 7));

        assertTrue(b.isCheckmated(king.getCurrentPosition()));
    }

    @Test
    void get_squares_between_attacker_and_king_down_right() {
        Board b = TestBoard.empty();
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Bishop bishop = new Bishop("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Bishop bishop = new Bishop("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Rook rook = new Rook("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Rook rook = new Rook("black");
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
        King king = new King("white");
        TestBoard.place(b, king, new Position(3, 4));   // e5

        Queen queen = new Queen("black");
        TestBoard.place(b, queen, new Position(3, 0));  // a5

        List<Position> squaresBetween = b.getSquaresBetween(queen.getCurrentPosition(), king.getCurrentPosition());

        assertTrue(squaresBetween.contains(new Position(3, 1)));
        assertTrue(squaresBetween.contains(new Position(3, 2)));
        assertTrue(squaresBetween.contains(new Position(3, 3)));
        assertFalse(squaresBetween.contains(new Position(3, 0)));
        assertFalse(squaresBetween.contains(new Position(3, 4)));
    }
}
