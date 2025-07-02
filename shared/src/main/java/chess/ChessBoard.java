// ChessBoard.java
package chess;

import java.util.HashMap;
import java.util.Map;

public class ChessBoard {
    private final Map<ChessPosition, ChessPiece> board = new HashMap<>();

    public void resetBoard() {
        board.clear();
        for (ChessPiece.Color col : ChessPiece.Color.values()) {
            int pawnRow = (col == ChessPiece.Color.WHITE) ? 2 : 7;
            int majorRow = (col == ChessPiece.Color.WHITE) ? 1 : 8;
            for (int f = 1; f <= 8; f++) {
                board.put(new ChessPosition(pawnRow, f), new ChessPiece(ChessPiece.PieceType.PAWN, col));
            }
            ChessPiece.Color c = col;
            board.put(new ChessPosition(majorRow, 1), new ChessPiece(ChessPiece.PieceType.ROOK, c));
            board.put(new ChessPosition(majorRow, 2), new ChessPiece(ChessPiece.PieceType.KNIGHT, c));
            board.put(new ChessPosition(majorRow, 3), new ChessPiece(ChessPiece.PieceType.BISHOP, c));
            board.put(new ChessPosition(majorRow, 4), new ChessPiece(ChessPiece.PieceType.QUEEN, c));
            board.put(new ChessPosition(majorRow, 5), new ChessPiece(ChessPiece.PieceType.KING, c));
            board.put(new ChessPosition(majorRow, 6), new ChessPiece(ChessPiece.PieceType.BISHOP, c));
            board.put(new ChessPosition(majorRow, 7), new ChessPiece(ChessPiece.PieceType.KNIGHT, c));
            board.put(new ChessPosition(majorRow, 8), new ChessPiece(ChessPiece.PieceType.ROOK, c));
        }
    }

    public ChessPiece get(ChessPosition pos) {
        return board.get(pos);
    }

    public void addPiece(ChessPosition pos, ChessPiece piece) {
        board.put(pos, piece);
    }

    public void removePiece(ChessPosition pos) {
        board.remove(pos);
    }

    public boolean isEmpty(ChessPosition pos) {
        return !board.containsKey(pos);
    }

    public boolean hasEnemy(ChessPosition pos, ChessPiece.Color col) {
        ChessPiece p = board.get(pos);
        return p != null && p.getColor() != col;
    }

    public boolean isEmptyOrEnemy(ChessPosition pos, ChessPiece.Color col) {
        return isEmpty(pos) || hasEnemy(pos, col);
    }
}
