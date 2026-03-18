package hu.eszter.chess.pgn;

import hu.eszter.chess.domain.Game;
import hu.eszter.chess.domain.Move;

import java.util.List;

public record ImportedGame(Game game, List<Move> moves) {
}
