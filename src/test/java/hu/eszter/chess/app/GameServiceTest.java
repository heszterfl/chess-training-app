package hu.eszter.chess.app;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.Pawn;
import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    void gameService_initialisation_success() {
        GameService gameService = new GameService();

        assertNotNull(gameService.getBoard());
        assertTrue(gameService.getMoveHistory().isEmpty());
    }

    @Test
    void newGame_resets_state_correctly() {
        GameService gameService = new GameService();
        Board b1 = gameService.getBoard();
        gameService.tryMove(new Position(6, 4), new Position(4, 4));

        assertFalse(gameService.getMoveHistory().isEmpty());

        gameService.newGame();
        Board b2 = gameService.getBoard();

        assertTrue(gameService.getMoveHistory().isEmpty());
        assertNotSame(b1, b2);
    }

    @Test
    void legal_move_saved_in_move_history() {
        GameService gameService = new GameService();
        boolean isLegal = gameService.tryMove(new Position(6, 4), new Position(4, 4));

        assertTrue(isLegal);
        assertEquals(1, gameService.getMoveHistory().size());
    }

    @Test
    void illegal_move_not_saved_in_move_history() {
        GameService gameService = new GameService();
        boolean isNotLegal = gameService.tryMove(new Position(7, 4), new Position(6, 4));

        assertFalse(isNotLegal);
        assertEquals(0, gameService.getMoveHistory().size());
    }

    @Test
    void moves_saved_in_order_in_move_history() {
        GameService gameService = new GameService();
        gameService.tryMove(new Position(6, 4), new Position(4, 4));
        gameService.tryMove(new Position(1, 4), new Position(3, 4));

        assertEquals(2, gameService.getMoveHistory().size());
        assertInstanceOf(Pawn.class, gameService.getMoveHistory().get(1).piece());
        assertSame(PieceColor.BLACK, gameService.getMoveHistory().get(1).color());
        assertEquals(new Position(1, 4), gameService.getMoveHistory().get(1).from());
        assertEquals(new Position(3, 4), gameService.getMoveHistory().get(1).to());
    }
}
