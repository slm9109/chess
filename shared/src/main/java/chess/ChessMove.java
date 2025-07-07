package chess;

import java.util.Objects;

public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promotion = promotionPiece;
    }

    public ChessPosition getStartPosition() {
        return start;
    }

    public ChessPosition getEndPosition() {
        return end;
    }

    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMove)) return false;
        ChessMove m = (ChessMove) o;
        return Objects.equals(start, m.start)
                && Objects.equals(end, m.end)
                && promotion == m.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotion);
    }
}


