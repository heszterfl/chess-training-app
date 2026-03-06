package hu.eszter.chess.ui;

import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.PieceKind;

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

        addPieceButton("K", PieceColor.WHITE, PieceKind.KING);
        addPieceButton("Q", PieceColor.WHITE, PieceKind.QUEEN);
        addPieceButton("R", PieceColor.WHITE, PieceKind.ROOK);
        addPieceButton("B", PieceColor.WHITE, PieceKind.BISHOP);
        addPieceButton("N", PieceColor.WHITE, PieceKind.KNIGHT);
        addPieceButton("P", PieceColor.WHITE, PieceKind.PAWN);

        add(new JLabel("Black:"));
        add(new JLabel(""));

        addPieceButton("K", PieceColor.BLACK, PieceKind.KING);
        addPieceButton("Q", PieceColor.BLACK, PieceKind.QUEEN);
        addPieceButton("R", PieceColor.BLACK, PieceKind.ROOK);
        addPieceButton("B", PieceColor.BLACK, PieceKind.BISHOP);
        addPieceButton("N", PieceColor.BLACK, PieceKind.KNIGHT);
        addPieceButton("P", PieceColor.BLACK, PieceKind.PAWN);

        // Törlés (radír) gomb
        JButton clearBtn = new JButton("Eraser");
        clearBtn.addActionListener(e -> selectionListener.accept(null));
        add(new JLabel("")); // üres helyre
        add(clearBtn);
    }

    private void addPieceButton(String label, PieceColor color, PieceKind kind) {
        JButton btn = new JButton(label + " (" + (color == PieceColor.WHITE ? "w" : "b") + ")");
        btn.addActionListener(e ->
                selectionListener.accept(new PaletteSelection(color, kind))
        );
        add(btn);
    }
}
