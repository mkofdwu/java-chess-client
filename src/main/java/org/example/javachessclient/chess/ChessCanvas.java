package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public class ChessCanvas {
    public static final double boardSize = 531;
    public static final double squareSize = boardSize / 8;
    public static final double pieceIconSize = 41; // each piece image is 41x41 px
    public static final Color whiteColor = Color.valueOf("#dddddd");
    public static final Color blackColor = Color.valueOf("#c4c4c4");

    private boolean isRotate;

    private final Chess chess;
    private final Canvas canvas;

    public ChessCanvas(Chess chess) {
        this.chess = chess;
        canvas = new Canvas(boardSize, boardSize);
        setupBindings();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private int applyRotation(int fileOrRank) {
        return isRotate ? 7 - fileOrRank : fileOrRank;
    }

    private void setupBindings() {
        canvas.setOnMousePressed(event -> {
            int rank = applyRotation((int) (event.getY() / squareSize));
            int file = applyRotation((int) (event.getX() / squareSize));
            Square square = new Square(file, rank);
            chess.onSquareClicked(square);
        });
    }

    public void rotateBoard() {
        isRotate = !isRotate;
        redrawBoard();
    }

    public void highlightSquare(Square square) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setLineWidth(2);
        context.strokeRect(applyRotation(square.getFile()) * squareSize + 2, applyRotation(square.getRank()) * squareSize + 2, squareSize - 4, squareSize - 4);
    }

    public void markAvailableMoves(ArrayList<Move> availableMoves) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.valueOf("#336a89"));
        for (Move move : availableMoves) {
            int file = applyRotation(move.getToSquare().getFile());
            int rank = applyRotation(move.getToSquare().getRank());
            context.fillOval(file * squareSize + 28, rank * squareSize + 28, 10, 10);
        }
    }

    public void redrawBoard() {
        for (int rank = 0; rank < 8; ++rank) {
            for (int file = 0; file < 8; ++file) {
                redrawSquare(new Square(applyRotation(file), applyRotation(rank)));
            }
        }
    }

    public void redrawSquare(Square square) {
        int file = applyRotation(square.getFile());
        int rank = applyRotation(square.getRank());
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(square.isWhite() ? whiteColor : blackColor);
        context.fillRect(file * squareSize, rank * squareSize, squareSize, squareSize);
        Piece piece = chess.pieceAt(square);
        if (piece != null) {
            Image pieceIcon = new Image(getClass().getResourceAsStream(piece.getIconFilePath()));
            double x = (file + 0.5) * squareSize - pieceIconSize / 2;
            double y = (rank + 0.5) * squareSize - pieceIconSize / 2;
            context.drawImage(pieceIcon, x, y);
        }
    }
}
