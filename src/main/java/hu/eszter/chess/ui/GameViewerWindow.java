package hu.eszter.chess.ui;

import hu.eszter.chess.app.GamePersistenceService;
import hu.eszter.chess.domain.Game;
import hu.eszter.chess.domain.Move;
import hu.eszter.chess.util.SquareNotation;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GameViewerWindow extends JFrame {
    private final GamePersistenceService gamePersistenceService;

    public GameViewerWindow(Game game) throws SQLException {
        this.gamePersistenceService = new GamePersistenceService();

        setTitle("Game Viewer - " + game.getId());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(400, 400));
        boardPanel.add(new JLabel("Board here"));

        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(4, 1));
        headerPanel.add(new JLabel("White: " + game.getWhitePlayer()));
        headerPanel.add(new JLabel("Black: " + game.getBlackPlayer()));
        headerPanel.add(new JLabel("Date: " + game.getDate()));
        headerPanel.add(new JLabel("Result: " + game.getResult()));

        JTextArea moveList = new JTextArea();
        moveList.setEditable(false);

        List<Move> moves = gamePersistenceService.loadMovesForGame(game.getId());
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(move.color())
                    .append(": ")
                    .append(SquareNotation.toSquare(move.from()))
                    .append(" -> ")
                    .append(SquareNotation.toSquare(move.to()))
                    .append("\n");
        }
        String moveString = sb.toString();
        moveList.setText(moveString);

        JScrollPane scroll = new JScrollPane(moveList);

        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);

        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
}
