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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GamePersistenceServiceTest {

    @BeforeEach
    void setup() throws Exception {
        Database.initialize();
        clearTables();
    }

    @Test
    void saveGame_persists_game_and_moves() throws SQLException {
        GamePersistenceService service = new GamePersistenceService();

        Game game = new Game();
        game.setWhitePlayer("Carlsen");
        game.setBlackPlayer("Nakamura");
        game.setResult("1-0");
        game.setDate("2026-03-12");

        Move move1 = new Move(new Pawn(PieceColor.WHITE), PieceColor.WHITE, new Position(6, 4), new Position(4, 4));
        Move move2 = new Move(new Pawn(PieceColor.BLACK), PieceColor.BLACK, new Position(1, 4), new Position(3, 4));
        Move move3 = new Move(new Pawn(PieceColor.WHITE), PieceColor.WHITE, new Position(6, 0), new Position(5, 0));

        List<Move> moves = List.of(move1, move2, move3);

        service.saveGame(game, moves);

        GameRepository gameRepository = service.getGameRepository();
        MoveRepository moveRepository = service.getMoveRepository();

        List<Move> moveList = moveRepository.findByGameId(game.getId());
        Game queriedGame = gameRepository.findById(game.getId());

        assertNotNull(queriedGame);
        assertEquals("Carlsen", queriedGame.getWhitePlayer());
        assertEquals("Nakamura", queriedGame.getBlackPlayer());
        assertEquals("1-0", queriedGame.getResult());
        assertEquals("2026-03-12", queriedGame.getDate());

        assertEquals(3, moveList.size());
        assertEquals(move1.from(), moveList.get(0).from());
        assertEquals(move1.to(), moveList.get(0).to());
        assertEquals(move2.from(), moveList.get(1).from());
        assertEquals(move2.to(), moveList.get(1).to());
        assertEquals(move3.from(), moveList.get(2).from());
        assertEquals(move3.to(), moveList.get(2).to());
    }

    void clearTables() throws SQLException {

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
