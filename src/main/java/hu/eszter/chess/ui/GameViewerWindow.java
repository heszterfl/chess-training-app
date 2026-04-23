package hu.eszter.chess.ui;

import hu.eszter.chess.app.GamePersistenceService;
import hu.eszter.chess.domain.*;
import hu.eszter.chess.pgn.SanMoveGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameViewerWindow extends JFrame {
    private final GamePersistenceService gamePersistenceService;
    private final SanMoveGenerator sanMoveGenerator;
    private final List<Move> moves;
    private int currentMoveIndex;
    private final ReplayBoardPanel replayBoardPanel;
    private final MovesTableModel movesTableModel;
    private final JTable movesTable;
    private int highlightRow;
    private int highlightCol;

    public GameViewerWindow(Game game) throws SQLException {
        this.gamePersistenceService = new GamePersistenceService();
        this.replayBoardPanel = new ReplayBoardPanel();
        this.sanMoveGenerator = new SanMoveGenerator();

        setTitle("Game Viewer - " + game.getId());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(400, 400));
        boardPanel.add(replayBoardPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(4, 1));
        headerPanel.add(new JLabel("White: " + game.getWhitePlayer()));
        headerPanel.add(new JLabel("Black: " + game.getBlackPlayer()));
        headerPanel.add(new JLabel("Date: " + game.getDate()));
        headerPanel.add(new JLabel("Result: " + game.getResult()));

        this.moves = gamePersistenceService.loadMovesForGame(game.getId());
        this.currentMoveIndex = 0;

        this.movesTableModel = new MovesTableModel();
        this.movesTable = new JTable(movesTableModel);

        MoveHighlightRenderer renderer = new MoveHighlightRenderer();

        movesTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
        movesTable.getColumnModel().getColumn(2).setCellRenderer(renderer);

        List<MoveRow> rows = movesToRows(moves);
        movesTableModel.setRows(rows);

        JScrollPane scroll = new JScrollPane(movesTable);

        JPanel controlPanel = new JPanel();

        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        prevButton.addActionListener(e -> {
            if (currentMoveIndex > 0) {
                currentMoveIndex--;
                calculateHighlight();
                Board board = updateReplayBoard();
                replayBoardPanel.setBoard(board);
                movesTable.repaint();
            }
        });
        nextButton.addActionListener(e -> {
            if (currentMoveIndex < moves.size()) {
                currentMoveIndex++;
                calculateHighlight();
                Board board = updateReplayBoard();
                replayBoardPanel.setBoard(board);
                movesTable.repaint();
            }
        });

        controlPanel.add(prevButton);
        controlPanel.add(nextButton);

        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        calculateHighlight();
        Board board = updateReplayBoard();
        replayBoardPanel.setBoard(board);
    }


    private Board updateReplayBoard() {
        Board board = new Board();

        for (int i = 0; i < currentMoveIndex; i++) {
            Position currentPos = moves.get(i).from();
            Position newPos = moves.get(i).to();
            Piece piece = moves.get(i).piece();
            board.applyMove(piece, currentPos, newPos);
        }
        return board;
    }

    private List<MoveRow> movesToRows(List<Move> moves) {
        List<MoveRow> moveRows = new ArrayList<>();
        Board board = new Board();

        for (int i = 0; i < moves.size(); i += 2) {
            int moveNumber = i / 2 + 1;

            Move whiteMove = moves.get(i);
            String whiteSan = sanMoveGenerator.toSan(board, whiteMove);
            Piece piece = board.getPieceAt(whiteMove.from());
            board.applyMove(piece, whiteMove.from(), whiteMove.to());

            Move blackMove = (i+1 >= moves.size()) ? null : moves.get(i+1);
            String blackSan;
            if (blackMove == null) {
                blackSan = "";
            } else {
                blackSan = sanMoveGenerator.toSan(board, blackMove);
                piece = board.getPieceAt(blackMove.from());
                board.applyMove(piece, blackMove.from(), blackMove.to());
            }

            MoveRow moveRow = new MoveRow(moveNumber, whiteSan, blackSan);
            moveRows.add(moveRow);
        }

        return moveRows;
    }

    private void calculateHighlight() {
        if (currentMoveIndex == 0) {
            highlightRow = -1;
            highlightCol = -1;
        } else {
            int highLightMoveIndex = currentMoveIndex - 1;
            highlightRow = highLightMoveIndex / 2;
            highlightCol = (highLightMoveIndex % 2 == 0) ? 1 : 2;
        }
    }

    private class MoveHighlightRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Font baseFont = component.getFont();

            if (row == highlightRow && column == highlightCol) {
                component.setBackground(new Color(210, 210, 210));
                component.setFont(baseFont.deriveFont(Font.BOLD));
            } else {
                component.setBackground(Color.WHITE);
                component.setFont(baseFont.deriveFont(Font.PLAIN));
            }

            return component;
        }
    }
}
