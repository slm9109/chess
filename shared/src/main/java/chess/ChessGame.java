package chess;

import java.util.*;
import java.util.stream.Collectors;

public class ChessGame {
    public enum TeamColor { WHITE, BLACK }

    private ChessBoard board;
    private TeamColor turn;
    private final CastlingRights castling = new CastlingRights();
    private final EnPassantTracker ep = new EnPassantTracker();

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.turn = TeamColor.WHITE;
    }

    // === Accessors ===

    public TeamColor getTeamTurn() {
        return turn;
    }

    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard b) {
        this.board = b;
        // reset special rights whenever board is directly set
        castling.whiteKingMoved = castling.blackKingMoved = false;
        castling.whiteKingRookMoved = castling.whiteQueenRookMoved =
                castling.blackKingRookMoved = castling.blackQueenRookMoved = false;
        ep.enPassantTarget = null;
        ep.vulnerable = null;
    }

    // === Core API ===

    /** Return all legal moves from startPosition, or null if empty. */
    public Collection<ChessMove> validMoves(ChessPosition start) {
        ChessPiece p = board.getPiece(start);
        if (p == null || p.getTeamColor() != turn) return null;

        // 1) get all "raw" piece moves
        Collection<ChessMove> raw = p.pieceMoves(board, start);

        // 2) add castling moves if king
        if (p.getPieceType() == ChessPiece.PieceType.KING) {
            raw.addAll(generateCastles(start));
        }

        // 3) add en passant if pawn
        if (p.getPieceType() == ChessPiece.PieceType.PAWN) {
            raw.addAll(generateEnPassant(start));
        }

        // 4) filter out any that leave king in check
        return raw.stream()
                .filter(mv -> !wouldBeInCheckAfterMove(mv))
                .collect(Collectors.toList());
    }

    /** Execute move or throw if illegal. */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // validate
        Collection<ChessMove> valids = validMoves(move.getStartPosition());
        if (valids == null || !valids.contains(move)) {
            throw new InvalidMoveException("Illegal move: " + move);
        }

        // apply move
        ChessPosition from = move.getStartPosition(), to = move.getEndPosition();
        ChessPiece moving = board.getPiece(from);

        // handle capture by en passant
        if (moving.getPieceType() == ChessPiece.PieceType.PAWN
                && ep.enPassantTarget != null
                && to.equals(ep.enPassantTarget)) {
            // pawn moved diagonally into empty square
            int dir = (turn == TeamColor.WHITE ? -1 : 1);
            ChessPosition captured = new ChessPosition(to.getRow() + dir, to.getColumn());
            board.addPiece(captured, null);
        }

        // normal capture or move
        board.addPiece(from, null);
        // promotion?
        ChessPiece.PieceType promo = move.getPromotionPiece();
        if (promo != null) {
            board.addPiece(to, new ChessPiece(turn, promo));
        } else {
            board.addPiece(to, moving);
        }

        // update castling rights
        updateCastlingRights(from, moving.getPieceType());
        // update en passant rights
        updateEnPassant(from, to, moving.getPieceType());

        // toggle turn
        turn = (turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    public boolean isInCheck(TeamColor team) {
        // find that team's king
        ChessPosition kingPos = findKing(team);
        // if any enemy move can hit it, it's check
        TeamColor enemy = opposite(team);
        for (var e : board.iterate()) {
            if (e.getValue().getTeamColor() == enemy) {
                for (ChessMove mv : e.getValue().pieceMoves(board, e.getKey())) {
                    if (mv.getEndPosition().equals(kingPos)) return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor team) {
        if (!isInCheck(team)) return false;
        // if no legal moves exist to escape
        return !hasAnyLegalMove(team);
    }

    public boolean isInStalemate(TeamColor team) {
        if (isInCheck(team)) return false;
        return !hasAnyLegalMove(team);
    }

    // === Helpers ===

    private TeamColor opposite(TeamColor c) {
        return c == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private ChessPosition findKing(TeamColor team) {
        for (var e : board.iterate()) {
            if (e.getValue().getPieceType() == ChessPiece.PieceType.KING
                    && e.getValue().getTeamColor() == team) {
                return e.getKey();
            }
        }
        throw new IllegalStateException("King missing for " + team);
    }

    private boolean hasAnyLegalMove(TeamColor team) {
        for (var e : board.iterate()) {
            if (e.getValue().getTeamColor() == team) {
                if (validMoves(e.getKey()).size() > 0) return true;
            }
        }
        return false;
    }

    private boolean wouldBeInCheckAfterMove(ChessMove mv) {
        // simulate
        ChessBoard snapshot = cloneBoard();
        ChessGame.TeamColor oldTurn = turn;
        try {
            ChessGame tmp = new ChessGame();
            tmp.board = snapshot;
            tmp.turn = oldTurn;
            tmp.castling.whiteKingMoved = castling.whiteKingMoved;
            tmp.castling.whiteKingRookMoved = castling.whiteKingRookMoved;
            tmp.castling.whiteQueenRookMoved = castling.whiteQueenRookMoved;
            tmp.castling.blackKingMoved = castling.blackKingMoved;
            tmp.castling.blackKingRookMoved = castling.blackKingRookMoved;
            tmp.castling.blackQueenRookMoved = castling.blackQueenRookMoved;
            tmp.ep.enPassantTarget = ep.enPassantTarget;
            tmp.ep.vulnerable = ep.vulnerable;
            tmp.makeMove(mv);
            return tmp.isInCheck(oldTurn);
        } catch (InvalidMoveException ex) {
            return true;
        }
    }

    private ChessBoard cloneBoard() {
        ChessBoard copy = new ChessBoard();
        for (var e : board.iterate()) {
            copy.addPiece(e.getKey(), e.getValue());
        }
        return copy;
    }

    // === Castling support ===

    private void updateCastlingRights(ChessPosition from, ChessPiece.PieceType type) {
        int r = from.getRow(), c = from.getColumn();
        if (type == ChessPiece.PieceType.KING) {
            if (turn == TeamColor.WHITE) castling.whiteKingMoved = true;
            else castling.blackKingMoved = true;
        }
        if (type == ChessPiece.PieceType.ROOK) {
            if (r == 1 && c == 1) castling.whiteQueenRookMoved = true;
            if (r == 1 && c == 8) castling.whiteKingRookMoved = true;
            if (r == 8 && c == 1) castling.blackQueenRookMoved = true;
            if (r == 8 && c == 8) castling.blackKingRookMoved = true;
        }
    }

    private Collection<ChessMove> generateCastles(ChessPosition kingPos) {
        List<ChessMove> res = new ArrayList<>();
        boolean white = (turn == TeamColor.WHITE);
        // Can't castle if king already moved or is in check
        if ((white && castling.whiteKingMoved) || (!white && castling.blackKingMoved)
                || isInCheck(turn)) return res;

        int row = white ? 1 : 8;
        // kingside
        if ((!white && !castling.blackKingRookMoved)
                || (white && !castling.whiteKingRookMoved)) {
            if (isEmpty(row,6) && isEmpty(row,7)
                    && !isSquareAttacked(row,6) && !isSquareAttacked(row,7)) {
                res.add(new ChessMove(kingPos, new ChessPosition(row,7), null));
            }
        }
        // queenside
        if ((!white && !castling.blackQueenRookMoved)
                || (white && !castling.whiteQueenRookMoved)) {
            if (isEmpty(row,2) && isEmpty(row,3) && isEmpty(row,4)
                    && !isSquareAttacked(row,3) && !isSquareAttacked(row,4)) {
                res.add(new ChessMove(kingPos, new ChessPosition(row,3), null));
            }
        }
        return res;
    }

    private boolean isEmpty(int r, int c) {
        return board.getPiece(new ChessPosition(r, c)) == null;
    }

    private boolean isSquareAttacked(int r, int c) {
        TeamColor enemy = opposite(turn);
        ChessPosition sq = new ChessPosition(r, c);
        for (var e : board.iterate()) {
            ChessPiece pc = e.getValue();
            if (pc.getTeamColor() != enemy) continue;
            for (var mv : pc.pieceMoves(board, e.getKey())) {
                if (mv.getEndPosition().equals(sq)) return true;
            }
        }
        return false;
    }

    // === En Passant support ===

    private void updateEnPassant(ChessPosition from, ChessPosition to, ChessPiece.PieceType type) {
        // reset by default
        ep.enPassantTarget = null;
        ep.vulnerable = null;
        // only if pawn moves two
        if (type == ChessPiece.PieceType.PAWN
                && Math.abs(to.getRow() - from.getRow()) == 2) {
            int midRow = (to.getRow() + from.getRow()) / 2;
            ep.enPassantTarget = new ChessPosition(midRow, to.getColumn());
            ep.vulnerable = turn;
        }
    }

    private Collection<ChessMove> generateEnPassant(ChessPosition p) {
        List<ChessMove> res = new ArrayList<>();
        if (ep.enPassantTarget == null || ep.vulnerable == opposite(turn)) return res;
        int dir = (turn == TeamColor.WHITE ? 1 : -1);
        ChessPosition target = ep.enPassantTarget;
        // only pawns adjacent horizontally
        if (p.getRow() == target.getRow()
                && Math.abs(p.getColumn() - target.getColumn()) == 1) {
            res.add(new ChessMove(p, new ChessPosition(p.getRow()+dir, target.getColumn()), null));
        }
        return res;
    }
}
// Minor comment to force a new commit
