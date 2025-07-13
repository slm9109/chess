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
        return legal;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        // basic validations
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) throw new InvalidMoveException("No piece at start");
        if (piece.getTeamColor() != teamTurn) throw new InvalidMoveException("Wrong turn");
        Collection<ChessMove> moves = validMoves(start);
        if (moves == null || !moves.contains(move)) throw new InvalidMoveException("Illegal move");
        // execute
        ChessPiece captured = board.getPiece(end);
        // move or promotion
        if (move.getPromotionPiece() != null) {
            board.addPiece(end, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            board.addPiece(start, null);
        } else {
            board.addPiece(end, piece);
            board.addPiece(start, null);
        }
        // switch turn
        teamTurn = (teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    public boolean isInCheck(TeamColor teamColor) {
        // find king
        ChessPosition kingPos = null;
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition p = new ChessPosition(r, c);
                ChessPiece pce = board.getPiece(p);
                if (pce != null && pce.getTeamColor() == teamColor && pce.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPos = p;
                    break;
                }
            }
            if (kingPos != null) break;
        }
        if (kingPos == null) return false;
        // check threats
        TeamColor opponent = (teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition p = new ChessPosition(r, c);
                ChessPiece pce = board.getPiece(p);
                if (pce != null && pce.getTeamColor() == opponent) {
                    Collection<ChessMove> moves = pce.pieceMoves(board, p);
                    for (ChessMove m : moves) {
                        if (m.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;
        // any legal move escapes?
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition p = new ChessPosition(r, c);
                ChessPiece pce = board.getPiece(p);
                if (pce != null && pce.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(p);
                    if (moves != null && !moves.isEmpty()) return false;
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;