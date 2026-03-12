package hu.eszter.chess.persistence;

import hu.eszter.chess.domain.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameRepositoryTest {

    private GameRepository repository;

    @BeforeEach
    void setup() throws Exception {
        Database.initialize();
        clearTables();
        repository = new GameRepository();
    }

    @Test
    void save_sets_id_on_game() throws Exception {
        Game game = new Game();
        game.setWhitePlayer("Carlsen");
        game.setBlackPlayer("Nakamura");
        game.setResult("1-0");
        game.setDate("2026-03-12");

        assertNull(game.getId());

        repository.save(game);

        assertNotNull(game.getId());
    }

    @Test
    void save_throws_exception_if_game_already_exists() throws SQLException {
        Game game = new Game();
        game.setId(100L);

        assertThrows(IllegalStateException.class, () -> repository.save(game));
    }

    @Test
    void findById_returns_saved_game() throws SQLException {
        Game game = new Game();
        game.setWhitePlayer("Caruana");
        game.setBlackPlayer("So");
        game.setResult("1/2-1/2");
        game.setDate("2026-03-11");

        repository.save(game);

        Game queried = repository.findById(game.getId());

        assertNotNull(queried);
        assertNotNull(queried.getId());
        assertEquals(game.getId(), queried.getId());
        assertEquals("Caruana", queried.getWhitePlayer());
        assertEquals("So", queried.getBlackPlayer());
        assertEquals("1/2-1/2", queried.getResult());
        assertEquals("2026-03-11", queried.getDate());
    }

    @Test
    void findById_returns_null_if_game_does_not_exist() throws SQLException {
        Game queried = repository.findById(123L);

        assertNull(queried);
    }

    @Test
    void findAll_returns_all_games_ordered_by_id() throws SQLException {
        Game game1 = new Game();
        game1.setWhitePlayer("Carlsen");
        game1.setBlackPlayer("Nakamura");
        game1.setResult("1-0");
        game1.setDate("2026-03-12");

        Game game2 = new Game();
        game2.setWhitePlayer("Caruana");
        game2.setBlackPlayer("So");
        game2.setResult("1/2-1/2");
        game2.setDate("2026-03-11");

        repository.save(game1);
        repository.save(game2);

        List<Game> queried = repository.findAll();

        assertEquals(2, queried.size());
        assertEquals(game1.getId(), queried.get(0).getId());
        assertEquals(game2.getId(), queried.get(1).getId());
        assertEquals("Carlsen", queried.get(0).getWhitePlayer());
        assertEquals("Nakamura", queried.get(0).getBlackPlayer());
        assertEquals("1-0", queried.get(0).getResult());
        assertEquals("2026-03-12", queried.get(0).getDate());
        assertEquals("Caruana", queried.get(1).getWhitePlayer());
        assertEquals("So", queried.get(1).getBlackPlayer());
        assertEquals("1/2-1/2", queried.get(1).getResult());
        assertEquals("2026-03-11", queried.get(1).getDate());
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
