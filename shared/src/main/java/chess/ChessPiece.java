package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        if (type == PieceType.KING)   return kingMoves(board, pos);
        if (type == PieceType.QUEEN)  return slidingMoves(board, pos, true, true);
        if (type == PieceType.ROOK)   return slidingMoves(board, pos, true, false);
        if (type == PieceType.BISHOP) return slidingMoves(board, pos, false, true);
        if (type == PieceType.KNIGHT) return knightMoves(board, pos);
        if (type == PieceType.PAWN)   return pawnMoves(board, pos);
        return new ArrayList<>();
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition p) {
        List<ChessMove> m = new ArrayList<>();
        int r0 = p.getRow(), c0 = p.getColumn();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int r = r0 + dr, c = c0 + dc;
                if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                    ChessPiece t = board.getPiece(new ChessPosition(r, c));
                    if (t == null || t.getTeamColor() != color) {
                        m.add(new ChessMove(p, new ChessPosition(r, c), null));
                    }
                }
            }
        }
        return m;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition p) {
        List<ChessMove> m = new ArrayList<>();
        int[][] d = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
        for (int[] delta : d) {
            int r = p.getRow() + delta[0], c = p.getColumn() + delta[1];
            if (r>=1 && r<=8 && c>=1 && c<=8) {
                ChessPiece t = board.getPiece(new ChessPosition(r,c));
                if (t == null || t.getTeamColor() != color) {
                    m.add(new ChessMove(p, new ChessPosition(r,c), null));
                }
            }
        }
        return m;
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition p,
                                               boolean straight, boolean diag) {
        List<ChessMove> m = new ArrayList<>();
        int[][] dirs = {
                {1,0},{-1,0},{0,1},{0,-1},
                {1,1},{1,-1},{-1,1},{-1,-1}
        };
        for (int[] d : dirs) {
            boolean isStra = (d[0]==0 || d[1]==0);
            boolean isDiag = (Math.abs(d[0])==Math.abs(d[1]));
            if ((isStra && !straight) || (isDiag && !diag)) continue;
            int r = p.getRow(), c = p.getColumn();
            while (true) {
                r += d[0]; c += d[1];
                if (r<1||r>8||c<1||c>8) break;
                ChessPosition np = new ChessPosition(r,c);
                ChessPiece t = board.getPiece(np);
                if (t == null) {
                    m.add(new ChessMove(p, np, null));
                } else {
                    if (t.getTeamColor() != color) {
                        m.add(new ChessMove(p, np, null));
                    }
                    break;
                }
            }
        }
        return m;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition p) {
        List<ChessMove> m = new ArrayList<>();
        int dir = (color == ChessGame.TeamColor.WHITE ? 1 : -1);
        int r = p.getRow(), c = p.getColumn();
        ChessPosition one = new ChessPosition(r+dir, c);
        if (r+dir>=1 && r+dir<=8 && board.getPiece(one)==null) {
            addPawn(m, p, one);
            if ((color==ChessGame.TeamColor.WHITE && r==2)
                    || (color==ChessGame.TeamColor.BLACK && r==7)) {
                ChessPosition two = new ChessPosition(r+2*dir, c);
                if (board.getPiece(two)==null) {
                    m.add(new ChessMove(p, two, null));
                }
            }
        }
        for (int dc : new int[]{-1,1}) {
            int rr = r+dir, cc = c+dc;
            if (rr>=1 && rr<=8 && cc>=1 && cc<=8) {
                ChessPosition cp = new ChessPosition(rr,cc);
                ChessPiece t = board.getPiece(cp);
                if (t!=null && t.getTeamColor()!=color) {
                    addPawn(m, p, cp);
                }
            }
        }
        return m;
    }

    private void addPawn(Collection<ChessMove> m, ChessPosition from, ChessPosition to) {
        int end = to.getRow();
        boolean promo = (color==ChessGame.TeamColor.WHITE && end==8)
                || (color==ChessGame.TeamColor.BLACK && end==1);
        if (!promo) {
            m.add(new ChessMove(from, to, null));
        } else {
            for (PieceType pt : new PieceType[]{
                    PieceType.QUEEN, PieceType.ROOK,
                    PieceType.BISHOP, PieceType.KNIGHT}) {
                m.add(new ChessMove(from, to, pt));
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
        return Objects.hash(color, type);
    }
}
