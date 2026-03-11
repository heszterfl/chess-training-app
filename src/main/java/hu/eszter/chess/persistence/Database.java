package hu.eszter.chess.persistence;

import java.sql.*;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:chess.db";

    public Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initialize() throws SQLException {

        System.out.println("Initializing database...");

        String createGamesTable = """
                CREATE TABLE IF NOT EXISTS games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    white_player TEXT,
                    black_player TEXT,
                    result TEXT,
                    date TEXT
                );
                """;

        String createMovesTable = """
                CREATE TABLE IF NOT EXISTS moves (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    game_id INTEGER,
                    move_index INTEGER,
                    from_square TEXT,
                    to_square TEXT,
                    piece TEXT,
                    piece_color TEXT,
                    CONSTRAINT fk_game
                        FOREIGN KEY (game_id)
                        REFERENCES games(id)
                );
                """;

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(createGamesTable);
            statement.execute(createMovesTable);
        }
    }
}
