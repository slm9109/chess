package chess;

public class ChessMove {
    private final ChessPosition start, end;
    private final ChessPiece.PieceType promotion;
    public ChessMove(ChessPosition s, ChessPosition e, ChessPiece.PieceType promo) {
        this.start = s; this.end = e; this.promotion = promo;
    }
    public ChessPosition getStartPosition() { return start; }
    public ChessPosition getEndPosition() { return end; }
    public ChessPiece.PieceType getPromotionPiece() { return promotion; }
    @Override public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof ChessMove)) return false;
        ChessMove m = (ChessMove)o;
        return start.equals(m.start) && end.equals(m.end)
                && promotion == m.promotion;
    }
    @Override public int hashCode() {
        int h = start.hashCode() * 31 + end.hashCode();
        return promotion != null ? h * 31 + promotion.hashCode() : h;
    }
}
