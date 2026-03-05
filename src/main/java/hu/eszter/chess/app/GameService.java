package hu.eszter.chess.app;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.Move;
import hu.eszter.chess.domain.Piece;
import hu.eszter.chess.domain.Position;

public class GameService {

    private final Board board;

    public GameService() {
        this.board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public boolean tryMove(Position from, Position to) {
        Piece piece = board.getPieceAt(board.getBoard(), from);
        if (piece == null) {
            return false;
        }

        Move move = new Move(piece, piece.getColor(), from, to);
        return board.tryMove(move.from(), move.to());
    }
}
