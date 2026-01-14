package hu.eszter.chess.ui;

import hu.eszter.chess.Board;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    private final BoardPanel boardPanel;

    public ChessWindow() {
        Board board = new Board();

        setTitle("Chess Training App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea moveLogArea = new JTextArea(20, 20);
        moveLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLogArea);

        boardPanel = new BoardPanel(board, moveLogArea);

        add(boardPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ChessWindow window = new ChessWindow();
            window.setVisible(true);
        });
    }
}
