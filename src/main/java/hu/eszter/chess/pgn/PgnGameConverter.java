package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.Game;
import hu.eszter.chess.domain.Move;

import java.util.ArrayList;
import java.util.List;

public class PgnGameConverter {

    private final SanMoveResolver sanMoveResolver;

    public PgnGameConverter() {
        this.sanMoveResolver = new SanMoveResolver();
    }

    public ImportedGame convert(PgnGame pgnGame) {

        if (pgnGame == null) {
            throw new IllegalArgumentException("PgnGame is null");
        }

        Game game = new Game();
        game.setWhitePlayer(pgnGame.white());
        game.setBlackPlayer(pgnGame.black());
        game.setDate(pgnGame.date());
        game.setResult(pgnGame.result());

        Board board = new Board();

        List<Move> moveList = new ArrayList<>();

        if (pgnGame.moveTokens() == null || pgnGame.moveTokens().isEmpty()) {
            throw new IllegalArgumentException("PGN game has no moves");
        }

        for (String token : pgnGame.moveTokens()) {
            Move move = sanMoveResolver.resolve(token, board);

            boolean success = board.tryMove(move.from(), move.to());

            if (!success) {
                throw new IllegalStateException("Invalid move resolved from token: " + token);
            }

            moveList.add(move);
        }

        return new ImportedGame(game, moveList);
    }
}
