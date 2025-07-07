// ChessBoard.java
package chess;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessBoard {
    private final Map<ChessPosition, ChessPiece> board;

    public ChessBoard() {
        board = new HashMap<>();
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    public void removePiece(ChessPosition position) {
        board.remove(position);
    }

    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    public Map<ChessPosition, ChessPiece> getPieces() {
        return board;
    }

    public void resetBoard() {
        board.clear();
        // Add pawns
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessPiece.PieceType.PAWN, ChessPiece.TeamColor.WHITE));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessPiece.PieceType.PAWN, ChessPiece.TeamColor.BLACK));
        }

        // Add other white pieces
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessPiece.PieceType.ROOK, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessPiece.PieceType.KNIGHT, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessPiece.PieceType.BISHOP, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessPiece.PieceType.QUEEN, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessPiece.PieceType.KING, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessPiece.PieceType.BISHOP, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessPiece.PieceType.KNIGHT, ChessPiece.TeamColor.WHITE));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessPiece.PieceType.ROOK, ChessPiece.TeamColor.WHITE));

        // Add other black pieces
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessPiece.PieceType.ROOK, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessPiece.PieceType.KNIGHT, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessPiece.PieceType.BISHOP, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessPiece.PieceType.QUEEN, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessPiece.PieceType.KING, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessPiece.PieceType.BISHOP, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessPiece.PieceType.KNIGHT, ChessPiece.TeamColor.BLACK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessPiece.PieceType.ROOK, ChessPiece.TeamColor.BLACK));
    }
}
