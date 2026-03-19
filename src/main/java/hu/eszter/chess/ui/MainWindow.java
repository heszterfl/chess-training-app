package hu.eszter.chess.ui;

import hu.eszter.chess.app.GamePersistenceService;
import hu.eszter.chess.domain.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MainWindow extends JFrame {

    private final GamePersistenceService gamePersistenceService;
    private final GamesTableModel gamesTableModel;
    private final JTable gamesTable;

    public MainWindow() {
        this.gamePersistenceService = new GamePersistenceService();

        setTitle("Chess Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.gamesTableModel = new GamesTableModel();
        this.gamesTable = new JTable(gamesTableModel);

        gamesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = gamesTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        Game selectedGame = gamesTableModel.getGameAt(selectedRow);

                        GameViewerWindow viewer = new GameViewerWindow(selectedGame);
                        viewer.setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gamesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton importButton = new JButton("Import PGN");
        topPanel.add(importButton);
        add(topPanel, BorderLayout.NORTH);

        loadGames();

        pack();
        setLocationRelativeTo(null);
    }

    private void loadGames() {
        try {
            List<Game> games = gamePersistenceService.loadAllGames();
            gamesTableModel.setGames(games);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hiba történt a játszmák betöltésekor:\n" + e.getMessage(),
                    "Adatbázis hiba",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
