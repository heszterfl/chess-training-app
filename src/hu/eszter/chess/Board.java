package hu.eszter.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Board {

    public static Piece[][] board;
    List<Piece> whiteArmy;
    List<Piece> blackArmy;
    List<Piece> removed = new ArrayList<>();

    public Board() {
        board = new Piece[8][8];

        for (int i = 0; i < board.length; i++) {
            Piece pawn = new Pawn("black", i);
            board[1][i] = pawn;
            pawn.startingPosition = new int[]{1, i};
        }

        for (int i = 0; i < board.length; i++) {
            Piece pawn = new Pawn("white", i);
            board[6][i] = pawn;
            pawn.startingPosition = new int[]{6, i};
        }

        Piece whiteKing = new King("white");
        Piece blackKing = new King("black");
        Piece whiteQueen = new Queen("white");
        Piece blackQueen = new Queen("black");

        Piece whiteRookQ = new Rook("white", "queen");
        Piece whiteRookK = new Rook("white", "king");

        Piece blackRookQ = new Rook("black", "queen");
        Piece blackRookK = new Rook("black", "king");

        Piece whiteKnightQ = new Knight("white", "queen");
        Piece whiteKnightK = new Knight("white", "king");

        Piece blackKnightQ = new Knight("black", "queen");
        Piece blackKnightK = new Knight("black", "king");

        Piece whiteBishopQ = new Bishop("white", "queen");
        Piece whiteBishopK = new Bishop("white", "king");

        Piece blackBishopQ = new Bishop("black", "queen");
        Piece blackBishopK = new Bishop("black", "king");

        whiteArmy = new ArrayList<>();
        whiteArmy.add(whiteQueen);
        whiteArmy.add(whiteKing);
        whiteArmy.add(whiteRookK);
        whiteArmy.add(whiteRookQ);
        whiteArmy.add(whiteBishopK);
        whiteArmy.add(whiteBishopQ);
        whiteArmy.add(whiteKnightK);
        whiteArmy.add(whiteKnightQ);

        blackArmy = new ArrayList<>();
        blackArmy.add(blackQueen);
        blackArmy.add(blackKing);
        blackArmy.add(blackRookQ);
        blackArmy.add(blackRookK);
        blackArmy.add(blackBishopK);
        blackArmy.add(blackBishopQ);
        blackArmy.add(blackKnightK);
        blackArmy.add(blackKnightQ);

        initializeBoard();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPieceAt(Piece[][]board, int[] position) {
        if (board[position[0]][position[1]] == null) {
            return null;
        }
        return board[position[0]][position[1]];
    }

    public void setPieceAt(int[] currentPosition, int[] newPos) {
        int currentX = currentPosition[0];
        int currentY = currentPosition[1];
        int newX = newPos[0];
        int newY = newPos[1];
        Piece piece = getPieceAt(currentPosition);
        Piece piece = getPieceAt(board, currentPosition);

        ListIterator<int[]> it;
        if (board[newX][newY] == null) {
            List<int[]> possibleMoves = piece.getLegalMoves(board, piece.getCurrentPosition());

            System.out.println("Current board: ");
            for (Piece[] pieces : board) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(pieces[j] + " ");
                }
                System.out.println();
            }

            it = possibleMoves.listIterator();
            while (it.hasNext()) {
                if (Arrays.equals(it.next(), newPos)) {
                    if ((piece instanceof Pawn && (piece.color.equals("white") && newPos[0] == 0) || (piece.color.equals("black") && newPos[0] == 7))) {
                        Piece newPiece = getPiece(newPos, piece);
                        board[newX][newY] = newPiece;
                    } else {
                        piece.setCurrentPosition(newPos);
                        board[newX][newY] = piece;
                    }
                    board[currentX][currentY] = null;
                }
            }
        } else {
            List<int[]> possibleCaptures = piece.getLegalCaptures(board, piece.getCurrentPosition());
            System.out.println(piece);
            System.out.println("Current board: ");
            for (Piece[] pieces : board) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(pieces[j] + " ");
                }
                System.out.println();
            }

            it = possibleCaptures.listIterator();
            while (it.hasNext()) {
                if (Arrays.equals(it.next(), newPos)) {
                    Piece toRemove = getPieceAt(this.getBoard(), newPos);
                    removed.add(toRemove);
                    if (piece instanceof Pawn && (piece.color.equals("white") && newPos[0] == 0) || (piece.color.equals("black") && newPos[0] == 7)) {
                        Piece newPiece = getPiece(newPos, piece);
                        board[newX][newY] = newPiece;
                    } else {
                        piece.setCurrentPosition(newPos);
                        board[newX][newY] = piece;
                    }
                    board[currentX][currentY] = null;
                }
            }
        }
    }

    private static Piece getPiece(int[] newPos, Piece piece) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("getPiece(): ");
        String input = scanner.next();
        Piece newPiece = switch (input) {
            case "bishop" -> new Bishop(piece.color);
            case "knight" -> new Knight(piece.color);
            case "rook" -> new Rook(piece.color);
            default -> new Queen(piece.color);
        };
        newPiece.setCurrentPosition(newPos);
        return newPiece;
    }

    public void initializeBoard() {
        for (Piece p : whiteArmy) {
            int[] pos = p.getStartingPosition();
            board[pos[0]][pos[1]] = p;
            p.currentPosition = p.startingPosition;
        }
        for (Piece p : blackArmy) {
            int[] pos = p.getStartingPosition();
            board[pos[0]][pos[1]] = p;
            p.currentPosition = p.startingPosition;
        }
    }
}
