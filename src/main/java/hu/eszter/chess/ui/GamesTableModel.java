package hu.eszter.chess.ui;

import hu.eszter.chess.domain.Game;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class GamesTableModel extends AbstractTableModel {

    List<Game> games;

    public GamesTableModel() {
        this.games = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (rowIndex < 0 || rowIndex >= games.size()) {
            return null;
        }

        Game game = games.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> game.getId();
            case 1 -> game.getWhitePlayer();
            case 2 -> game.getBlackPlayer();
            case 3 -> game.getDate();
            case 4 -> game.getResult();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "ID";
            case 1 -> "White";
            case 2 -> "Black";
            case 3 -> "Date";
            case 4 -> "Result";
            default -> throw new IllegalArgumentException("Invalid column");
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Long.class;
            case 1, 2, 3, 4 -> String.class;
            default -> Object.class;
        };
    }

    public void setGames(List<Game> games) {
        this.games = (games != null) ? games : new ArrayList<>();
        fireTableDataChanged();
    }

    public Game getGameAt(int rowIndex) {
        return games.get(rowIndex);
    }
}
