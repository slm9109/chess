package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ChessGame {
    public enum TeamColor { WHITE, BLACK }

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        Collection<ChessMove> raw = piece.pieceMoves(board, startPosition);
        List<ChessMove> legal = new ArrayList<>();
        for (ChessMove move : raw) {
            // simulate
            ChessPosition from = move.getStartPosition();
            ChessPosition to = move.getEndPosition();
            ChessPiece moving = board.getPiece(from);
            ChessPiece captured = board.getPiece(to);
            // apply
            board.addPiece(to, moving);
            board.addPiece(from, null);
            // handle promotion simulation
            if (move.getPromotionPiece() != null) {
                board.addPiece(to, new ChessPiece(moving.getTeamColor(), move.getPromotionPiece()));
            }
            boolean leavesInCheck = isInCheck(piece.getTeamColor());
            // undo
            board.addPiece(from, moving);
            board.addPiece(to, captured);
            if (!leavesInCheck) {
                legal.add(move);
            }
        }

