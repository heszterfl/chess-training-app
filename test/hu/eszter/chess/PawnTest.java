package hu.eszter.chess;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {
    @Test
    void whitePawn_on_edge_legal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn("white", 7);
        TestBoard.place(b, pawn, new Position(3, 7)); // h5

        Pawn enemyG = new Pawn("black", 6);
        TestBoard.place(b, enemyG, new Position(3, 6)); // g5

        Move lastMove = new Move(enemyG, "black", new Position(1, 6), new Position(3, 6));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertEquals(new Position(2, 6), enPassantCapture);
    }

    @Test
    void whitePawn_in_middle_legal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn("white", 4);
        TestBoard.place(b, pawn, new Position(3, 4));   // e5

        Pawn enemyD = new Pawn("black", 3);
        TestBoard.place(b, enemyD, new Position(3, 3)); // d5

        Pawn enemyF = new Pawn("black", 5);
        TestBoard.place(b, enemyF, new Position(3, 5)); // f5

        Move lastMove = new Move(enemyD, "black", new Position(1, 3), new Position(3, 3));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertEquals(new Position(2, 3), enPassantCapture);
        assertNotEquals(new Position(2, 5), enPassantCapture);
    }

    @Test
    void white_Pawn_illegal_en_passant() {
        Board b = TestBoard.empty();
        Pawn pawn = new Pawn("white", 7);
        TestBoard.place(b, pawn, new Position(3, 7)); // h4

        Pawn enemyG = new Pawn("black", 6);
        TestBoard.place(b, enemyG, new Position(3, 6));

        Move lastMove = new Move(enemyG, "black", new Position(2, 6), new Position(3, 6));

        Position enPassantCapture = pawn.getEnPassant(lastMove);

        assertNull(enPassantCapture);
    }

}
