package hu.eszter.chess.ui;

import hu.eszter.chess.app.GamePersistenceService;
import hu.eszter.chess.app.PgnImportService;
import hu.eszter.chess.domain.Game;
import hu.eszter.chess.persistence.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainWindow extends JFrame {

    private final GamePersistenceService gamePersistenceService;
    private final GamesTableModel gamesTableModel;
    private final JTable gamesTable;
    private final PgnImportService pgnImportService;

    public MainWindow() {
        this.pgnImportService = new PgnImportService();
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

                        try {
                            GameViewerWindow viewer = new GameViewerWindow(selectedGame);
                            viewer.setVisible(true);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(
                                    MainWindow.this,
                                    "Hiba történt a játszma betöltésekor:\n" + ex.getMessage(),
                                    "Hiba",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gamesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton importButton = new JButton("Import PGN");
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
                try {
                    pgnImportService.importPgn(selectedFile);
                    loadGames();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Hiba történt a fájl betöltésekor:\n" + ex.getMessage(),
                            "Fájl hiba",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Hiba történt a játszma importálásakor:\n" + ex.getMessage(),
                            "Adatbázis hiba",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
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

    public static void main(String[] args) {

        try {
            Database.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
