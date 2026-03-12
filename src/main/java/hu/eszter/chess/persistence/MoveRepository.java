package hu.eszter.chess.persistence;

import hu.eszter.chess.domain.*;
import hu.eszter.chess.util.SquareNotation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {

    public void save(Long gameId, int moveIndex, Move move) throws SQLException {
        Piece piece = move.piece();
        PieceColor color = move.color();
        Position from = move.from();
        Position to = move.to();

        String fromSquare = SquareNotation.toSquare(from);
        String toSquare = SquareNotation.toSquare(to);

        String sql = """
                INSERT INTO moves (game_id, move_index, from_square, to_square, piece, piece_color)
                VALUES(?,?,?,?,?,?);""";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, gameId);
            ps.setInt(2, moveIndex);
            ps.setString(3, fromSquare);
            ps.setString(4, toSquare);
            ps.setString(5, piece.getPieceKind().name());
            ps.setString(6, color.name());

            ps.executeUpdate();
        }
    }

    public List<Move> findByGameId(Long gameId) throws SQLException {

        String sql = """
                SELECT from_square, to_square, piece, piece_color
                FROM moves
                WHERE game_id = ?
                ORDER BY move_index;""";

        List<Move> moves = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, gameId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    moves.add(mapRowToMove(rs));
                }
                return moves;
            }
        }
    }

    private Move mapRowToMove(ResultSet rs) throws SQLException {

        String fromSquare = rs.getString("from_square");
        String toSquare = rs.getString("to_square");

        Position from = SquareNotation.fromSquare(fromSquare);
        Position to = SquareNotation.fromSquare(toSquare);

        PieceColor color = PieceColor.valueOf(rs.getString("piece_color"));
        PieceKind kind = PieceKind.valueOf(rs.getString("piece"));

        Piece piece = PieceFactory.createPiece(kind, color, from);

        return new Move(piece, color, from, to);
    }
}
