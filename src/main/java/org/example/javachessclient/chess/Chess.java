package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.example.javachessclient.chess.models.pieces.*;
import org.example.javachessclient.chess.models.specialmoves.SpecialMovesUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chess {
    public static final double boardSize = 800;
    public static final double squareSize = boardSize / 8;
    public static final String startingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Pattern fenRegexPattern = Pattern.compile("(?<piecePlacement>[pnbrqkPNBRQK1-8/]+) (?<activeColor>w|b) (?<cannotCastle>-)?(?<wck>K)?(?<wcq>Q)?(?<bck>k)?(?<bcq>q)? (?<enPassantSquare>-|[a-h][1-8]) (?<halfmoveClock>\\d+) (?<fullmoveNumber>\\d+)");
    public static final double pieceIconSize = 50; // each piece image is 50x50 px
    public static final Color whiteColor = Color.valueOf("#dddddd");
    public static final Color blackColor = Color.valueOf("#c4c4c4");

    private Canvas canvas;
    private Board board;
    private ArrayList<Move> record;
    private boolean whiteToMove;
    private boolean whiteCanCastleKingside;
    private boolean whiteCanCastleQueenside;
    private boolean blackCanCastleKingside;
    private boolean blackCanCastleQueenside;
    private Square enPassantSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    private Square selectedSquare;

    public Chess() {
        canvas = new Canvas(boardSize, boardSize);
        try {
            loadFEN(startingFEN);
        } catch (ParseException e) {
            System.out.println("Chess: failed to parse starting position: " + e.getMessage());
        }
        redrawBoard();
        setupBindings();
    }

    private void loadFEN(String fen) throws ParseException {
        Matcher matcher = fenRegexPattern.matcher(fen);
        if (!matcher.find()) {
            throw new ParseException("Invalid FEN string", 0);
        }

        // piece placement
        board = new Board();
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
                            piece = new Pawn(board, square, isWhite);
                            break;
                        case 'n':
                            piece = new Knight(board, square, isWhite);
                            break;
                        case 'b':
                            piece = new Bishop(board, square, isWhite);
                            break;
                        case 'r':
                            piece = new Rook(board, square, isWhite);
                            break;
                        case 'q':
                            piece = new Queen(board, square, isWhite);
                            break;
                        case 'k':
                            piece = new King(board, square, isWhite);
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

    private void redrawBoard() {
        for (int rank = 0; rank < 8; ++rank) {
            for (int file = 0; file < 8; ++file) {
                canvasRedrawSquare(new Square(file, rank));
            }
        }
    }

    private void setupBindings() {
        canvas.setOnMousePressed(event -> {
            Square square = new Square((int) (event.getX() / squareSize), (int) (event.getY() / squareSize));
            if (selectedSquare == null) {
                // select first piece
                selectedSquare = square;
                canvasHighlightSelectedSquare();
            } else {
                // select second piece
                try {
                    // determine which piece is moving where based on active side
                    Piece firstPiece = board.pieceAt(selectedSquare);
                    Piece secondPiece = board.pieceAt(square);
                    Piece fromPiece;
                    Square toSquare;
                    if (firstPiece != null && firstPiece.getIsWhite() == whiteToMove) {
                        // moving from first selected square
                        fromPiece = firstPiece;
                        toSquare = square;
                    } else if (secondPiece != null && secondPiece.getIsWhite() == whiteToMove) {
                        // moving from second selected square
                        fromPiece = secondPiece;
                        toSquare = selectedSquare;
                    } else {
                        throw new InvalidMoveException();
                    }

                    movePiece(fromPiece, toSquare);
                } catch (InvalidMoveException e) {
                    // do nothing
                } finally {
                    canvasRedrawSquare(selectedSquare); // also clears the outline of the first selected square
                    selectedSquare = null;
                }
            }
        });
    }

    private void movePiece(Piece piece, Square toSquare) {
        // this is also triggered when socket message from other player is sent
        // TODO: check for leaving king in check
        // TODO: after a move, check for checkmate
        SpecialMovesUtil.MoveType specialMoveType = SpecialMovesUtil.checkSpecialMove(this, piece, toSquare);
        if (piece.canMoveTo(toSquare) || specialMoveType != null) {
            if (specialMoveType != null) {
                SpecialMovesUtil.specialEffect(chess, piece, toSquare);
            }

            board.get(piece.getSquare().getRank()).set(piece.getSquare().getFile(), null);
            board.get(toSquare.getRank()).set(toSquare.getFile(), piece);
            piece.setSquare(toSquare);
            canvasRedrawSquare(toSquare);
            whiteToMove = !whiteToMove;

            checkForEnd();
        } else {
            throw new InvalidMoveException();
        }
    }

    private boolean kingIsInCheck() {
        // check if the active player's king is in check
        for (ArrayList<Piece>)
    }

    private void checkForEnd() {
        // TODO
        // check for checkmate, stalemate, threefold repetition, 50 move rule
        for (ArrayList<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece instanceof King && piece.getIsWhite() == whiteToMove) {
                    // check if this piece has anywhere to move / anything can block this
                }
            }
        }
    }

    private Piece findActiveKing() {
        // find the active player's king
    }

    // canvas utils

    private void canvasHighlightSelectedSquare() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.strokeRect(selectedSquare.getFile() * squareSize + 1, selectedSquare.getRank() * squareSize + 1, squareSize - 2, squareSize - 2);
    }

    private void canvasRedrawSquare(Square square) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(square.isWhite() ? whiteColor : blackColor);
        context.fillRect(square.getFile() * squareSize, square.getRank() * squareSize, squareSize, squareSize);
        Piece piece = board.pieceAt(square);
        if (piece != null) {
            Image pieceIcon = new Image(getClass().getResourceAsStream(piece.getIconFilePath()));
            double x = (square.getFile() + 0.5) * squareSize - pieceIconSize / 2;
            double y = (square.getRank() + 0.5) * squareSize - pieceIconSize / 2;
            context.drawImage(pieceIcon, x, y);
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
        return canvas;
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
