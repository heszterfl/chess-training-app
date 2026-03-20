package hu.eszter.chess.ui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MovesTableModel extends AbstractTableModel {

    private List<MoveRow> rows;

    public MovesTableModel() {
        this.rows = new ArrayList<>();
    }

    public void setRows(List<MoveRow> rows) {
        this.rows = rows;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        MoveRow moveRow = rows.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> moveRow.moveNumber();
            case 1 -> moveRow.whiteMove();
            case 2 -> moveRow.blackMove();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "#";
            case 1 -> "White";
            case 2 -> "Black";
            default -> throw new IllegalArgumentException("Invalid column");
        };
    }
}
