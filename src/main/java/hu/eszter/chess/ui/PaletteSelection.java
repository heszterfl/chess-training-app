package hu.eszter.chess.ui;

import hu.eszter.chess.PieceKind;

public record PaletteSelection(String color, PieceKind kind) {
    // color: "white" or "black"
}
