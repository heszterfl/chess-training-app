package hu.eszter.chess.domain;

public record Move(Piece piece, PieceColor color, Position from, Position to) {
}
