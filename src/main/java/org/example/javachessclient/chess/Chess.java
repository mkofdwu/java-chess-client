package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.exceptions.InvalidBoardException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.*;
import org.example.javachessclient.chess.models.specialmoves.SpecialEffect;
import org.example.javachessclient.chess.views.PromotionOptionsModal;
import org.example.javachessclient.models.UserMoveCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Chess {
    public static final String startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final BoardPainter boardPainter;
    private final NotationParser notationParser;
    private List<List<Piece>> board;
    private final List<Move> moves;
    private boolean whiteToMove;
    private boolean whiteCanCastleKingside;
    private boolean whiteCanCastleQueenside;
    private boolean blackCanCastleKingside;
    private boolean blackCanCastleQueenside;
    private Square enPassantSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    private Square selectedSquare;
    private List<Move> availableMoves; // of the selected piece

    // for interface with the actual app
    private boolean canPlayWhite = false;
    private boolean canPlayBlack = false;
    private UserMoveCallback onUserMove;
    private int result; // 0 - nothing, 1 - draw, 2 - white wins, 3 - black wins

    public Chess() {
        boardPainter = new BoardPainter(this);
        notationParser = new NotationParser(this);
        if (Store.user.getSettings().getTheme() == 1) {
            // dark theme
            boardPainter.setWhiteColor(Color.valueOf("#4b4b4b"));
            boardPainter.setBlackColor(Color.valueOf("#323232"));
        }
        moves = new ArrayList<>();
    }

    public void loadFEN(String fen) throws ParseException {
        notationParser.loadFEN(fen);
        boardPainter.redrawBoard();
    }

    public void rotateBoard() {
        boardPainter.rotateBoard();
    }

    // player input

    public void onSquareClicked(Square square) {
        if (selectedSquare == null) {
            // select first piece
            onFirstSquareSelected(square);
        } else {
            // select second piece
            onSecondSquareSelected(square);
        }
    }

    public void onFirstSquareSelected(Square square) {
        selectedSquare = square;
        boardPainter.highlightSquare(selectedSquare);
        Piece piece = pieceAt(square);
        boolean canPlay = (whiteToMove && canPlayWhite) || (!whiteToMove && canPlayBlack);
        if (canPlay && piece != null && piece.getIsWhite() == whiteToMove) {
            availableMoves = piece.findAvailableMoves();
            boardPainter.markAvailableMoves(availableMoves);
        }
    }

    public void onSecondSquareSelected(Square square) {
        boolean canPlay = (whiteToMove && canPlayWhite) || (!whiteToMove && canPlayBlack);
        if (!canPlay) {
            boardPainter.redrawSquare(selectedSquare);
            boardPainter.highlightSquare(square);
            selectedSquare = square;
            return;
        }

        Move move = null;
        if (availableMoves == null) {
            // selected destination first then piece to move
            Piece piece = pieceAt(square);
            if (piece == null) availableMoves = new ArrayList<>();
            else {
                boardPainter.redrawSquare(selectedSquare);
                availableMoves = piece.findAvailableMoves();
                Square temp = selectedSquare;
                selectedSquare = square;
                square = temp; // `square` is basically square to move to in this situation, while selectedSquare is fromSquare
            }
        }
        // find move from selected square
        for (Move availableMove : availableMoves) {
            if (availableMove.getToSquare().equals(square)) {
                move = availableMove;
                break;
            }
        }

        if (move != null) {
            playMove(move);
            if (onUserMove != null)
                onUserMove.callback(move);
        }

        boardPainter.redrawSquare(selectedSquare); // also clears the outline of the first selected square
        for (Move availableMove : availableMoves) {
            // clear highlighted available moves
            boardPainter.redrawSquare(availableMove.getToSquare());
        }
        selectedSquare = null;
        availableMoves = null;

        if (move == null && pieceAt(square) != null && pieceAt(square).getIsWhite() == whiteToMove) {
            // invalid move, but selecting another piece
            onFirstSquareSelected(square);
        }
    }

    // board utils

    public Piece pieceAt(int file, int rank) {
        if (file < 0 || 7 < file || rank < 0 || 7 < rank) return null;
        return board.get(rank).get(file);
    }

    public Piece pieceAt(Square square) {
        return pieceAt(square.getFile(), square.getRank());
    }

    public void removeAt(int file, int rank) {
        board.get(rank).set(file, null);
    }

    public void removeAt(Square square) {
        removeAt(square.getFile(), square.getRank());
    }

    public void moveTo(Piece piece, Square square) {
        // System.out.println("moving " + piece + " to " + square);
        board.get(piece.getSquare().getRank()).set(piece.getSquare().getFile(), null);
        board.get(square.getRank()).set(square.getFile(), piece);
        piece.setSquare(square);
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
        Square fromSquare = new Square(fromFile, fromRank);
        for (int file = fromFile + fileDir, rank = fromRank + rankDir; Square.isValid(file, rank); file += fileDir, rank += rankDir) {
            Piece landingPiece = board.get(rank).get(file);
            if (landingPiece == null) {
                available.add(new Move(piece, fromSquare, new Square(file, rank), null, null));
            } else if (landingPiece.getIsWhite() == piece.getIsWhite()) {
                return;
            } else {
                available.add(new Move(piece, fromSquare, new Square(file, rank), landingPiece, null));
                return;
            }
        }
    }

    public boolean squaresClearUntil(int fromFile, int fromRank, int targetFile, int targetRank) {
        int filesMoved = targetFile - fromFile;
        int ranksMoved = targetRank - fromRank;
        int fileDir = filesMoved == 0 ? 0 : filesMoved / Math.abs(filesMoved);
        int rankDir = ranksMoved == 0 ? 0 : ranksMoved / Math.abs(ranksMoved);
        for (int file = fromFile + fileDir, rank = fromRank + rankDir; file != targetFile || rank != targetRank; file += fileDir, rank += rankDir) {
            if (board.get(rank).get(file) != null) return false;
        }
        return true;
    }

    public boolean moveLeavesKingInCheck(Move move) {
        testMove(move);
        boolean kingInCheck = kingInCheck(move.getPiece().getIsWhite());
        undoTestMove(move);
        return kingInCheck;
    }

    public boolean kingInCheck(boolean isWhite) {
        return squareIsAttacked(findKing(isWhite).getSquare(), !isWhite);
    }

    private King findKing(boolean isWhite) {
        for (List<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece instanceof King && piece.getIsWhite() == isWhite) {
                    return (King) piece;
                }
            }
        }
        throw new InvalidBoardException();
    }

    public boolean squareIsAttacked(Square square, boolean byWhite) {
        for (List<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece != null && piece.getIsWhite() == byWhite && piece.isAttackingSquare(square)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean otherPlayerCannotMove() {
        for (List<Piece> rankList : board) {
            for (Piece piece : rankList) {
                if (piece != null && piece.getIsWhite() == !whiteToMove) {
                    if (!piece.findAvailableMoves().isEmpty()) return false;
                }
            }
        }
        return true;
    }

    // actual logic

    public void playMove(Move move) {
        // NOTE: The move `move` is assumed to be valid
        // this is also triggered when socket message from other player is sent
        Piece piece = move.getPiece();
        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        SpecialEffect specialEffect = move.getSpecialEffect();

        moveTo(piece, toSquare);

        if (specialEffect != null) {
            List<Square> squaresToRedraw = specialEffect.perform();
            for (Square square : squaresToRedraw) boardPainter.redrawSquare(square);
        }

        // check for pawn promotion (show modal)
        if ((piece instanceof Pawn) && toSquare.getRank() == (piece.getIsWhite() ? 0 : 7)
                && (piece.getIsWhite() ? canPlayWhite : canPlayBlack)) {
            Store.modal.show(PromotionOptionsModal.buildModal((pieceName) -> {
                Piece newPiece;
                switch (pieceName) {
                    case "Queen":
                        newPiece = new Queen(this, toSquare, piece.getIsWhite());
                        break;
                    case "Rook":
                        newPiece = new Rook(this, toSquare, piece.getIsWhite());
                        break;
                    case "Bishop":
                        newPiece = new Bishop(this, toSquare, piece.getIsWhite());
                        break;
                    case "Knight":
                        newPiece = new Knight(this, toSquare, piece.getIsWhite());
                        break;
                    default:
                        return;
                }
                move.setPromotedPiece(newPiece);
                board.get(toSquare.getRank()).set(toSquare.getFile(), newPiece);
                boardPainter.redrawSquare(toSquare);
            }));
        }

        moves.add(move);

        // update game flags based on move
        updateCastlingAvailability();
        updateEnPassantSquare();
        updateThreefoldRepetition();
        updateFiftyMoveRule();

        if (kingInCheck(!whiteToMove)) {
            move.setChecksOpponentKing(true); // the opponent king was checked
        }

        // check for end of game
        if (otherPlayerCannotMove()) {
            if (move.getChecksOpponentKing()) {
                result = whiteToMove ? 2 : 3;
                Store.modal.showMessage("Checkmate", (whiteToMove ? "White" : "Black") + " has won the match.");
            } else {
                result = 1;
                Store.modal.showMessage("Stalemate", "It's a draw.");
            }
        } else if (checkForThreefoldRepetition()) {
            canPlayWhite = false;
            canPlayBlack = false;
            result = 1;
            Store.modal.showMessage("Threefold Repetition", "It's a draw.");
        } else if (checkForFiftyMoveRule()) {
            canPlayWhite = false;
            canPlayBlack = false;
            result = 1;
            Store.modal.showMessage("Fifty Move", "It's a draw");
        }

        // the last step of completing the move
        whiteToMove = !whiteToMove;

        // redraw canvas after all computation is done
        boardPainter.redrawSquare(fromSquare); // this is only necessary when getting moves via websockets from the other opponent
        boardPainter.redrawSquare(toSquare);
    }

    // TODO
//    public void undoMove() {
//        Move move = recordedMoves.remove(recordedMoves.size() - 1);
//        Piece piece = move.getPiece();
//        Square fromSquare = move.getFromSquare();
//        Square toSquare = move.getToSquare();
//        MoveType type = move.getType();
//
//        moveTo(piece, fromSquare);
//        if (type != MoveType.normal && type != MoveType.capture) {
//            move.getPiece().undoSpecialMove(move);
//        }
//        whiteToMove = !whiteToMove;
//        // FUTURE: undo promotion, change game flags
//        chessCanvas.redrawSquare(fromSquare);
//        chessCanvas.redrawSquare(toSquare);
//    }

    private void testMove(Move move) {
        // called when checking for check on opponent king
        Piece piece = move.getPiece();
        Square toSquare = move.getToSquare();
        SpecialEffect specialEffect = move.getSpecialEffect();

        moveTo(piece, toSquare);

        if (specialEffect != null) {
            specialEffect.perform();
        }
    }

    private void undoTestMove(Move move) {
        Piece piece = move.getPiece();
        Square fromSquare = move.getFromSquare();
        SpecialEffect specialEffect = move.getSpecialEffect();

        moveTo(piece, fromSquare);

        if (move.getCapturedPiece() != null) { // this works for en passant
            Piece capturedPiece = move.getCapturedPiece();
            board.get(capturedPiece.getSquare().getRank()).set(capturedPiece.getSquare().getFile(), capturedPiece);
        }

        if (specialEffect != null) {
            specialEffect.undo();
        }
    }

    private boolean checkForThreefoldRepetition() {
        // TODO
        return false;
    }

    private boolean checkForFiftyMoveRule() {
        return halfmoveClock >= 50;
    }

    private void updateCastlingAvailability() {
        Move lastMove = moves.get(moves.size() - 1);
        if (lastMove.getPiece() instanceof King) {
            if (whiteToMove) {
                whiteCanCastleKingside = false;
                whiteCanCastleQueenside = false;
            } else {
                blackCanCastleKingside = false;
                blackCanCastleQueenside = false;
            }
        } else if (lastMove.getPiece() instanceof Rook) {
            int fromFile = lastMove.getFromSquare().getFile();
            if (whiteToMove) {
                if (fromFile == 0) whiteCanCastleQueenside = false;
                else if (fromFile == 7) whiteCanCastleKingside = false;
            } else {
                if (fromFile == 0) blackCanCastleQueenside = false;
                else if (fromFile == 7) blackCanCastleKingside = false;
            }
        }
    }

    private void updateEnPassantSquare() {
        Move lastMove = moves.get(moves.size() - 1);
        Piece piece = lastMove.getPiece();
        int toFile = lastMove.getToSquare().getFile();
        int toRank = lastMove.getToSquare().getRank();
        boolean movedTwoSquares = Math.abs(toRank - lastMove.getFromSquare().getRank()) == 2;
        if (piece instanceof Pawn && movedTwoSquares) {
            Piece piece1 = pieceAt(toFile - 1, toRank);
            Piece piece2 = pieceAt(toFile + 1, toRank);
            if ((piece1 instanceof Pawn && piece1.getIsWhite() != piece.getIsWhite())
                    || (piece2 instanceof Pawn && piece2.getIsWhite() != piece.getIsWhite())) {
                enPassantSquare = new Square(toFile, toRank + (piece.getIsWhite() ? 1 : -1));
                return;
            }
        }
        enPassantSquare = null;
    }

    private void updateThreefoldRepetition() {
        // TODO
    }

    private void updateFiftyMoveRule() {
        Move lastMove = moves.get(moves.size() - 1);
        if (lastMove.getPiece() instanceof Pawn || lastMove.getCapturedPiece() != null) {
            halfmoveClock = 0;
        } else {
            ++halfmoveClock;
        }
    }

    // getters and setters

    public NotationParser getNotationParser() {
        return notationParser;
    }

    public Canvas getCanvas() {
        return boardPainter.getCanvas();
    }

    public List<List<Piece>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Piece>> board) {
        // only used by ChessNotationParser
        this.board = board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public boolean getWhiteToMove() {
        return whiteToMove;
    }

    public void setWhiteToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
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

    public void setCanPlayWhite(boolean canPlayWhite) {
        this.canPlayWhite = canPlayWhite;
    }

    public void setCanPlayBlack(boolean canPlayBlack) {
        this.canPlayBlack = canPlayBlack;
    }

    public void setOnUserMove(UserMoveCallback onUserMove) {
        this.onUserMove = onUserMove;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
