package chess;

import java.util.Collection;

public class ChessGame {
    public enum TeamColor { WHITE, BLACK }
    public ChessGame() {}
    public TeamColor getTeamTurn() { throw new RuntimeException("Not implemented"); }
    public void setTeamTurn(TeamColor team) { throw new RuntimeException("Not implemented"); }
    public Collection<ChessMove> validMoves(ChessPosition startPosition) { throw new RuntimeException("Not implemented"); }
    public void makeMove(ChessMove move) throws InvalidMoveException { throw new RuntimeException("Not implemented"); }
    public boolean isInCheck(TeamColor teamColor) { throw new RuntimeException("Not implemented"); }
    public boolean isInCheckmate(TeamColor teamColor) { throw new RuntimeException("Not implemented"); }
    public boolean isInStalemate(TeamColor teamColor) { throw new RuntimeException("Not implemented"); }
    public void setBoard(ChessBoard board) { throw new RuntimeException("Not implemented"); }
    public ChessBoard getBoard() { throw new RuntimeException("Not implemented"); }
}
