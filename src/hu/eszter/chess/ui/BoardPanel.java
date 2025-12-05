package hu.eszter.chess.ui;

import hu.eszter.chess.Board;
import hu.eszter.chess.Piece;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private final Board board;
    private final JButton[][] buttons = new JButton[8][8];

    private int[] selectedSquare = null;

    private final Color lightColor = new Color(240, 217, 181);
    private final Color darkColor = new Color(181, 136, 99);
    private final Color selectedColor = new Color(189, 214, 88);

    public BoardPanel(Board board) {
        this.board = board;
        setLayout(new GridLayout(8, 8));

        initBoardButtons();
        refreshBoard();
    }

    private void initBoardButtons() {

        Font pieceFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setFont(pieceFont);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBorderPainted(false);

                if ((row + col) % 2 == 0) {
                    button.setBackground(lightColor);
                } else {
                    button.setBackground(darkColor);
                }

                final int r = row;
                final int c = col;
                button.addActionListener(e -> handleSquareClick(r, c));

                buttons[row][col] = button;
                add(button);
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if (selectedSquare == null) {
            Piece[][] boardArray = board.getBoard();
            Piece piece = boardArray[row][col];

            if (piece == null) {
                return;
            }

            selectedSquare = new int[]{row, col};
            highlightSelectedSquare();
        } else {
            int[] from = selectedSquare;
            int[] to = new int[]{row, col};

            board.setPieceAt(from, to);

            selectedSquare = null;
            resetSquareColors();
            refreshBoard();
        }
    }

    private void highlightSelectedSquare() {
        resetSquareColors();

        if (selectedSquare != null) {
            int row = selectedSquare[0];
            int col = selectedSquare[1];
            buttons[row][col].setBackground(selectedColor);
        }
    }

    private void resetSquareColors() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    buttons[row][col].setBackground(lightColor);
                } else {
                    buttons[row][col].setBackground(darkColor);
                }
            }
        }
    }

    public void refreshBoard() {
        Piece[][] boardArray = board.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = boardArray[row][col];
                JButton button = buttons[row][col];

                if (piece == null) {
                    button.setText("");
                } else {
                    button.setText(shortenPieceString(piece.toString()));
                }
            }
        }
        revalidate();
        repaint();
    }

    private String shortenPieceString(String s) {
        if (s.length() > 2) {
            return s.substring(0, 2);
        }
        return s;
    }
}
