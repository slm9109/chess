package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessPiece {
    public enum PieceType { KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    public PieceType getPieceType() {
        return type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        return switch (type) {
            case KING   -> kingMoves(board, pos);
            case QUEEN  -> slidingMoves(board, pos, true, true);
            case ROOK   -> slidingMoves(board, pos, true, false);
            case BISHOP -> slidingMoves(board, pos, false, true);
            case KNIGHT -> knightMoves(board, pos);
            case PAWN   -> pawnMoves(board, pos);
        };
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition p) {
        var moves = new ArrayList<ChessMove>();
        int r0 = p.getRow(), c0 = p.getColumn();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr==0 && dc==0) continue;
                int r = r0 + dr, c = c0 + dc;
                if (r>=1 && r<=8 && c>=1 && c<=8) {
                    var target = board.getPiece(new ChessPosition(r,c));
                    if (target==null || target.getTeamColor()!=color) {
                        moves.add(new ChessMove(p, new ChessPosition(r,c), null));
                    }
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition p) {
        var moves = new ArrayList<ChessMove>();
        int[][] deltas = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
        for (var d : deltas) {
            int r = p.getRow()+d[0], c = p.getColumn()+d[1];
            if (r>=1 && r<=8 && c>=1 && c<=8) {
                var t = board.getPiece(new ChessPosition(r,c));
                if (t==null || t.getTeamColor()!=color) {
                    moves.add(new ChessMove(p, new ChessPosition(r,c), null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition p,
                                               boolean straight, boolean diagonal) {
        var moves = new ArrayList<ChessMove>();
        int[][] dirs = {
                {1,0},{-1,0},{0,1},{0,-1},
                {1,1},{1,-1},{-1,1},{-1,-1}
        };
        for (var d: dirs) {
            if ((!straight && (d[0]==0 || d[1]==0)) ||
                    (!diagonal && Math.abs(d[0])==Math.abs(d[1]))) {
                continue;
            }
            int r = p.getRow(), c = p.getColumn();
            while (true) {
                r += d[0]; c += d[1];
                if (r<1||r>8||c<1||c>8) break;
                var t = board.getPiece(new ChessPosition(r,c));
                if (t==null) {
                    moves.add(new ChessMove(p, new ChessPosition(r,c), null));
                } else {
                    if (t.getTeamColor()!=color) {
                        moves.add(new ChessMove(p, new ChessPosition(r,c), null));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition p) {
        var moves = new ArrayList<ChessMove>();
        int dir = (color==ChessGame.TeamColor.WHITE ? 1 : -1);
        int r = p.getRow(), c = p.getColumn();

        // forward one
        ChessPosition one = new ChessPosition(r+dir, c);
        if (r+dir>=1 && r+dir<=8 && board.getPiece(one)==null) {
            addPawnMove(moves, p, one);
            // forward two
            if ((color==ChessGame.TeamColor.WHITE && r==2)
                    || (color==ChessGame.TeamColor.BLACK && r==7)) {
                ChessPosition two = new ChessPosition(r+2*dir, c);
                if (board.getPiece(two)==null) {
                    moves.add(new ChessMove(p, two, null));
                }
            }
        }
        // captures
        for (int dc : new int[]{-1,1}) {
            int cc = c+dc, rr = r+dir;
            if (rr>=1 && rr<=8 && cc>=1 && cc<=8) {
                var t = board.getPiece(new ChessPosition(rr,cc));
                if (t!=null && t.getTeamColor()!=color) {
                    addPawnMove(moves, p, new ChessPosition(rr,cc));
                }
            }
        }
        return moves;
    }

    private void addPawnMove(Collection<ChessMove> moves, ChessPosition from, ChessPosition to) {
        int endRow = to.getRow();
        boolean promote = (endRow==8 && color==ChessGame.TeamColor.WHITE)
                || (endRow==1 && color==ChessGame.TeamColor.BLACK);
        if (!promote) {
            moves.add(new ChessMove(from, to, null));
        } else {
            for (var pt : new ChessPiece.PieceType[]{
                    PieceType.QUEEN, PieceType.ROOK,
                    PieceType.BISHOP, PieceType.KNIGHT}) {
                moves.add(new ChessMove(from, to, pt));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof ChessPiece)) return false;
        ChessPiece p = (ChessPiece)o;
        return color==p.color && type==p.type;
    }

    @Override
    public int hashCode() {
        return color.hashCode()*31 + type.hashCode();
    }
}
