package hu.eszter.chess.domain;

import java.util.*;

public class Board {

    private final Piece[][] squares;

    List<Piece> whiteArmy;
    List<Piece> blackArmy;
    private List<Piece> removed = new ArrayList<>();
    private List<Move> pastMoves = new ArrayList<>();
    private Move lastMove;
    boolean kingInCheck = false;
    Position whiteKingPosition;
    Position blackKingPosition;
    Position kingPosition = isWhiteToMove() ? whiteKingPosition : blackKingPosition;
    boolean whiteToMove = true;
    public boolean isCheckMate = false;


    public Board() {
        this.squares = new Piece[8][8];

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

        for (int i = 0; i < squares.length; i++) {
            Piece pawn = new Pawn("black", i);
            squares[1][i] = pawn;
            pawn.startingPosition = new Position(1, i);
            blackArmy.add(pawn);
        }

        for (int i = 0; i < squares.length; i++) {
            Piece pawn = new Pawn("white", i);
            squares[6][i] = pawn;
            pawn.startingPosition = new Position(6, i);
            whiteArmy.add(pawn);
        }

        whiteKingPosition = whiteKing.getCurrentPosition();
        blackKingPosition = blackKing.getCurrentPosition();

        initializeBoard();
    }

    public Board(HashMap<Position, String> white, HashMap<Position, String> black) {
        this.squares = new Piece[8][8];

        Set<Map.Entry<Position, String>> entryWhite = white.entrySet();
        for (Map.Entry<Position, String> e : entryWhite) {
            Position k = e.getKey();
            String v = e.getValue();
            Piece p = switch (v) {
                case "king" -> new King("white");
                case "queen" -> new Queen("white");
                case "bishop" -> new Bishop("white");
                case "knight" -> new Knight("white");
                case "rook" -> new Rook("white");
                case "pawn" -> new Pawn("white");
                default -> null;
            };
            if (p instanceof King) {
                whiteKingPosition = k;
            }
            p.setCurrentPosition(k);
            int x = k.row();
            int y = k.col();
            squares[x][y] = p;
        }

        Set<Map.Entry<Position, String>> entryBlack = black.entrySet();
        for (Map.Entry<Position, String> e : entryBlack) {
            Position k = e.getKey();
            String v = e.getValue();
            Piece p = switch (v) {
                case "king" -> new King("black");
                case "queen" -> new Queen("black");
                case "bishop" -> new Bishop("black");
                case "knight" -> new Knight("black");
                case "rook" -> new Rook("black");
                case "pawn" -> new Pawn("black");
                default -> null;
            };

            if (p instanceof King) {
                blackKingPosition = k;
            }
            p.setCurrentPosition(k);

            int x = k.row();
            int y = k.col();
            squares[x][y] = p;
        }
    }

