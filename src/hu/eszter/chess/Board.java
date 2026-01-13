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
    private List<Piece> removed = new ArrayList<>();
    private List<Move> pastMoves = new ArrayList<>();
    private Move lastMove;

    public Board() {
        board = new Piece[8][8];

        for (int i = 0; i < board.length; i++) {
            Piece pawn = new Pawn("black", i);
            board[1][i] = pawn;
            pawn.startingPosition = new Position(1, i);
        }

        for (int i = 0; i < board.length; i++) {
            Piece pawn = new Pawn("white", i);
            board[6][i] = pawn;
            pawn.startingPosition = new Position(6, i);
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

    public Piece getPieceAt(Piece[][] board, Position position) {
        if (board[position.row()][position.col()] == null) {
            return null;
        }
        return board[position.row()][position.col()];
    }

    public void setPieceAt(Position currentPosition, Position newPos) {
        int currentX = currentPosition.row();
        int currentY = currentPosition.col();
        int newX = newPos.row();
        int newY = newPos.col();

        Piece piece = getPieceAt(board, currentPosition);

        ListIterator<Position> it;
        if (board[newX][newY] == null) {
            List<Position> possibleMoves = piece.getLegalMoves(board, piece.getCurrentPosition());

            System.out.println("Current board: ");
            for (Piece[] pieces : board) {
                for (int j = 0; j < board[0].length; j++) {
                    System.out.print(pieces[j] + " ");
                }
                System.out.println();
            }

            it = possibleMoves.listIterator();
            while (it.hasNext()) {
                if (it.next().equals(newPos)) {
                    if ((piece instanceof Pawn && (piece.color.equals("white") && newPos.row() == 0) || (piece.color.equals("black") && newPos.row() == 7))) {
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
            List<Position> possibleCaptures = piece.getLegalCaptures(board, piece.getCurrentPosition());
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
                if (it.next().equals(newPos)) {
                    Piece toRemove = getPieceAt(this.getBoard(), newPos);
                    removed.add(toRemove);
                    if (piece instanceof Pawn && (piece.color.equals("white") && newPos.row() == 0) || (piece.color.equals("black") && newPos.row() == 7)) {
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

    private static Piece getPiece(Position newPos, Piece piece) {
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

    public void moveEnPassant(Piece pawn, Position currentPos, Position newPos) {

        // remove enemy pawn
        Piece toRemove = getPieceAt(this.getBoard(), new Position(currentPos.row(), newPos.col()));
        removed.add(toRemove);
        board[currentPos.row()][newPos.col()] = null;

        // set capturing pawn's new position
        pawn.setCurrentPosition(newPos);

        // put pawn on new square, delete pawn from old square
        board[newPos.row()][newPos.col()] = pawn;
        board[currentPos.row()][currentPos.col()] = null;

        printBoard();
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
    public List<Move> getPastMoves() {
        return pastMoves;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void printBoard() {
        System.out.println("Current board: ");
        for (Piece[] pieces : board) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(pieces[j] + " ");
            }
            System.out.println();
        }
    }
}
