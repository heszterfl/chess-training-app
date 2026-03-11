package hu.eszter.chess.ui;

import hu.eszter.chess.app.GameService;
import hu.eszter.chess.persistence.Database;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ChessWindow extends JFrame {

    private final BoardPanel boardPanel;
    private final GameService gameService;
    private final JTextArea moveLogArea;

    public ChessWindow(boolean customSetup) {
        gameService = new GameService();

        setTitle("Chess Training App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        moveLogArea = new JTextArea(20, 20);
        moveLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLogArea);

        boardPanel = new BoardPanel(gameService, moveLogArea, customSetup);

        PiecePalettePanel palettePanel = new PiecePalettePanel(
                boardPanel::setSelectedPalettePiece
        );

        add(palettePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (customSetup) {
            JButton startGameButton = new JButton("Start game");
            startGameButton.addActionListener(e -> boardPanel.exitSetupMode());
            buttonPanel.add(startGameButton);
        }

        JButton newGameButton = new JButton("New game");
        newGameButton.addActionListener(e -> {
            gameService.newGame();
            moveLogArea.setText("");
            boardPanel.gameReset();
        });
        buttonPanel.add(newGameButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        try {
            Database.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Hogyan szeretnéd kezdeni?",
                            "Kezdés módja",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Normál felállás", "Custom board"},
                    "Normál felállás"
            );

            boolean customSetup = (choice == 1);
            ChessWindow window = new ChessWindow(customSetup);
            window.setVisible(true);
        });
    }
}
