package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        if (piece.getTeam() != teamTurn) return new ArrayList<>();
        return piece.pieceMoves(startPosition, board);
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (moves == null || !moves.contains(move)) {
            throw new InvalidMoveException("invalid move: " + move);
        }
        ChessPiece moving = board.getPiece(move.getStartPosition());
        board.removePiece(move.getStartPosition());
        ChessPiece placed = move.getPromotionPiece() != null
                ? new ChessPiece(teamTurn, move.getPromotionPiece())
                : moving;
        board.addPiece(move.getEndPosition(), placed);
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;
        for (Map.Entry<ChessPosition,ChessPiece> e : board.getPieces().entrySet()) {
            ChessPiece p = e.getValue();
            if (p.getType() == ChessPiece.PieceType.KING && p.getTeam() == convert(teamColor)) {
                kingPos = e.getKey();
                break;
            }
        }
        if (kingPos == null) return false;
        for (Map.Entry<ChessPosition,ChessPiece> e : board.getPieces().entrySet()) {
            ChessPiece p = e.getValue();
            if (p.getTeam() != convert(teamColor)) {
                for (ChessMove m : p.pieceMoves(e.getKey(), board)) {
                    if (m.getEndPosition().equals(kingPos)) return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;
        for (Map.Entry<ChessPosition,ChessPiece> e : board.getPieces().entrySet()) {
            ChessPiece p = e.getValue();
            if (p.getTeam() == convert(teamColor) && !validMoves(e.getKey()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        for (Map.Entry<ChessPosition,ChessPiece> e : board.getPieces().entrySet()) {
            ChessPiece p = e.getValue();
            if (p.getTeam() == convert(teamColor) && !validMoves(e.getKey()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    private ChessPiece.TeamColor convert(TeamColor tc) {
        return tc == TeamColor.WHITE
                ? ChessPiece.TeamColor.WHITE
                : ChessPiece.TeamColor.BLACK;
    }
}
