package hu.eszter.chess.app;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.Move;
import hu.eszter.chess.domain.Piece;
import hu.eszter.chess.domain.Position;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    private Board board;
    private List<Move> moveHistory;

    public GameService() {
        this.board = new Board();
        this.moveHistory = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public void newGame() {
        board = new Board();
        moveHistory = new ArrayList<>();
    }

    public boolean tryMove(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return false;
        }

        Move move = new Move(piece, piece.getColor(), from, to);
        boolean success = board.tryMove(move.from(), move.to());
        if (success) {
            moveHistory.add(move);
        }
        return success;
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
}
