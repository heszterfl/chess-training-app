package hu.eszter.chess.app;

import hu.eszter.chess.domain.Game;
import hu.eszter.chess.domain.Move;
import hu.eszter.chess.persistence.GameRepository;
import hu.eszter.chess.persistence.MoveRepository;

import java.sql.SQLException;
import java.util.List;

public class GamePersistenceService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;

    public GamePersistenceService() {
        this.gameRepository = new GameRepository();
        this.moveRepository = new MoveRepository();
    }

    public void saveGame(Game game, List<Move> moves) throws SQLException {

        if (game == null || moves == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        gameRepository.save(game);

        Long gameId = game.getId();

        int moveIndex = 0;
        for (Move move : moves) {
            moveRepository.save(gameId, moveIndex, move);
            moveIndex++;
        }
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }

    public MoveRepository getMoveRepository() {
        return moveRepository;
    }
}
