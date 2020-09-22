package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.exceptions.InvalidBoardException;
import org.example.javachessclient.chess.exceptions.InvalidMoveException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chess {
    public static final String startingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Pattern fenRegexPattern = Pattern.compile("(?<piecePlacement>[pnbrqkPNBRQK1-8/]+) (?<activeColor>w|b) (?<cannotCastle>-)?(?<wck>K)?(?<wcq>Q)?(?<bck>k)?(?<bcq>q)? (?<enPassantSquare>-|[a-h][1-8]) (?<halfmoveClock>\\d+) (?<fullmoveNumber>\\d+)");

    private final ChessCanvas chessCanvas;
    private ArrayList<ArrayList<Piece>> board;
    private ArrayList<Move> recordedMoves;
    private boolean whiteToMove;
    private boolean whiteCanCastleKingside;
    private boolean whiteCanCastleQueenside;
    private boolean blackCanCastleKingside;
    private boolean blackCanCastleQueenside;
    private Square enPassantSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    private Square selectedSquare;
    private ArrayList<Move> availableMoves; // of the selected piece

    public Chess() {
        // start a new game
        chessCanvas = new ChessCanvas(this);
        recordedMoves = new ArrayList<>();
        try {
            loadFEN(startingFEN);
        } catch (ParseException e) {
            System.out.println("Chess: failed to parse starting position: " + e.getMessage());
        }
        chessCanvas.redrawBoard();
    }

    private void loadFEN(String fen) throws ParseException {
        Matcher matcher = fenRegexPattern.matcher(fen);
        if (!matcher.find()) {
            throw new ParseException("Invalid FEN string", 0);
        }

        // piece placement
        board = new ArrayList<>();
        for (String line : matcher.group("piecePlacement").split("/")) {
            board.add(new ArrayList<>());
            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    for (int _i = 0; _i < Character.digit(c, 10); ++_i) {
                        board.get(board.size() - 1).add(null);
                    }
                } else {
                    int rank = board.size() - 1;
                    int file = board.get(rank).size();
                    Square square = new Square(file, rank);
                    boolean isWhite = Character.isUpperCase(c);
                    Piece piece;
                    switch (Character.toLowerCase(c)) {
                        case 'p':
                            piece = new Pawn(this, square, isWhite);
                            break;
                        case 'n':
                            piece = new Knight(this, square, isWhite);
                            break;
                        case 'b':
                            piece = new Bishop(this, square, isWhite);
                            break;
                        case 'r':
                            piece = new Rook(this, square, isWhite);
                            break;
                        case 'q':
                            piece = new Queen(this, square, isWhite);
                            break;
                        case 'k':
                            piece = new King(this, square, isWhite);
                            break;
                        default:
                            throw new ParseException("Invalid piece type in FEN string: " + c, i);
                    }
                    board.get(board.size() - 1).add(piece);
                }
            }
        }

        // active color
        whiteToMove = matcher.group("activeColor").equals("w");

        // castling availability
        if (matcher.group("cannotCastle") != null) {
            whiteCanCastleKingside = false;
            whiteCanCastleQueenside = false;
            blackCanCastleKingside = false;
            blackCanCastleQueenside = false;
        } else {
            whiteCanCastleKingside = matcher.group("wck") != null;
            whiteCanCastleQueenside = matcher.group("wcq") != null;
            blackCanCastleKingside = matcher.group("bck") != null;
            blackCanCastleQueenside = matcher.group("bcq") != null;
        }

        // en passant square
        String enPassantSquareString = matcher.group("enPassantSquare");
        if (enPassantSquareString.equals("-")) {
            enPassantSquare = null;
        } else {
            enPassantSquare = new Square(enPassantSquareString);
        }

        // halfmove clock
        halfmoveClock = Integer.parseInt(matcher.group("halfmoveClock"));

        // fullmove number
        fullmoveNumber = Integer.parseInt(matcher.group("fullmoveNumber"));
    }

    // player input

    public void onSquareClicked(Square square) {
        if (selectedSquare == null) {
            // select first piece

            selectedSquare = square;
            chessCanvas.highlightSquare(selectedSquare);

            Piece piece = pieceAt(square);
            if (piece != null) {
                availableMoves = piece.findAvailableMoves();
                chessCanvas.highlightAvailableMoves(availableMoves);
            }
        } else {
            // select second piece

            Move move = null;
            if (availableMoves == null) {
                // selected destination first then piece to move
                Piece piece = pieceAt(square);
                if (piece == null) return;
                availableMoves = piece.findAvailableMoves();
            }
            // find move from selected square
            for (Move availableMove : availableMoves) {
                if (availableMove.getSquare() == square) {
                    move = availableMove;
                    break;
                }
            }
            if (move == null) {
                // invalid move, TODO clear selected piece & highlights
                return;
            }

            try {
                playMove(move);
            } catch (InvalidMoveException e) {
                // do nothing
            } finally {
                chessCanvas.redrawSquare(selectedSquare); // also clears the outline of the first selected square
                selectedSquare = null;
                availableMoves = null;
            }
        }
    }

    // board utils

    public Piece pieceAt(int file, int rank) {
        return board.get(rank).get(file);
    }

    public Piece pieceAt(Square square) {
        return board.get(square.getRank()).get(square.getFile());
    }

    public ArrayList<Move> diagonalMoves(Piece piece) {
        // from initial square
        ArrayList<Move> available = new ArrayList<>();
        int fromFile = piece.getSquare().getFile();
        int fromRank = piece.getSquare().getRank();
        for (int fileDir : new int[]{-1, 1}) {
            for (int rankDir : new int[]{-1, 1}) {
                addMovesInDir(piece, available, fromFile, fromRank, fileDir, rankDir);
            }
        }
        return available;
    }

    public ArrayList<Move> lineMoves(Piece piece) {
        // returns line squares the piece can move to. pieceIsWhite is the color of the piece with line reaches
        ArrayList<Move> available = new ArrayList<>();
        int fromFile = piece.getSquare().getFile();
        int fromRank = piece.getSquare().getRank();
        for (int[] dir : new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}}) {
            int fileDir = dir[0];
            int rankDir = dir[1];
            addMovesInDir(piece, available, fromFile, fromRank, fileDir, rankDir);
        }
        return available;
    }

    private void addMovesInDir(Piece piece, ArrayList<Move> available, int fromFile, int fromRank, int fileDir, int rankDir) {
        // automatically refactored
        for (int file = fromFile + fileDir, rank = fromRank + rankDir; 0 <= file && file < 8 && 0 <= rank && rank < 8; file += fileDir, rank += rankDir) {
            Piece landingPiece = board.get(file).get(rank);
            if (landingPiece == null) {
                available.add(new Move(piece, new Square(file, rank), MoveType.normal));
            } else if (landingPiece.getIsWhite() == piece.getIsWhite()) {
                break;
            } else {
                available.add(new Move(piece, new Square(file, rank), MoveType.capture));
                break;
            }
        }
    }

    public boolean moveLeavesKingInCheck(Move move) {
        playMove(move);
        boolean kingInCheck = activeKingInCheck();
        undoMove();
        return kingInCheck;
    }

    private boolean activeKingInCheck() {
        King activeKing = activeKing();
        if (activeKing == null) throw new InvalidBoardException();
        for (ArrayList<Piece> rankList : board) {
            for (Piece piece : rankList) {
                // TODO: add `canMoveTo(Square square) -> MoveType` if this is too slow
                for (Move availableMove : piece.findAvailableMoves()) {
                    if (availableMove.getType() == MoveType.capture && availableMove.getSquare() == activeKing.getSquare()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private King activeKing() {
        for (ArrayList<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece instanceof King && piece.getIsWhite() == whiteToMove) {
                    return (King) piece;
                }
            }
        }
        throw new InvalidBoardException();
    }

    private boolean activePlayerCannotMove() {
        for (ArrayList<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece != null && piece.getIsWhite() == whiteToMove) {
                    if (!piece.findAvailableMoves().isEmpty()) return false;
                }
            }
        }
        return true;
    }

    private void undoMove() {
        // called after testing for check
    }

    // chess utils

    private void playMove(Move move) {
        // NOTE: The move `move` is assumed to be valid
        // this is also triggered when socket message from other player is sent
        Piece piece = move.getPiece();
        Square square = move.getSquare();
        MoveType type = move.getType();

        board.get(piece.getSquare().getRank()).set(piece.getSquare().getFile(), null);
        board.get(square.getRank()).set(square.getFile(), piece);
        piece.setSquare(square);

        recordedMoves.add(move);
        whiteToMove = !whiteToMove;

        if (type != MoveType.normal && type != MoveType.capture) {
            piece.makeSpecialMove(move);
        }

        // check for pawn promotion (show modal)
        if (piece instanceof Pawn && square.getRank() == (piece.getIsWhite() ? 0 : 7)) {
            // TODO
        }

        // check for end of game
        if (checkForCheckmate()) {
            // TODO
            return;
        }
        if (checkForStalemate()) {
            // TODO
            return;
        }
        if (checkForThreefoldRepetition()) {
            // TODO
            return;
        }
        if (checkForFiftyMoveRule()) {
            // TODO
            return;
        }

        // update game flags based on move
        updateCastlingAvailability();
        updateEnPassantSquare();
        updateThreefoldRepetition();
        updateFiftyMoveRule();

        // redraw canvas after all computation is done
        chessCanvas.redrawSquare(square);
    }

    private boolean checkForCheckmate() {
        return activePlayerCannotMove() && activeKingInCheck();
    }

    private boolean checkForStalemate() {
        return activePlayerCannotMove() && !activeKingInCheck(); // currently this method call is redundant since checkForCheckmate is called before this
    }

    private boolean checkForThreefoldRepetition() {
        // TODO
        return false;
    }

    private boolean checkForFiftyMoveRule() {
        // TODO
        return false;
    }

    private void updateCastlingAvailability() {

    }

    private void updateEnPassantSquare() {

    }

    private void updateThreefoldRepetition() {

    }

    private void updateFiftyMoveRule() {

    }

    // misc tasks

    public String toPgn() {
        // TODO
        // convert the current game to a string with the pgn notation
        return "";
    }

    // getters and setters

    public Canvas getCanvas() {
        return chessCanvas.getCanvas();
    }

    public boolean getWhiteCanCastleKingside() {
        return whiteCanCastleKingside;
    }

    public boolean getWhiteCanCastleQueenside() {
        return whiteCanCastleQueenside;
    }

    public boolean getBlackCanCastleKingside() {
        return blackCanCastleKingside;
    }

    public boolean getBlackCanCastleQueenside() {
        return blackCanCastleQueenside;
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }
}
