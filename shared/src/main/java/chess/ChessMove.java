package chess;

import java.util.Objects;

public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition from, ChessPosition to, ChessPiece.PieceType promo) {
        this.start = from;
        this.end = to;
        this.promotion = promo;
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
        ChessMove other = (ChessMove) o;
        return Objects.equals(start, other.start)
                && Objects.equals(end, other.end)
                && promotion == other.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotion);
    }
}




