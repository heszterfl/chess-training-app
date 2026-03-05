package hu.eszter.chess.ui;

import hu.eszter.chess.Board;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    private final BoardPanel boardPanel;

    public ChessWindow(boolean customSetup) {
        Board board = new Board();

        setTitle("Chess Training App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea moveLogArea = new JTextArea(20, 20);
        moveLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLogArea);

        boardPanel = new BoardPanel(board, moveLogArea, customSetup);

        PiecePalettePanel palettePanel = new PiecePalettePanel(
                boardPanel::setSelectedPalettePiece
        );

        add(palettePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);

        if (customSetup) {
            JButton startGameButton = new JButton("Start game");
            startGameButton.addActionListener(e -> boardPanel.exitSetupMode());
            add(startGameButton, BorderLayout.SOUTH);
        }

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

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
