package hu.eszter.chess.ui;

import hu.eszter.chess.Board;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    private Board board;
    private BoardPanel boardPanel;

    public ChessWindow() {
        this.board = new Board();
        this.boardPanel = new BoardPanel(board);

        setTitle("Chess Training App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(boardPanel);
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
