package org.example.javachessclient.chess;

import org.example.javachessclient.Store;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.*;
import org.example.javachessclient.models.PastGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotationParser {
    public static final Pattern fenRegexPattern = Pattern.compile("(?<piecePlacement>[pnbrqkPNBRQK1-8/]+) (?<activeColor>w|b) (?<cannotCastle>-)?(?<wck>K)?(?<wcq>Q)?(?<bck>k)?(?<bcq>q)? (?<enPassantSquare>-|[a-h][1-8]) (?<halfmoveClock>\\d+) (?<fullmoveNumber>\\d+)");

    private final Chess chess;

    public NotationParser(Chess chess) {
        this.chess = chess;
    }

    public void loadFEN(String fen) throws ParseException {
        Matcher matcher = fenRegexPattern.matcher(fen);
        if (!matcher.find()) {
            throw new ParseException("Invalid FEN string", 0);
        }

        // piece placement
        List<List<Piece>> board = new ArrayList<>();
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
                            piece = new Pawn(chess, square, isWhite);
                            break;
                        case 'n':
                            piece = new Knight(chess, square, isWhite);
                            break;
                        case 'b':
                            piece = new Bishop(chess, square, isWhite);
                            break;
                        case 'r':
                            piece = new Rook(chess, square, isWhite);
                            break;
                        case 'q':
                            piece = new Queen(chess, square, isWhite);
                            break;
                        case 'k':
                            piece = new King(chess, square, isWhite);
                            break;
                        default:
                            throw new ParseException("Invalid piece type in FEN string: " + c, i);
                    }
                    board.get(board.size() - 1).add(piece);
                }
            }
        }
        chess.setBoard(board);

        // active color
        chess.setWhiteToMove(matcher.group("activeColor").equals("w"));

        FlagsHandler flags = chess.getFlagsHandler();

        // castling availability
        if (matcher.group("cannotCastle") != null) {
            flags.setWhiteCanCastleKingside(false);
            flags.setWhiteCanCastleQueenside(false);
            flags.setBlackCanCastleKingside(false);
            flags.setBlackCanCastleQueenside(false);
        } else {
            flags.setWhiteCanCastleKingside(matcher.group("wck") != null);
            flags.setWhiteCanCastleQueenside(matcher.group("wcq") != null);
            flags.setBlackCanCastleKingside(matcher.group("bck") != null);
            flags.setBlackCanCastleQueenside(matcher.group("bcq") != null);
        }

        // en passant square
        String enPassantSquareString = matcher.group("enPassantSquare");
        if (!enPassantSquareString.equals("-")) {
            flags.setEnPassantSquare(new Square(enPassantSquareString));
        }

        // halfmove clock
        flags.setHalfmoveClock(Integer.parseInt(matcher.group("halfmoveClock")));

        // fullmove number
        flags.setFullmoveNumber(Integer.parseInt(matcher.group("fullmoveNumber")));
    }

    public String toFEN() {
        // convert the current position to FEN
        StringBuilder fen = new StringBuilder();
        int emptySpaces = 0;
        List<List<Piece>> board = chess.getBoard();
        for (int i = 0; i < board.size(); ++i) {
            List<Piece> rankList = board.get(i);
            for (Piece piece : rankList) {
                if (piece == null) emptySpaces++;
                else {
                    if (emptySpaces > 0) {
                        fen.append(emptySpaces);
                        emptySpaces = 0;
                    }
                    if (piece.getClass() == Pawn.class) {
                        fen.append(piece.getIsWhite() ? 'P' : 'p');
                    } else if (piece.getClass() == Knight.class) {
                        fen.append(piece.getIsWhite() ? 'N' : 'n');
                    } else if (piece.getClass() == Bishop.class) {
                        fen.append(piece.getIsWhite() ? 'B' : 'b');
                    } else if (piece.getClass() == Rook.class) {
                        fen.append(piece.getIsWhite() ? 'R' : 'r');
                    } else if (piece.getClass() == Queen.class) {
                        fen.append(piece.getIsWhite() ? 'Q' : 'q');
                    } else if (piece.getClass() == King.class) {
                        fen.append(piece.getIsWhite() ? 'K' : 'k');
                    }
                }
            }
            if (emptySpaces > 0) {
                fen.append(emptySpaces);
                emptySpaces = 0;
            }
            if (i < board.size() - 1) fen.append('/');
        }

        fen.append(chess.getWhiteToMove() ? " w " : " b ");

        FlagsHandler flags = chess.getFlagsHandler();

        if (!flags.getWhiteCanCastleKingside()
                && !flags.getWhiteCanCastleQueenside()
                && !flags.getBlackCanCastleKingside()
                && !flags.getBlackCanCastleQueenside()) {
            fen.append('-');
        } else {
            if (flags.getWhiteCanCastleKingside()) fen.append('K');
            if (flags.getWhiteCanCastleQueenside()) fen.append('Q');
            if (flags.getBlackCanCastleKingside()) fen.append('k');
            if (flags.getBlackCanCastleQueenside()) fen.append('q');
        }

        fen.append(" " + (flags.getEnPassantSquare() == null ? '-' : flags.getEnPassantSquare().toString()));
        fen.append(" " + flags.getHalfmoveClock() + " " + flags.getFullmoveNumber());

        return fen.toString();
    }

    public String toPgn(UserGame userGame, PastGame game) {
        StringBuilder pgn = new StringBuilder();

        pgn.append("[Event \"Unknown\"]\n");
        pgn.append("[Site \"Unknown\"]\n");
        pgn.append("[Date \"" + new SimpleDateFormat("yyyy.MM.dd").format(game.getTimestamp()) + "\"]\n");
        pgn.append("[Round \"1\"]\n");
        UserProfile otherUserProfile = UserService.getUserProfile(userGame.getIsWhite() ? game.getBlack() : game.getWhite());
        pgn.append("[White \"" + (userGame.getIsWhite() ? Store.user.getUsername() : otherUserProfile.getUsername()) + "\"]\n");
        pgn.append("[Black \"" + (userGame.getIsWhite() ? otherUserProfile : Store.user.getUsername()) + "\"]\n");
        String result = "*";
        if (game.getResult() == 1) {
            result = "1/2-1/2";
        } else if (game.getResult() == 2) {
            result = "1-0";
        } else if (game.getResult() == 3) {
            result = "0-1";
        }
        pgn.append("[Result \"" + result + "\"]\n\n");

        for (int i = 0; i < chess.getMoves().size(); ++i) {
            Move move = chess.getMoves().get(i);
            if (move.getPiece().getIsWhite()) {
                pgn.append(i / 2 + 1 + ". " + move + " ");
            } else {
                pgn.append(move.toString() + " ");
            }
        }

        if (!result.equals("*")) {
            pgn.append(result);
        }

        return pgn.toString();
    }
}
