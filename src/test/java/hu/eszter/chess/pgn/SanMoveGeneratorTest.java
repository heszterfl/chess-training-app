package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.testutil.TestBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SanMoveGeneratorTest {

    SanMoveGenerator generator;

    @BeforeEach
    void setup() {
        generator = new SanMoveGenerator();
    }

    @Test
    void toSan_generates_simple_pawn_move () {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(6, 4));

        Move move = new Move(pawn, PieceColor.WHITE, new Position(6, 4), new Position(4, 4));

        String sanToken = generator.toSan(b, move);

        assertEquals("e4", sanToken);
    }

    @Test
    void toSan_generates_knight_move() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Knight knight = new Knight(PieceColor.WHITE);
        TestBoard.place(b, knight, new Position(7, 6));

        Move move = new Move(knight, PieceColor.WHITE, new Position(7, 6), new Position(5, 5));

        String sanToken = generator.toSan(b, move);

        assertEquals("Nf3", sanToken);
    }

    @Test
    void toSan_generates_piece_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Queen queen = new Queen(PieceColor.WHITE);
        TestBoard.place(b, queen, new Position(7, 3));

        Pawn pawn = new Pawn(PieceColor.BLACK);
        TestBoard.place(b, pawn, new Position(3, 3));

        Move move = new Move(queen, PieceColor.WHITE, new Position(7, 3), new Position(3, 3));

        String sanToken = generator.toSan(b, move);

        assertEquals("Qxd5", sanToken);
    }

    @Test
    void toSan_generates_pawn_capture() {
        Board b = TestBoard.empty();
        TestBoard.placeDefaultKings(b);

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(4, 4));

        Bishop bishop = new Bishop(PieceColor.BLACK);
        TestBoard.place(b, bishop, new Position(3, 3));

        Move move = new Move(pawn, PieceColor.WHITE, new Position(4, 4), new Position(3, 3));

        String sanToken = generator.toSan(b, move);

        assertEquals("exd5", sanToken);
    }

    @Test
    void toSan_generates_kingside_castling() {
        Board b = TestBoard.empty();

        King whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7, 7));

        King blackKing = new King(PieceColor.BLACK);
        TestBoard.place(b, blackKing, new Position(0, 4));

        Move move = new Move(whiteKing, PieceColor.WHITE, new Position(7, 4), new Position(7, 6));

        String sanToken = generator.toSan(b, move);

        assertEquals("O-O", sanToken);
    }

    @Test
    void toSan_generates_queenside_castling() {
        Board b = TestBoard.empty();

        King whiteKing = new King(PieceColor.WHITE);
        TestBoard.place(b, whiteKing, new Position(7, 4));

        Rook rook = new Rook(PieceColor.WHITE);
        TestBoard.place(b, rook, new Position(7, 0));

        King blackKing = new King(PieceColor.BLACK);
        TestBoard.place(b, blackKing, new Position(0, 4));

        Move move = new Move(whiteKing, PieceColor.WHITE, new Position(7, 4), new Position(7, 2));

        String sanToken = generator.toSan(b, move);

        assertEquals("O-O-O", sanToken);
    }

    @Test
    void toSan_generates_simple_promotion() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1,0));

        Move move = new Move(pawn, PieceColor.WHITE, new Position(1, 0), new Position(0, 0));

        String sanToken = generator.toSan(b, move);

        assertEquals("a8=Q", sanToken);
    }

    @Test
    void toSan_generates_capture_promotion() {
        Board b = TestBoard.empty();
        TestBoard.placeKings(b, new Position(7, 4), new Position(1, 4));

        Pawn pawn = new Pawn(PieceColor.WHITE);
        TestBoard.place(b, pawn, new Position(1,0));

        Rook rook = new Rook(PieceColor.BLACK);
        TestBoard.place(b, rook, new Position(0, 1));

        Move move = new Move(pawn, PieceColor.WHITE, new Position(1, 0), new Position(0, 1));

        String sanToken = generator.toSan(b, move);

        assertEquals("axb8=Q", sanToken);
    }
}
