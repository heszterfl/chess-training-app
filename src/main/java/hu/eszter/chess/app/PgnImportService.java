package hu.eszter.chess.app;

import hu.eszter.chess.domain.Game;
import hu.eszter.chess.pgn.ImportedGame;
import hu.eszter.chess.pgn.PgnGame;
import hu.eszter.chess.pgn.PgnGameConverter;
import hu.eszter.chess.pgn.PgnParser;

import java.sql.SQLException;

public class PgnImportService {

    private final PgnParser parser;
    private final PgnGameConverter converter;
    private final GamePersistenceService gamePersistenceService;

    public PgnImportService() {
        this.parser = new PgnParser();
        this.converter = new PgnGameConverter();
        this.gamePersistenceService = new GamePersistenceService();
    }

    public Game importPgn(String pgnText) throws SQLException {

        if (pgnText == null || pgnText.isBlank()) {
            throw new IllegalArgumentException("PGN text must not be null or blank");
        }

        PgnGame pgnGame = parser.parse(pgnText);

        ImportedGame imported = converter.convert(pgnGame);

        return gamePersistenceService.saveGame(imported.game(), imported.moves());
    }
}
