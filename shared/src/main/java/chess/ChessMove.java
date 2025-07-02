// ChessMove.java
package chess;

import java.util.Objects;

public class ChessMove {
    private final ChessPosition from;
    private final ChessPosition to;
    private final PieceType promotion;

    public ChessMove(ChessPosition from, ChessPosition to, PieceType promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public ChessMove(ChessPosition from, ChessPosition to) {
        this(from, to, null);
    }

    public ChessPosition getFrom() {
        return from;
    }

    public ChessPosition getTo() {
        return to;
    }

    public PieceType getPromotion() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMove)) return false;
        ChessMove move = (ChessMove) o;
        return Objects.equals(from, move.from)
                && Objects.equals(to, move.to)
                && promotion == move.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }
}


