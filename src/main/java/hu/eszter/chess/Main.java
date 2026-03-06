package hu.eszter.chess;

import hu.eszter.chess.domain.Board;
import hu.eszter.chess.domain.PieceColor;
import hu.eszter.chess.domain.Position;

import java.util.*;

public class Main {

    public static Position convertSquareToArray(String square) {

        String subY = square.substring(0, 1);
        String subX = square.substring(1);
        int y = switch (subY) {
            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> -1;
        };
        int x = Integer.parseInt(subX);
        return new Position(8 - x, y);
    }

    public static void main(String[] args) {

        Board board;
        Position from;
        Position to;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose: Start / Custom ");
        String playStart = scanner.next();

        HashMap<Position, String> whiteArmy = new HashMap<>();
        HashMap<Position, String> blackArmy = new HashMap<>();
        if (playStart.equalsIgnoreCase("start")) {
            board = new Board();
        } else {
            board = new Board(whiteArmy, blackArmy);
            whiteArmy.put(convertSquareToArray("b5"), "pawn");
            whiteArmy.put(convertSquareToArray("e4"), "pawn");
            whiteArmy.put(convertSquareToArray("h4"), "pawn");
            whiteArmy.put(convertSquareToArray("e1"), "king");
            blackArmy.put(convertSquareToArray("a7"), "pawn");
            blackArmy.put(convertSquareToArray("d7"), "pawn");
            blackArmy.put(convertSquareToArray("g7"), "pawn");
            blackArmy.put(convertSquareToArray("e8"), "king");
        }

// READING IN USER INPUT
        while (!board.isCheckMate) {
            board.printBoard();
            scanner = new Scanner(System.in);
            System.out.println("What's your move with " + (board.isWhiteToMove() ? "white" : "black") + "? (i.e. e2-e4 / e2xf3 / Ng1-f3 / Ng1xf3) ");
            String input = scanner.next();
            if (input.equals("q")) {
                break;
            }

            System.out.println("User input: " + input);

            String[] split = input.split("[-x]");
            System.out.println(Arrays.toString(split));
            String start = split[0];
            String end = split[1];

            if (start.matches("[a-z][1-8]")) {
                if (!input.contains("x")) {
                    split = input.split("-");
                } else {
                    split = input.split("x");
                }
                start = split[0];
                end = split[1];
            } else if (start.matches("[A-Z].+")) {
                if (!input.contains("x")) {
                    split = input.split("-");
                    start = split[0];
                    start = start.substring(1);
                } else {
                    split = input.split("x");
                    start = split[0].substring(1);
                }
                end = split[1];
            }

            from = convertSquareToArray(start);
            to = convertSquareToArray(end);

            if (board.isWhiteToMove() && board.getPieceAt(from).getColor() == PieceColor.WHITE) {
                board.tryMove(from, to);

            } else if (!board.isWhiteToMove() && board.getPieceAt(from).getColor() == PieceColor.BLACK) {
                board.tryMove(from, to);
            } else {
                continue;
            }

            board.printBoard();
        }
    }
}
