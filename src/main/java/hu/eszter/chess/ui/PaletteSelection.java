package hu.eszter.chess.ui;

import hu.eszter.chess.domain.PieceKind;

public record PaletteSelection(String color, PieceKind kind) {
    // color: "white" or "black"
}
