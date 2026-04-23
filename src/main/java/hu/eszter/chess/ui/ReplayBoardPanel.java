package hu.eszter.chess.ui;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.Piece;
import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.Position;

import javax.swing.*;
import java.awt.*;

public class ReplayBoardPanel extends JPanel {

    private Board board;

    private final JButton[][] buttons = new JButton[8][8];

    private final Color lightColor = new Color(240, 217, 181);
    private final Color darkColor = new Color(181, 136, 99);
    private final Color checkColor = new Color(229, 65, 65);

    public ReplayBoardPanel() {
        setLayout(new GridLayout(8, 8));

        initBoardButtons();
    }

    public void setBoard(Board board) {
        this.board = board;
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

                buttons[row][col] = button;
                add(button);
            }
        }
    }

    public void refreshBoard() {
        if (board == null) return;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(new Position(row, col));
                JButton button = buttons[row][col];

                if (piece == null) {
                    button.setText("");
                } else {
                    button.setText(shortenPieceString(piece.toString()));
                }

                if ((row + col) % 2 == 0) {
                    button.setBackground(lightColor);
                } else {
                    button.setBackground(darkColor);
                }
            }
        }

        if (board.isInCheck(PieceColor.WHITE)) {
            Position kingPos = board.getKingPosition(PieceColor.WHITE);
            int row = kingPos.row();
            int col = kingPos.col();
            buttons[row][col].setBackground(checkColor);
        }

        if (board.isInCheck(PieceColor.BLACK)) {
            Position kingPos = board.getKingPosition(PieceColor.BLACK);
            int row = kingPos.row();
            int col = kingPos.col();
            buttons[row][col].setBackground(checkColor);
        }

        repaint();
    }

    private String shortenPieceString(String s) {
        if (s.length() > 2) {
            return s.substring(0, 2);
        }
        return s;
    }
}
