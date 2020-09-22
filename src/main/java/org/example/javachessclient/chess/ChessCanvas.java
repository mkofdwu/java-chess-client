package org.example.javachessclient.chess;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.ArrayList;

public class ChessCanvas {
    public static final double boardSize = 800;
    public static final double squareSize = boardSize / 8;
    public static final double pieceIconSize = 50; // each piece image is 50x50 px
    public static final Color whiteColor = Color.valueOf("#dddddd");
    public static final Color blackColor = Color.valueOf("#c4c4c4");

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

    private void setupBindings() {
        canvas.setOnMousePressed(event -> {
            Square square = new Square((int) (event.getX() / squareSize), (int) (event.getY() / squareSize));
            chess.onSquareClicked(square);
        });
    }

    public void highlightSquare(Square square) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.strokeRect(square.getFile() * squareSize + 1, square.getRank() * squareSize + 1, squareSize - 2, squareSize - 2);
    }

    public void highlightAvailableMoves(ArrayList<Move> availableMoves) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.valueOf("#ff0000aa")); // temp FIXME
        for (Move move : availableMoves) {
            int file = move.getSquare().getFile();
            int rank = move.getSquare().getRank();
            // TEMP
            context.fillOval(rank * squareSize + 10, file * squareSize + 10, squareSize - 20, squareSize - 20);
        }
    }

    public void redrawBoard() {
        for (int rank = 0; rank < 8; ++rank) {
            for (int file = 0; file < 8; ++file) {
                redrawSquare(new Square(file, rank));
            }
        }
    }

    public void redrawSquare(Square square) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(square.isWhite() ? whiteColor : blackColor);
        context.fillRect(square.getFile() * squareSize, square.getRank() * squareSize, squareSize, squareSize);
        Piece piece = chess.pieceAt(square);
        if (piece != null) {
            Image pieceIcon = new Image(getClass().getResourceAsStream(piece.getIconFilePath()));
            double x = (square.getFile() + 0.5) * squareSize - pieceIconSize / 2;
            double y = (square.getRank() + 0.5) * squareSize - pieceIconSize / 2;
            context.drawImage(pieceIcon, x, y);
        }
    }
}
