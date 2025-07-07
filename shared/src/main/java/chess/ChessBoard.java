package chess;

import java.util.Arrays;
import java.util.Objects;

public class ChessBoard {
    private final ChessPiece[][] grid = new ChessPiece[8][8];

    public ChessBoard() { }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;
        grid[r][c] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;
        return grid[r][c];
    }

    public void resetBoard() {
        for (ChessPiece[] row : grid) {
            Arrays.fill(row, null);
        }
        for (int col = 1; col <= 8; col++) {
            addPiece(new ChessPosition(2, col),
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col),
                    new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        ChessPiece.PieceType[] order = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(1, i+1),
                    new ChessPiece(ChessGame.TeamColor.WHITE, order[i]));
            addPiece(new ChessPosition(8, i+1),
                    new ChessPiece(ChessGame.TeamColor.BLACK, order[i]));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof ChessBoard)) return false;
        ChessBoard b = (ChessBoard)o;
        return Arrays.deepEquals(this.grid, b.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(grid));
    }
}

