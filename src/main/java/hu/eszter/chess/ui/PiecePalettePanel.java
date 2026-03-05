package hu.eszter.chess.ui;

import hu.eszter.chess.PieceKind;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PiecePalettePanel extends JPanel {

    private final Consumer<PaletteSelection> selectionListener;

    public PiecePalettePanel(Consumer<PaletteSelection> selectionListener) {
        this.selectionListener = selectionListener;
        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("White:"));
        add(new JLabel(""));

        addPieceButton("K", "white", PieceKind.KING);
        addPieceButton("Q", "white", PieceKind.QUEEN);
        addPieceButton("R", "white", PieceKind.ROOK);
        addPieceButton("B", "white", PieceKind.BISHOP);
        addPieceButton("N", "white", PieceKind.KNIGHT);
        addPieceButton("P", "white", PieceKind.PAWN);

        add(new JLabel("Black:"));
        add(new JLabel(""));

        addPieceButton("K", "black", PieceKind.KING);
        addPieceButton("Q", "black", PieceKind.QUEEN);
        addPieceButton("R", "black", PieceKind.ROOK);
        addPieceButton("B", "black", PieceKind.BISHOP);
        addPieceButton("N", "black", PieceKind.KNIGHT);
        addPieceButton("P", "black", PieceKind.PAWN);

        // Törlés (radír) gomb
        JButton clearBtn = new JButton("Eraser");
        clearBtn.addActionListener(e -> selectionListener.accept(null));
        add(new JLabel("")); // üres helyre
        add(clearBtn);
    }

    private void addPieceButton(String label, String color, PieceKind kind) {
        JButton btn = new JButton(label + " (" + color.charAt(0) + ")");
        btn.addActionListener(e ->
                selectionListener.accept(new PaletteSelection(color, kind))
        );
        add(btn);
    }
}
