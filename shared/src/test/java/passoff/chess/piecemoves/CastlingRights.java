package chess;

/**
 * Tracks whether each rook and king have moved yet,
 * so we know when castling is allowed.
 */
public class CastlingRights {
    public boolean whiteKingMoved = false;
    public boolean whiteKingRookMoved = false;
    public boolean whiteQueenRookMoved = false;
    public boolean blackKingMoved = false;
    public boolean blackKingRookMoved = false;
    public boolean blackQueenRookMoved = false;
}
.
