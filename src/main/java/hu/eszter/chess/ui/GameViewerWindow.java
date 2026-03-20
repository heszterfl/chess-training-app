package hu.eszter.chess.ui;

import hu.eszter.chess.app.GamePersistenceService;
import hu.eszter.chess.domain.*;
import hu.eszter.chess.util.SquareNotation;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GameViewerWindow extends JFrame {
    private final GamePersistenceService gamePersistenceService;
    private final List<Move> moves;
    private int currentMoveIndex;
    private ReplayBoardPanel replayBoardPanel;

    public GameViewerWindow(Game game) throws SQLException {
        this.gamePersistenceService = new GamePersistenceService();
        this.replayBoardPanel = new ReplayBoardPanel();

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

        JTextArea moveList = new JTextArea();
        moveList.setEditable(false);

        this.moves = gamePersistenceService.loadMovesForGame(game.getId());
        this.currentMoveIndex = 0;
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

        JPanel controlPanel = new JPanel();

        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        prevButton.addActionListener(e -> {
            if (currentMoveIndex > 0) {
                currentMoveIndex--;
                System.out.println("Index: " + currentMoveIndex);
                Board board = updateReplayBoard();
                replayBoardPanel.setBoard(board);
            }
        });
        nextButton.addActionListener(e -> {
            if (currentMoveIndex < moves.size()) {
                currentMoveIndex++;
                System.out.println("Index: " + currentMoveIndex);
                Board board = updateReplayBoard();
                replayBoardPanel.setBoard(board);
            }
        });

        controlPanel.add(prevButton);
        controlPanel.add(nextButton);

        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

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
}
