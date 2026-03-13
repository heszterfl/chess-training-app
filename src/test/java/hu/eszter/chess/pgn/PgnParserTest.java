package hu.eszter.chess.pgn;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PgnParserTest {

    @Test
    void parse_returns_pgn_game_for_valid_input() {

        String pgnString = """
                [Event "Casual Game"]
                [Site "Berlin GER"]
                [Date "1852.??.??"]
                [White "Adolf Anderssen"]
                [Black "Jean Dufresne"]
                [Result "1-0"]
                
                1. e4 e5 2. Nf3 Nc6 3. Bc4 Bc5 1-0
                """;

        PgnParser parser = new PgnParser();
        PgnGame pgnGame = parser.parse(pgnString);

        List<String> moves = pgnGame.moveTokens();

        assertEquals("Casual Game", pgnGame.event());
        assertEquals("Berlin GER", pgnGame.site());
        assertEquals("1852.??.??", pgnGame.date());
        assertEquals("Adolf Anderssen", pgnGame.white());
        assertEquals("Jean Dufresne", pgnGame.black());
        assertEquals("1-0", pgnGame.result());
        assertEquals(6, moves.size());
        assertEquals("e4", moves.get(0));
        assertEquals("e5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
        assertEquals("Nc6", moves.get(3));
        assertEquals("Bc4", moves.get(4));
        assertEquals("Bc5", moves.get(5));
    }

    @Test
    void parse_throws_for_blank_input() {
        String pgnString = "";

        PgnParser parser = new PgnParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(pgnString));
    }

    @Test
    void parse_throws_if_move_text_is_missing() {

        String pgnString = """
                [Event "Casual Game"]
                [Site "Berlin GER"]
                [Date "1852.??.??"]
                [White "Adolf Anderssen"]
                [Black "Jean Dufresne"]
                [Result "1-0"]
                
                """;

        PgnParser parser = new PgnParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(pgnString));
    }

    @Test
    void parse_ignores_missing_optional_headers() {

        String pgnString = """
                [White "Kasparov"]
                [Black "Karpov"]
                
                1. e4 e5 2. Nf3 Nc6
                """;

        PgnParser parser = new PgnParser();
        PgnGame pgnGame = parser.parse(pgnString);

        List<String> moves = pgnGame.moveTokens();

        assertEquals("Kasparov", pgnGame.white());
        assertEquals("Karpov", pgnGame.black());
        assertNull(pgnGame.event());
        assertNull(pgnGame.site());
        assertNull(pgnGame.date());
        assertNull(pgnGame.result());
        assertEquals(4, moves.size());
        assertEquals("e4", moves.get(0));
        assertEquals("e5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
        assertEquals("Nc6", moves.get(3));
    }
}
