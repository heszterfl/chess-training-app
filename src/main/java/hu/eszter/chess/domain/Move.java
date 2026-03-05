package hu.eszter.chess.domain;

public record Move(Piece piece, String color, Position from, Position to) {
}
