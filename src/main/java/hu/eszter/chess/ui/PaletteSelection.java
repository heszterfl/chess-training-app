package hu.eszter.chess.ui;

import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.PieceKind;

public record PaletteSelection(PieceColor color, PieceKind kind) {
    // color: "white" or "black"
}
