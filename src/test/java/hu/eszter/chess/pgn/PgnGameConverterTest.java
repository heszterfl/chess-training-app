package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.PieceKind;
import hu.eszter.chess.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PgnGameConverterTest {

    @Test
    void convert_returns_game_and_moves_for_valid_pgn_game() {
        PgnGame pgnGame = new PgnGame(null, null, "2026-03-18", "Carlsen", "Nakamura", "1-0", List.of("e4", "e5", "Nf3", "Nc6"));

        PgnGameConverter pgnGameConverter = new PgnGameConverter();
        ImportedGame imported = pgnGameConverter.convert(pgnGame);

        assertEquals("2026-03-18", imported.game().getDate());
        assertEquals("Carlsen", imported.game().getWhitePlayer());
        assertEquals("Nakamura", imported.game().getBlackPlayer());
        assertEquals("1-0", imported.game().getResult());

        assertEquals(4, imported.moves().size());
        assertEquals(new Position(6, 4), imported.moves().get(0).from());
        assertEquals(new Position(4, 4), imported.moves().get(0).to());
        assertEquals(PieceKind.PAWN, imported.moves().get(0).piece().getPieceKind());
        assertEquals(PieceColor.WHITE, imported.moves().get(0).color());

        assertEquals(new Position(3, 4), imported.moves().get(1).to());
        assertEquals(PieceKind.PAWN, imported.moves().get(1).piece().getPieceKind());
        assertEquals(PieceColor.BLACK, imported.moves().get(1).color());

        assertEquals(new Position(5, 5), imported.moves().get(2).to());
        assertEquals(PieceKind.KNIGHT, imported.moves().get(2).piece().getPieceKind());
        assertEquals(PieceColor.WHITE, imported.moves().get(2).color());

        assertEquals(new Position(2, 2), imported.moves().get(3).to());
        assertEquals(PieceKind.KNIGHT, imported.moves().get(3).piece().getPieceKind());
        assertEquals(PieceColor.BLACK, imported.moves().get(3).color());
    }
}
