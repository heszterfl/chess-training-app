package hu.eszter.chess.pgn;

import java.util.List;

public record PgnGame(String event, String site, String date,
                      String white, String black, String result, List<String> moveTokens) {
}
