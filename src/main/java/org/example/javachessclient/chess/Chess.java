package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
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

    public Piece pieceAt(Square square) {
        return board.get(square.getRank()).get(square.getFile())
    }

    public ArrayList<Square> diagonalSquares(Square square, boolean pieceIsWhite) {
        // from initial square
        ArrayList<Square> availableSquares = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        for (int fileDir : new int[]{-1, 1}) {
            for (int rankDir : new int[]{-1, 1}) {
                squaresInDir(pieceIsWhite, availableSquares, fromFile, fromRank, fileDir, rankDir);
            }
        }
        return availableSquares;
    }

    public ArrayList<Square> lineSquares(Square square, boolean pieceIsWhite) {
        // returns line squares the piece can move to. pieceIsWhite is the color of the piece with line reaches
        ArrayList<Square> availableSquares = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        for (int[] dir : new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}) {
            int fileDir = dir[0];
            int rankDir = dir[1];
            squaresInDir(pieceIsWhite, availableSquares, fromFile, fromRank, fileDir, rankDir);
        }
        return availableSquares;
    }

    private void squaresInDir(boolean pieceIsWhite, ArrayList<Square> availableSquares, int fromFile, int fromRank, int fileDir, int rankDir) {
        // automatically refractored
        for (int file = fromFile + fileDir, rank = fromRank + rankDir; 0 <= file && file < 8 && 0 <= rank && rank < 8; file += fileDir, rank += rankDir) {
            Piece landingPiece = board.get(file).get(rank);
            if (landingPiece == null) {
                availableSquares.add(new Square(file, rank));
            } else if (landingPiece.getIsWhite() == pieceIsWhite) {
                break;
            } else {
                availableSquares.add(new Square(file, rank));
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
        // TODO: check if active player is in check
        for (Piece piece : )
        if (whiteToMove)
    }

    private void undoMove() {
        // called after testing for check
    }

    // chess utils

    private void playMove(Move move) {
        // NOTE: The move `move` is assumed to be valid
        // this is also triggered when socket message from other player is sent
        if (piece.canMoveTo(toSquare) /* || specialMoveType != null */) {
//            if (specialMoveType != null) {
//                SpecialMoves.specialEffect(chess, piece, toSquare);
//            }

            board.get(piece.getSquare().getRank()).set(piece.getSquare().getFile(), null);
            board.get(toSquare.getRank()).set(toSquare.getFile(), piece);
            piece.setSquare(toSquare);
            recordedMoves.add(new Move(piece, toSquare, ));
            chessCanvas.redrawSquare(toSquare);
            whiteToMove = !whiteToMove;

            // TODO check for checkmate, stalemate & other end of game
            // TODO update game flags based on move
        } else {
            throw new InvalidMoveException();
        }
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

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public boolean getWhiteCanCastleKingside() {
        return whiteCanCastleKingside;
    }

    public void setWhiteCanCastleKingside(boolean whiteCanCastleKingside) {
        this.whiteCanCastleKingside = whiteCanCastleKingside;
    }

    public boolean getWhiteCanCastleQueenside() {
        return whiteCanCastleQueenside;
    }

    public void setWhiteCanCastleQueenside(boolean whiteCanCastleQueenside) {
        this.whiteCanCastleQueenside = whiteCanCastleQueenside;
    }

    public boolean getBlackCanCastleKingside() {
        return blackCanCastleKingside;
    }

    public void setBlackCanCastleKingside(boolean blackCanCastleKingside) {
        this.blackCanCastleKingside = blackCanCastleKingside;
    }

    public boolean getBlackCanCastleQueenside() {
        return blackCanCastleQueenside;
    }

    public void setBlackCanCastleQueenside(boolean blackCanCastleQueenside) {
        this.blackCanCastleQueenside = blackCanCastleQueenside;
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }
}
