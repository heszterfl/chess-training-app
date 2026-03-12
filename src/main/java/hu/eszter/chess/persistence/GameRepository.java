package hu.eszter.chess.persistence;

import hu.eszter.chess.domain.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    public void save(Game game) throws SQLException {

        if (game.getId() != null) {
            throw new IllegalStateException("Game already exists in database.");
        }

        String sql = """
                INSERT INTO games (white_player, black_player, result, date)
                VALUES(?,?,?,?)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, game.getWhitePlayer());
            ps.setString(2, game.getBlackPlayer());
            ps.setString(3, game.getResult());
            ps.setString(4, game.getDate());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    game.setId(rs.getLong(1));
                }
            }

        }
    }

    public Game findById(Long id) throws SQLException {

        String sql = """
                SELECT id, white_player, black_player, result, date
                FROM games
                WHERE id = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setLong(1, id);

             try (ResultSet rs = ps.executeQuery()) {
                 if (!rs.next()) {
                     return null;
                 }

                 return mapRowToGame(rs);
             }
        }
    }

    public List<Game> findAll() throws SQLException {

        String sql = """
                SELECT id, white_player, black_player, result, date
                FROM games
                ORDER BY id
                """;

        List<Game> gameList = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                gameList.add(mapRowToGame(rs));
            }
            return gameList;
        }
    }

    private Game mapRowToGame(ResultSet rs) throws SQLException {
        Game game = new Game();
        game.setId(rs.getLong("id"));
        game.setWhitePlayer(rs.getString("white_player"));
        game.setBlackPlayer(rs.getString("black_player"));
        game.setResult(rs.getString("result"));
        game.setDate(rs.getString("date"));
        return game;
    }
}
