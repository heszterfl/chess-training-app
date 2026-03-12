package hu.eszter.chess.util;

import hu.eszter.chess.domain.Position;

public final class SquareNotation {

    private SquareNotation() {}

    public static String toSquare(Position position) {

        int x = 8 - position.row();
        int y = position.col();
        String posX = Integer.toString(x);
        String posY = switch (y) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> throw new IllegalArgumentException("Invalid column");
        };
        return posY + posX;
    }

    public static Position fromSquare(String square) {

        if (square.length() != 2) {
            throw new IllegalArgumentException("Invalid square");
        }

        int x = Integer.parseInt(square.substring(1));
        if (x < 1 || x > 8) {
            throw new IllegalArgumentException("Invalid row");
        }

        String subY = square.substring(0, 1);
        int y = switch (subY) {
            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> throw new IllegalArgumentException("Invalid column");
        };

        return new Position(8 - x, y);
    }
}
