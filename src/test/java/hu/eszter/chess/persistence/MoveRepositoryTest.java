package hu.eszter.chess.persistence;

import hu.eszter.chess.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveRepositoryTest {

    private MoveRepository moveRepository;
    private GameRepository gameRepository;

    @BeforeEach
    void setup() throws Exception {
        Database.initialize();
        clearTables();
        moveRepository = new MoveRepository();
        gameRepository = new GameRepository();
    }

    @Test
    void save_persists_move() throws SQLException {
        Game game = new Game();
        game.setWhitePlayer("Carlsen");
        game.setBlackPlayer("Nakamura");
        game.setResult("1-0");
        game.setDate("2026-03-12");

        gameRepository.save(game);

        Move move = new Move(new Pawn(PieceColor.WHITE), PieceColor.WHITE, new Position(6, 4), new Position(4, 4));
        moveRepository.save(game.getId(), 1, move);
        List<Move> moves = moveRepository.findByGameId(game.getId());

        assertEquals(1, moves.size());
        assertEquals(new Position(6, 4), moves.get(0).from());
        assertEquals(new Position(4, 4), moves.get(0).to());
        assertEquals(PieceColor.WHITE, moves.get(0).color());
        assertEquals(PieceKind.PAWN, moves.get(0).piece().getPieceKind());
    }

    @Test
    void findByGameId_returns_moves_in_order() throws SQLException {
        Game game = new Game();
        game.setWhitePlayer("Carlsen");
        game.setBlackPlayer("Nakamura");
        game.setResult("1-0");
        game.setDate("2026-03-12");

        gameRepository.save(game);

        Move move1 = new Move(new Pawn(PieceColor.WHITE), PieceColor.WHITE, new Position(6, 4), new Position(4, 4));
        Move move2 = new Move(new Pawn(PieceColor.BLACK), PieceColor.BLACK, new Position(1, 4), new Position(3, 4));
        Move move3 = new Move(new Pawn(PieceColor.WHITE), PieceColor.WHITE, new Position(6, 0), new Position(5, 0));
        moveRepository.save(game.getId(), 3, move3);
        moveRepository.save(game.getId(), 1, move1);
        moveRepository.save(game.getId(), 2, move2);
        List<Move> moves = moveRepository.findByGameId(game.getId());

        assertEquals(3, moves.size());
        assertEquals(move1.from(), moves.get(0).from());
        assertEquals(move1.to(), moves.get(0).to());
        assertEquals(move2.from(), moves.get(1).from());
        assertEquals(move2.to(), moves.get(1).to());
        assertEquals(move3.from(), moves.get(2).from());
        assertEquals(move3.to(), moves.get(2).to());
    }

    @Test
    void findByGameId_returns_empty_list_if_game_has_no_moves() throws SQLException {
        Game game = new Game();
        game.setWhitePlayer("Carlsen");
        game.setBlackPlayer("Nakamura");
        game.setResult("1-0");
        game.setDate("2026-03-12");

        gameRepository.save(game);

        List<Move> moves = moveRepository.findByGameId(game.getId());

        assertTrue(moves.isEmpty());
    }

    @Test
    void findByGameId_returns_empty_list_if_game_does_not_exist() throws SQLException {
        Long gameId = 1000L;

        List<Move> moves = moveRepository.findByGameId(gameId);

        assertTrue(moves.isEmpty());
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
