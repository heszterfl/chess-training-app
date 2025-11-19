package hu.eszter.chess;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static int[] convertSquareToArray(String square) {

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
        int[] result = new int[]{8 - x, y};
        return result;
    }

    public static String convertSquareToString(int[] position) {
        int x = 8 - position[0];
        int y = position[1];
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
            default -> "invalid";
        };
        return posY + posX;
    }

    public static void main(String[] args) throws IOException {

        Board board = new Board();
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                System.out.print(board.getBoard()[i][j] + " ");
            }
            System.out.println();
        }

// READING IN USER INPUT
        boolean gameOn = true;

        while (gameOn) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("What's your move with white? (i.e. e2-e4 / e2xf3 / Ng1-f3 / Ng1xf3) ");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                gameOn = false;
                break;
            }
            System.out.println("User input: " + input);

            String[] splitted = input.split("-|x");
            System.out.println(Arrays.toString(splitted));
            String start = splitted[0];
            String end = splitted[1];

            if (start.matches("[a-z][1-8]")) {
                if (!input.contains("x")) {  // simple move
                    splitted = input.split("-");
                    start = splitted[0];
                    end = splitted[1];
                } else {
                    splitted = input.split("x");
                    start = splitted[0];
                    end = splitted[1];
                }
            } else if (start.matches("[A-Z].+")) {
                if (!input.contains("x")) {
                    splitted = input.split("-");
                    start = splitted[0];
                    start = start.substring(1);
                    end = splitted[1];
                } else {
                    splitted = input.split("x");
                    start = splitted[0].substring(1);
                    end = splitted[1];
                }
            }

            board.setPieceAt(convertSquareToArray(start), convertSquareToArray(end));
        }
    }
}
