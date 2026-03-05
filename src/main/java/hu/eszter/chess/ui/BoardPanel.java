package hu.eszter.chess.ui;

import hu.eszter.chess.app.GameService;
import hu.eszter.chess.domain.*;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private final GameService gameService;
    private Board board;
    private final JButton[][] buttons = new JButton[8][8];
    private final JTextArea moveLogArea;

    private Position selectedSquare = null;

    private final Color lightColor = new Color(240, 217, 181);
    private final Color darkColor = new Color(181, 136, 99);
    private final Color selectedColor = new Color(189, 214, 88);

    private boolean setupMode;

    private PaletteSelection selectedPalettePiece;

    public BoardPanel(GameService gameService, JTextArea moveLogArea, boolean setupMode) {
        this.gameService = gameService;
        this.board = gameService.getBoard();
        this.moveLogArea = moveLogArea;
        this.setupMode = setupMode;

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
        if (setupMode) {
            handleSetupClick(row, col);
        } else {
            handlePlayClick(row, col);
        }
    }

    private void handleSetupClick(int row, int col) {
        Piece[][] array = board.getBoard();
        Position pos = new Position(row, col);

        if (selectedPalettePiece == null) {
            array[row][col] = null;
        } else {
            Piece newPiece = PieceFactory.createPiece(
                    selectedPalettePiece.kind(),
                    selectedPalettePiece.color(),
                    pos
            );
            array[row][col] = newPiece;
            newPiece.setCurrentPosition(pos);
        }
        refreshBoard();
    }

    private void handlePlayClick(int row, int col) {

        if (selectedSquare == null) {
            Piece[][] boardArray = board.getBoard();
            Piece piece = boardArray[row][col];

            if (piece == null) {
                return;
            }

            if (board.isWhiteToMove() && piece.getColor().equalsIgnoreCase("white")) {
                selectedSquare = new Position(row, col);
                highlightSelectedSquare();
            } else if (!board.isWhiteToMove() && piece.getColor().equalsIgnoreCase("black")) {
                selectedSquare = new Position(row, col);
                highlightSelectedSquare();
            }

        } else {
            Position from = new Position(selectedSquare.row(), selectedSquare.col());
            Position to = new Position(row, col);

            boolean moved = gameService.tryMove(from, to);
            if (moved) {
                logMove(from, to);
                selectedSquare = null;
                resetSquareColors();
                refreshBoard();
            }
        }
    }

    private void logMove(Position from, Position to) {
        if (moveLogArea == null) return;

        String fromStr = board.convertSquareToString(from);
        String toStr = board.convertSquareToString(to);

        moveLogArea.append(fromStr + " -> " + toStr + "\n");
    }

    private void highlightSelectedSquare() {
        resetSquareColors();

        if (selectedSquare != null) {
            int row = selectedSquare.row();
            int col = selectedSquare.col();
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

    public void exitSetupMode() {
        this.setupMode = false;
        this.selectedSquare = null;
        resetSquareColors();
        refreshBoard();
        JOptionPane.showMessageDialog(this, "Setup kész. Mostantól a kattintás lépésnek számít.");
    }

    private String shortenPieceString(String s) {
        if (s.length() > 2) {
            return s.substring(0, 2);
        }
        return s;
    }

    public void setSelectedPalettePiece(PaletteSelection selection) {
        this.selectedPalettePiece = selection;
        this.selectedSquare = null;
        resetSquareColors();
    }
}
