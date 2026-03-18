package hu.eszter.chess.app;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.persistence.Database;
import hu.eszter.chess.persistence.GameRepository;
import hu.eszter.chess.persistence.MoveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PgnImportServiceTest {

    PgnImportService pgnImportService = new PgnImportService();
    MoveRepository moveRepository = new MoveRepository();
    GameRepository gameRepository = new GameRepository();

    @BeforeEach
    void setup() throws Exception {
        Database.initialize();
        clearTables();
    }

    @Test
    void importPgn_persists_game_and_moves_correctly() throws SQLException {

        String pgnText = """
                [White "Carlsen"]
                [Black "Nakamura"]
                [Date "2026-03-18"]
                [Result "1-0"]
                
                1. e4 e5 2. Nf3 Nc6""";

        Game game = pgnImportService.importPgn(pgnText);
        Game queried = gameRepository.findById(game.getId());

        assertNotNull(queried.getId());
        assertEquals("Carlsen", queried.getWhitePlayer());
        assertEquals("Nakamura", queried.getBlackPlayer());
        assertEquals("2026-03-18", queried.getDate());
        assertEquals("1-0", queried.getResult());

        List<Move> moves = moveRepository.findByGameId(game.getId());

        assertEquals(4, moves.size());
        assertEquals(new Position(6, 4), moves.get(0).from());
        assertEquals(new Position(4, 4), moves.get(0).to());
        assertEquals(PieceKind.PAWN, moves.get(0).piece().getPieceKind());
        assertEquals(PieceColor.WHITE, moves.get(0).color());

        assertEquals(new Position(1, 4), moves.get(1).from());
        assertEquals(new Position(3, 4), moves.get(1).to());
        assertEquals(PieceKind.PAWN, moves.get(1).piece().getPieceKind());
        assertEquals(PieceColor.BLACK, moves.get(1).color());

        assertEquals(new Position(7, 6), moves.get(2).from());
        assertEquals(new Position(5, 5), moves.get(2).to());
        assertEquals(PieceKind.KNIGHT, moves.get(2).piece().getPieceKind());
        assertEquals(PieceColor.WHITE, moves.get(2).color());

        assertEquals(new Position(0, 1), moves.get(3).from());
        assertEquals(new Position(2, 2), moves.get(3).to());
        assertEquals(PieceKind.KNIGHT, moves.get(3).piece().getPieceKind());
        assertEquals(PieceColor.BLACK, moves.get(3).color());
    }

    @Test
    void importPgn_throws_for_invalid_pgn() throws SQLException {

        String pgnText = """
                [White "Carlsen"]""";

        assertThrows(IllegalArgumentException.class, () -> pgnImportService.importPgn(pgnText));
    }

    private void clearTables() throws SQLException {

        String deleteAllMoves = "DELETE FROM moves";
        String deleteAllGames = "DELETE FROM games";

        try (Connection conn = Database.getConnection();
             PreparedStatement deleteMoves = conn.prepareStatement(deleteAllMoves);
             PreparedStatement deleteGames = conn.prepareStatement(deleteAllGames)) {

            deleteMoves.executeUpdate();
            deleteGames.executeUpdate();
        }
    }
}