    public String convertSquareToString(Position position) {
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
            default -> "invalid";
        };
        return posY + posX;
    }

    public Piece[][] getBoard() {
        return squares;
    }

    public Piece getPieceAt(Piece[][] board, Position position) {
        if (board[position.row()][position.col()] == null) {
            return null;
        }
        return squares[position.row()][position.col()];
    }

    public void applyMove(Piece piece, Position currentPosition, Position newPos) {
        int currentX = currentPosition.row();
        int currentY = currentPosition.col();
        int newX = newPos.row();
        int newY = newPos.col();

        if (piece instanceof King && piece.getColor().equals("white")) {
            whiteKingPosition = newPos;
        } else if (piece instanceof King && piece.getColor().equals("black")) {
            blackKingPosition = newPos;
        }

        piece.setCurrentPosition(newPos);
        squares[newX][newY] = piece;
        squares[currentX][currentY] = null;

        lastMove = new Move(piece, piece.getColor(), currentPosition, newPos);
        pastMoves.add(lastMove);

        whiteToMove = !whiteToMove;

        if (whiteToMove && isCheckmated(whiteKingPosition)) {
            isCheckMate = true;
            System.out.println("Checkmate!");
        }
    }

    public boolean tryMove(Position currentPos, Position newPos) {

        if (!inbounds(currentPos) || !inbounds(newPos)) return false;

        Piece piece = getPieceAt(squares, currentPos);
        if (piece == null) {
            return false;
        }

        if (currentPos.equals(newPos)) {
            return false;
        }

        if (isKingInCheck(lastMove.piece())) {
            if (piece instanceof King && (piece.getLegalMoves(squares, piece.getCurrentPosition()).contains(newPos) ||
                    piece.getLegalCaptures(squares, piece.getCurrentPosition()).contains(newPos))) {
                applyMove(piece, currentPos, newPos);
                return true;
            } else if (!(piece instanceof King) && piece.getLegalCaptures(squares, piece.getCurrentPosition()).contains(lastMove.to())) {
                applyMove(piece, currentPos, newPos);
                return true;
            } else if (piece.getLegalMoves(squares, piece.getCurrentPosition()).contains(newPos) &&
                    getSquaresBetween(lastMove.to(), kingPosition).contains(newPos)) {
                applyMove(piece, currentPos, newPos);
                return true;
            }
        }

        boolean targetEmpty = getPieceAt(squares, newPos) == null;

        if (targetEmpty) {
            if (piece instanceof Pawn pawn) {
                Position ep = pawn.getEnPassant(getLastMove());
                if (ep != null && ep.equals(newPos)) {
                    moveEnPassant(pawn, currentPos, newPos);
                    return true;
                }
            }

            if (!piece.getLegalMoves(squares, currentPos).contains(newPos)) {
                return false;
            }

            if (isPromotionSquare(piece, newPos)) {
                Piece newPiece = new Queen(piece.color);
                applyMove(newPiece, currentPos, newPos);
                return true;
            }

            applyMove(piece, currentPos, newPos);
            return true;
        } else {
            if (!piece.getLegalCaptures(squares, currentPos).contains(newPos)) {
                return false;
            }

            Piece toRemove = getPieceAt(squares, newPos);
            if (toRemove != null) {
                removed.add(toRemove);
                toRemove.setCurrentPosition(null);
            }


            if (isPromotionSquare(piece, newPos)) {
                Piece newPiece = new Queen(piece.color);
                applyMove(newPiece, currentPos, newPos);
                return true;
            }

            applyMove(piece, currentPos, newPos);
            return true;
        }
    }

    private boolean inbounds(Position pos) {
        return pos.row() >= 0 && pos.row() <= 7 && pos.col() >= 0 && pos.col() <= 7;
    }

    private boolean isPromotionSquare(Piece piece, Position to) {
        return piece instanceof Pawn &&
                ((piece.color.equals("white") && to.row() == 0) || (piece.color.equals("black") && to.row() == 7));
    }

    private static Piece getPiece(Position newPos, Piece piece) {
        // TODO: replace console input with UI selection
        Scanner scanner = new Scanner(System.in);
        System.out.println("getPiece(): ");
        String input = scanner.next();
        Piece newPiece = switch (input) {
            case "bishop" -> new Bishop(piece.color);
            case "knight" -> new Knight(piece.color);
            case "rook" -> new Rook(piece.color);
            default -> new Queen(piece.color);
        };

        return newPiece;
    }

    public List<Position> getSquaresBetween(Position attacker, Position king) {
        List<Position> squaresBetween = new ArrayList<>();
        Piece attackerPiece = getPieceAt(getBoard(), attacker);
        Position direction = getDirectionToKing(attacker, king);
        int rowDiff = Math.abs(attacker.row() - king.row());
        int colDiff = Math.abs(attacker.col() - king.col());

        if ((attackerPiece instanceof Queen || attackerPiece instanceof Bishop) &&
            (rowDiff == colDiff)) {
            int startIndex = attacker.row();
            int j = attacker.col();
            for (int i = startIndex; i != king.row() + (direction.row() * -1); i += direction.row()) {
                squaresBetween.add(new Position(i + direction.row(), j + direction.col()));
                j += direction.col();
            }
        } else if ((attackerPiece instanceof Queen || attackerPiece instanceof Rook) &&
        (attacker.row() == king.row() || attacker.col() == king.col())) {
            int startIndex;
            int endIndex;
            if (attacker.row() == king.row()) {
                startIndex = attacker.col();
                endIndex = king.col();
                for (int i = startIndex; i != endIndex + (direction.col() * -1); i += direction.col()) {
                    squaresBetween.add(new Position( king.row(), i + direction.col()));
                }
            } else {
                startIndex = attacker.row();
                endIndex = king.row();
                for (int i = startIndex; i != endIndex + (direction.row() * -1); i += direction.row()) {
                    squaresBetween.add(new Position(i + direction.row(), king.col()));
                }
            }
        }
        return squaresBetween;
    }

    public Position getDirectionToKing(Position attacker, Position king) {
        Position direction;

        if (attacker.row() < king.row() && attacker.col() < king.col()) {
            direction = new Position(1, 1);
        } else if (attacker.row() < king.row() && attacker.col() > king.col()) {
            direction = new Position(1, -1);
        } else if (attacker.row() > king.row() && attacker.col() > king.col()) {
            direction = new Position(-1, -1);
        } else if (attacker.row() > king.row() && attacker.col() < king.col()){
            direction = new Position(-1, 1);
        } else if (attacker.row() < king.row()) {
            direction = new Position(1, 0);
        } else if (attacker.row() == king.row() && attacker.col() > king.col()) {
            direction = new Position(0, -1);
        } else if (attacker.row() > king.row()) {
            direction = new Position(-1, 0);
        } else {
            direction = new Position(0, 1);
        }

        return direction;
    }

    public boolean isKingInCheck(Piece attackingPiece) {
        if (attackingPiece.getColor().equals("white")) {
            if (attackingPiece.getLegalCaptures(squares, attackingPiece.getCurrentPosition()).contains(blackKingPosition)) {
                kingInCheck = true;
                System.out.println("Black king in check");
                return true;
            }
        } else if (attackingPiece.getColor().equals("black")) {
            if (attackingPiece.getLegalCaptures(squares, attackingPiece.getCurrentPosition()).contains(whiteKingPosition)) {
                kingInCheck = true;
                System.out.println("White king in check");
                return true;
            }
        }
        return false;
    }

    public boolean isCheckmated(Position kingPos) {
        List<Position> legalCaptures = new ArrayList<>();
        boolean legalCaptureFound = false;
        List<Piece> army = isWhiteToMove() ? getWhiteArmy(squares) : getBlackArmy(squares);
        for (Piece p : army) {
            legalCaptures = p.getLegalCaptures(squares, p.getCurrentPosition());
            if (!legalCaptures.isEmpty()) {
                legalCaptureFound = true;
                break;
            }
        }

        boolean blockingFound = false;
        List<Position> legalMoves;
        List<Position> squaresBetween = getSquaresBetween(lastMove.to(), kingPos);
        for (Piece p : army) {
            for (Position pos : squaresBetween) {
                legalMoves = p.getLegalMoves(squares, p.getCurrentPosition());
                if (legalMoves.contains(pos)) {
                    blockingFound = true;
                    break;
                }
            }
        }

        Piece king = getPieceAt(squares, kingPos);
        if (isKingInCheck(lastMove.piece())) {
            if (king.getLegalMoves(squares, king.getCurrentPosition()).isEmpty() &&
                    king.getLegalCaptures(squares, king.getCurrentPosition()).isEmpty() &&
                    legalCaptures.isEmpty() &&
                    !legalCaptureFound &&
                    !blockingFound) {
                return true;
            }
        }
        return false;
    }

    public List<Piece> getWhiteArmy(Piece[][] board) {
        List<Piece> whiteArmy = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                if (board[i][j].getColor().equals("white")) {
                    Piece p = getPieceAt(board, new Position(i, j));
                    whiteArmy.add(p);
                }
            }
        }
        return whiteArmy;
    }

    public List<Piece> getBlackArmy(Piece[][] board) {
        List<Piece> blackArmy = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                if (board[i][j].getColor().equals("black")) {
                    Piece p = getPieceAt(board, new Position(i, j));
                    blackArmy.add(p);
                }
            }
        }
        return blackArmy;
    }

    public void moveEnPassant(Piece pawn, Position currentPos, Position newPos) {

        Piece toRemove = getPieceAt(this.getBoard(), new Position(currentPos.row(), newPos.col()));
        removed.add(toRemove);
        squares[currentPos.row()][newPos.col()] = null;

        applyMove(pawn, currentPos, newPos);

        printBoard();
    }

    public void initializeBoard() {
        for (Piece p : whiteArmy) {
            int[] pos = p.getStartingPosition();
            squares[pos[0]][pos[1]] = p;
            p.currentPosition = p.startingPosition;
        }
        for (Piece p : blackArmy) {
            int[] pos = p.getStartingPosition();
            squares[pos[0]][pos[1]] = p;
            p.currentPosition = p.startingPosition;
        }
    }

    public List<Move> getPastMoves() {
        return pastMoves;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move move) {
        this.lastMove = move;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public void printBoard() {
        System.out.println("Current board: ");
        for (Piece[] pieces : squares) {
            for (int j = 0; j < squares[0].length; j++) {
                System.out.print(pieces[j] + " ");
            }
            System.out.println();
        }
    }
}
