//package org.example.javachessclient.chess;
//
//import javafx.fxml.FXMLLoader;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import org.example.javachessclient.chess.models.*;
//
//public class SpecialMoves {
//
//    public enum MoveType {
//        WHITE_CASTLING_KINGSIDE,
//        WHITE_CASTLING_QUEENSIDE,
//        BLACK_CASTLING_KINGSIDE,
//        BLACK_CASTLING_QUEENSIDE,
//        PREVENT_CASTLING,
//        PROMOTION,
//        EN_PASSANT
//    }
//
//    private static MoveType checkForCastling(Chess chess, Piece piece, Square toSquare) {
//        // FIXME: is there a more elegant way?
//        // TODO: check for blocks
//        if (piece instanceof King) {
//            if (piece.getIsWhite()) {
//                if (toSquare.getRank() != 7) return null;
//                if (toSquare.getFile() == 6 && chess.getWhiteCanCastleKingside()) {
//                    chess.setWhiteCanCastleKingside(false);
//                    chess.setWhiteCanCastleQueenside(false);
//                    return MoveType.WHITE_CASTLING_KINGSIDE;
//                }
//                if (toSquare.getFile() == 2 && chess.getWhiteCanCastleQueenside()) {
//                    chess.setWhiteCanCastleKingside(false);
//                    chess.setWhiteCanCastleQueenside(false);
//                    return MoveType.WHITE_CASTLING_QUEENSIDE;
//                }
//                return null;
//            } else {
//                if (toSquare.getRank() != 0) return null;
//                if (toSquare.getFile() == 6 && chess.getBlackCanCastleKingside()) {
//                    chess.setBlackCanCastleKingside(false);
//                    chess.setBlackCanCastleQueenside(false);
//                    return MoveType.BLACK_CASTLING_KINGSIDE;
//                }
//                if (toSquare.getFile() == 2 && chess.getBlackCanCastleQueenside()) {
//                    chess.setBlackCanCastleKingside(false);
//                    chess.setBlackCanCastleQueenside(false);
//                    return MoveType.BLACK_CASTLING_QUEENSIDE;
//                }
//            }
//        }
//        return null;
//    }
//
//    private static MoveType checkForPreventCastling(Chess chess, Piece piece, Square toSquare) {
//        // moving king or rook
//        if (piece instanceof King) {
//            if (piece.getIsWhite()) {
//                chess.setWhiteCanCastleKingside(false);
//                chess.setWhiteCanCastleQueenside(false);
//            } else {
//                chess.setBlackCanCastleKingside(false);
//                chess.setBlackCanCastleQueenside(false);
//            }
//            return MoveType.PREVENT_CASTLING;
//        } else if (piece instanceof Rook) {
//            // moved away from one of the starting corners
//            int fromFile = piece.getSquare().getFile();
//            int fromRank = piece.getSquare().getRank();
//            if (piece.getIsWhite()) {
//                if (fromRank == 7) {
//                    if (fromFile == 0) {
//                        chess.setWhiteCanCastleQueenside(false);
//                        return MoveType.PREVENT_CASTLING;
//                    }
//                    if (fromFile == 7) {
//                        chess.setWhiteCanCastleKingside(false);
//                        return MoveType.PREVENT_CASTLING;
//                    }
//                }
//            } else {
//                if (fromRank == 0) {
//                    if (fromFile == 0) {
//                        chess.setBlackCanCastleQueenside(false);
//                        return MoveType.PREVENT_CASTLING;
//                    }
//                    if (fromFile == 7) {
//                        chess.setBlackCanCastleKingside(false);
//                        return MoveType.PREVENT_CASTLING;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private static MoveType checkForPromotion(Chess chess, Piece piece, Square toSquare) {
//        Stage promotionPieceStage = new Stage();
//        promotionPieceStage.setScene(FXMLLoader.load(SpecialMoves.class.getResource("/fxml/promotion.fxml")));
//        promotionPieceStage.setTitle("Promote pawn");
//        promotionPieceStage.initModality(Modality.WINDOW_MODAL);
//        promotionPieceStage.showAndWait();
//    }
//
//    private static MoveType checkForEnPassant(Chess chess, Piece piece, Square toSquare) {
//        Square enPassantSquare = chess.getEnPassantSquare();
//        if (enPassantSquare != null && piece instanceof Pawn && toSquare == enPassantSquare) {
//            // return piece.getSquare().getRank() == enPassantSquare.getRank() - 1 // TODO
//        }
//    }
//
//    public static MoveType checkSpecialMove(Chess chess, Piece piece, Square toSquare) {
//        MoveType castling = checkForCastling(chess, piece, toSquare);
//        if (castling != null) return castling;
//
//        MoveType preventCastling = checkForPreventCastling(chess, piece, toSquare);
//        if (preventCastling != null) return preventCastling;
//
//        MoveType promotion = checkForPromotion(chess, piece, toSquare);
//        if (promotion != null) return promotion;
//
//        checkForEnPassant(chess, piece, toSquare);
//
//        // checkForAllowEnPassant();
//
//        return null;
//    }
//
//    public static void specialEffect(Chess chess, MoveType moveType) {
//
//    }
//}
