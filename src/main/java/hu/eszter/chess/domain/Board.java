package hu.eszter.chess.domain;

import java.util.*;

import static hu.eszter.chess.domain.PieceColor.BLACK;
import static hu.eszter.chess.domain.PieceColor.WHITE;

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

        Piece whiteKing = new King(WHITE);
        Piece blackKing = new King(BLACK);
        Piece whiteQueen = new Queen(WHITE);
        Piece blackQueen = new Queen(BLACK);

        Piece whiteRookQ = new Rook(WHITE, "queen");
        Piece whiteRookK = new Rook(WHITE, "king");

        Piece blackRookQ = new Rook(BLACK, "queen");
        Piece blackRookK = new Rook(BLACK, "king");

        Piece whiteKnightQ = new Knight(WHITE, "queen");
        Piece whiteKnightK = new Knight(WHITE, "king");

        Piece blackKnightQ = new Knight(BLACK, "queen");
        Piece blackKnightK = new Knight(BLACK, "king");

        Piece whiteBishopQ = new Bishop(WHITE, "queen");
        Piece whiteBishopK = new Bishop(WHITE, "king");

        Piece blackBishopQ = new Bishop(BLACK, "queen");
        Piece blackBishopK = new Bishop(BLACK, "king");

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
            Piece pawn = new Pawn(BLACK, i);
            squares[1][i] = pawn;
            pawn.startingPosition = new Position(1, i);
            blackArmy.add(pawn);
        }

        for (int i = 0; i < squares.length; i++) {
            Piece pawn = new Pawn(WHITE, i);
            squares[6][i] = pawn;
            pawn.startingPosition = new Position(6, i);
            whiteArmy.add(pawn);
        }

        whiteKingPosition = whiteKing.getCurrentPosition();
        blackKingPosition = blackKing.getCurrentPosition();

        initializeBoard();
    }

    public Board(Board other) {
        this.squares = new Piece[8][8];
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.squares[i][j] = other.squares[i][j];
            }
        }

        this.whiteKingPosition = other.whiteKingPosition;
        this.blackKingPosition = other.blackKingPosition;
        this.lastMove = other.lastMove;
        this.whiteToMove = other.whiteToMove;
    }

    public Board(HashMap<Position, String> white, HashMap<Position, String> black) {
        this.squares = new Piece[8][8];

        Set<Map.Entry<Position, String>> entryWhite = white.entrySet();
        for (Map.Entry<Position, String> e : entryWhite) {
            Position k = e.getKey();
            String v = e.getValue();
            Piece p = switch (v) {
                case "king" -> new King(WHITE);
                case "queen" -> new Queen(WHITE);
                case "bishop" -> new Bishop(WHITE);
                case "knight" -> new Knight(WHITE);
                case "rook" -> new Rook(WHITE);
                case "pawn" -> new Pawn(WHITE);
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
                case "king" -> new King(BLACK);
                case "queen" -> new Queen(BLACK);
                case "bishop" -> new Bishop(BLACK);
                case "knight" -> new Knight(BLACK);
                case "rook" -> new Rook(BLACK);
                case "pawn" -> new Pawn(BLACK);
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

    public Piece getPieceAt(Position position) {
        if (squares[position.row()][position.col()] == null) {
            return null;
        }
        return squares[position.row()][position.col()];
    }

    public void applyMove(Piece piece, Position currentPosition, Position newPos) {
        int currentX = currentPosition.row();
        int currentY = currentPosition.col();
        int newX = newPos.row();
        int newY = newPos.col();

        if (piece instanceof King && piece.getColor() == WHITE) {
            whiteKingPosition = newPos;
        } else if (piece instanceof King && piece.getColor() == BLACK) {
            blackKingPosition = newPos;
        }

        Piece toRemove = getPieceAt(newPos);
        if (toRemove != null) {
            removed.add(toRemove);
            toRemove.setCurrentPosition(null);
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

        if (currentPos.equals(newPos)) {
            return false;
        }

        Piece piece = getPieceAt(currentPos);
        if (piece == null) {
            return false;
        }

        if ((whiteToMove && piece.getColor() == BLACK) ||
                (!whiteToMove && piece.getColor() == WHITE)) {
            return false;
        }

        Piece targetPiece = getPieceAt(newPos);
        if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
            return false;
        }

        if (isLegalMove(currentPos, newPos)) {
            applyMove(piece, currentPos, newPos);
            return true;
        }

        return false;
    }

    private void simulateMove(Piece piece, Position currentPos, Position newPos) {
        int currentX = currentPos.row();
        int currentY = currentPos.col();
        int newX = newPos.row();
        int newY = newPos.col();

        if (piece instanceof King && piece.getColor() == WHITE) {
            whiteKingPosition = newPos;
        } else if (piece instanceof King && piece.getColor() == BLACK) {
            blackKingPosition = newPos;
        }

        squares[newX][newY] = piece;
        squares[currentX][currentY] = null;
    }

    private boolean isLegalMove(Position currentPos, Position newPos) {

        if (!isPseudoLegalMove(currentPos, newPos)) {
            return false;
        }

        Board copy = new Board(this);
        PieceColor currentPlayerColor = copy.whiteToMove ? WHITE : BLACK;
        copy.simulateMove(copy.getPieceAt(currentPos), currentPos, newPos);

        return !copy.isKingInCheck(currentPlayerColor);
    }

    private boolean isPseudoLegalMove(Position currentPos, Position newPos) {
        Piece piece = getPieceAt(currentPos);

        if (piece == null) {
            return false;
        }

        boolean targetEmpty = getPieceAt(newPos) == null;

        if (piece instanceof Pawn pawn) {
            if (targetEmpty) {
                Position ep = pawn.getEnPassant(getLastMove());
                if (isPromotionSquare(piece, newPos)) {
                    return true;
                } else if (ep != null && ep.equals(newPos)) {
                    return true;
                }
            } else {
                if (isPromotionSquare(piece, newPos)) {
                    return true;
                }
            }
        }

        if (targetEmpty) {
            if (!piece.getLegalMoves(squares, currentPos).contains(newPos)) {
                return false;
            }
            return true;
        } else {
            if (!piece.getLegalCaptures(squares, currentPos).contains(newPos)) {
                return false;
            }
            return true;
        }
    }

    private boolean inbounds(Position pos) {
        return pos.row() >= 0 && pos.row() <= 7 && pos.col() >= 0 && pos.col() <= 7;
    }

    private boolean isPromotionSquare(Piece piece, Position to) {
        return piece instanceof Pawn &&
                ((piece.getColor() == WHITE && to.row() == 0) || (piece.getColor() == BLACK && to.row() == 7));
    }

    private static Piece getPiece(Position newPos, Piece piece) {
        // TODO: replace console input with UI selection
        Scanner scanner = new Scanner(System.in);
        System.out.println("getPiece(): ");
        String input = scanner.next();
        Piece newPiece = switch (input) {
            case "bishop" -> new Bishop(piece.getColor());
            case "knight" -> new Knight(piece.getColor());
            case "rook" -> new Rook(piece.getColor());
            default -> new Queen(piece.getColor());
        };

        return newPiece;
    }

    private Position getKingPosition(PieceColor color) {

        return color == WHITE ? whiteKingPosition : blackKingPosition;
    }

    private boolean isSquareAttacked(Position square, PieceColor byColor) {

        int x = square.row();
        int y = square.col();
        Piece piece;
        Position attackerPos;

        // Pawn attack
        if (byColor == WHITE) {

            attackerPos = new Position(x + 1, y - 1);
            if (inbounds(attackerPos)) {
                piece = getPieceAt(attackerPos);
                if (piece != null && piece.getColor() == byColor && piece instanceof Pawn) {
                    return true;
                }
            }

            attackerPos = new Position(x + 1, y + 1);
            if (inbounds(attackerPos)) {
                piece = getPieceAt(attackerPos);
                if (piece != null && piece.getColor() == byColor && piece instanceof Pawn) {
                    return true;
                }
            }
        } else {

            attackerPos = new Position(x - 1, y - 1);
            if (inbounds(attackerPos)) {
                piece = getPieceAt(attackerPos);
                if (piece != null && piece.getColor() == byColor && piece instanceof Pawn) {
                    return true;
                }
            }

            attackerPos = new Position(x - 1, y + 1);
            if (inbounds(attackerPos)) {
                piece = getPieceAt(attackerPos);
                if (piece != null && piece.getColor() == byColor && piece instanceof Pawn) {
                    return true;
                }
            }
        }

        // Knight
        int[] xValues = {-2, -1, 1, 2};
        int[] yValues = {-2, -1, 1, 2};
        for (int v : xValues) {
            for (int w : yValues) {
                if (Math.abs(v) == Math.abs(w)) {
                    continue;
                }
                int targetRow = x + v;
                int targetCol = y + w;
                Position targetPos = new Position(targetRow, targetCol);

                if (inbounds(targetPos)) {
                    attackerPos = targetPos;
                    piece = getPieceAt(attackerPos);
                } else {
                    continue;
                }

                if (piece == null) {
                    continue;
                }
                if (piece.getColor() == byColor && piece instanceof Knight) {
                    return true;
                }
            }
        }

        // King
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                attackerPos = new Position(x+i, y+j);
                if (inbounds(attackerPos)) {
                    piece = getPieceAt(attackerPos);

                    if (piece != null && piece.getColor() == byColor && piece instanceof King) {
                        return true;
                    }
                }
            }
        }

        // Sliding pieces
        // Up
        int currentX = x - 1;
        while (currentX >= 0) {
            attackerPos = new Position(currentX, y);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentX--;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Rook)) {
                return true;
            } else {
                break;
            }
        }

        // Down
        currentX = x + 1;
        while (currentX < 8) {
            attackerPos = new Position(currentX, y);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentX++;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Rook)) {
                return true;
            } else {
                break;
            }
        }

        // Left
        int currentY = y - 1;
        while (currentY >= 0) {
            attackerPos = new Position(x, currentY);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentY--;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Rook)) {
                return true;
            } else {
                break;
            }
        }

        // Right
        currentY = y + 1;
        while (currentY < 8) {
            attackerPos = new Position(x, currentY);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentY++;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Rook)) {
                return true;
            } else {
                break;
            }
        }

        // Up-left
        currentX = x-1;
        currentY = y-1;
        while (currentX >= 0 && currentY >= 0) {
            attackerPos = new Position(currentX, currentY);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentX--;
                currentY--;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Bishop)) {
                return true;
            } else {
                break;
            }
        }

        // Down-right
        currentX = x + 1;
        currentY = y + 1;
        while (currentX < 8 && currentY < 8) {
            attackerPos = new Position(currentX, currentY);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentX++;
                currentY++;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Bishop)) {
                return true;
            } else {
                break;
            }
        }

        // Up-right
        currentX = x - 1;
        currentY = y + 1;
        while (currentX >= 0 && currentY < 8) {
            attackerPos = new Position(currentX, currentY);
            piece = getPieceAt(attackerPos);
            if (piece == null) {
                currentX--;
                currentY++;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Bishop)) {
                return true;
            } else {
                break;
            }
        }

        // Down-left
        currentX = x + 1;
        currentY = y - 1;
        while (currentX < 8 && currentY >= 0) {
            attackerPos = new Position(currentX, currentY);
            piece = getPieceAt(attackerPos);
            if ( piece == null) {
                currentX++;
                currentY--;
            } else if ((piece.getColor() == byColor) && (piece instanceof Queen || piece instanceof Bishop)) {
                return true;
            } else {
                break;
            }
        }

        return false;
    }

    private boolean isKingInCheck(PieceColor color) {
        Position kingPos = getKingPosition(color);
        PieceColor opponent = (color == WHITE ? BLACK : WHITE);
        return isSquareAttacked(kingPos, opponent);
    }

    public List<Position> getSquaresBetween(Position attacker, Position king) {
        List<Position> squaresBetween = new ArrayList<>();
        Piece attackerPiece = getPieceAt(attacker);
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
        if (attackingPiece.getColor() == WHITE) {
            if (attackingPiece.getLegalCaptures(squares, attackingPiece.getCurrentPosition()).contains(blackKingPosition)) {
                kingInCheck = true;
                System.out.println("Black king in check");
                return true;
            }
        } else if (attackingPiece.getColor() == BLACK) {
            if (attackingPiece.getLegalCaptures(squares, attackingPiece.getCurrentPosition()).contains(whiteKingPosition)) {
                kingInCheck = true;
                System.out.println("White king in check");
                return true;
            }
        }
        return false;
    }

    public boolean isCheckmated(Position kingPos) {
        if (lastMove == null) {
            return false;
        }
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

        Piece king = getPieceAt(kingPos);
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
                if (board[i][j].getColor() == WHITE) {
                    Piece p = getPieceAt(new Position(i, j));
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
                if (board[i][j].getColor() == BLACK) {
                    Piece p = getPieceAt(new Position(i, j));
                    blackArmy.add(p);
                }
            }
        }
        return blackArmy;
    }

    public void moveEnPassant(Piece pawn, Position currentPos, Position newPos) {

        Piece toRemove = getPieceAt(new Position(currentPos.row(), newPos.col()));
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

    public boolean getIsLegalMove(Position currentPos, Position newPos) {
        return isLegalMove(currentPos, newPos);
    }
}
