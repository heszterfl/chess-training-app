package hu.eszter.chess.ui;

import hu.eszter.chess.Board;

import javax.swing.*;
import java.awt.*;

public class ChessWindow extends JFrame {

    private Board board;

    public ChessWindow() {
        this.board = new Board();

        setTitle("Chess Training App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
