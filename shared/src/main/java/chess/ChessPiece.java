// ChessPiece.java
package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessPiece {
    public enum PieceType { KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }
    public enum Color { WHITE, BLACK }

    private final PieceType type;
    private final Color color;

    public ChessPiece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public List<ChessMove> pieceMoves(ChessPosition pos, ChessBoard board) {
        List<ChessMove> moves = new ArrayList<>();
        int r = pos.getRow();
        int c = pos.getCol();
        int dir = (color == Color.WHITE) ? 1 : -1;

        switch (type) {
            case PAWN:
                ChessPosition one = new ChessPosition(r + dir, c);
                if (board.isEmpty(one)) moves.add(new ChessMove(pos, one));
                if ((r == 2 && color == Color.WHITE) || (r == 7 && color == Color.BLACK)) {
                    ChessPosition two = new ChessPosition(r + 2*dir, c);
                    if (board.isEmpty(one) && board.isEmpty(two))
                        moves.add(new ChessMove(pos, two));
                }
                for (int dc : new int[]{-1,1}) {
                    ChessPosition diag = new ChessPosition(r + dir, c + dc);
                    if (board.hasEnemy(diag, color)) moves.add(new ChessMove(pos, diag));
                }
                break;
            case KNIGHT:
                int[][] km = {{1,2},{2,1},{-1,2},{-2,1},{1,-2},{2,-1},{-1,-2},{-2,-1}};
                for (int[] d : km) {
                    ChessPosition np = new ChessPosition(r + d[0], c + d[1]);
                    if (board.isEmptyOrEnemy(np, color))
                        moves.add(new ChessMove(pos, np));
                }
                break;
            case BISHOP:
                addSliding(moves, pos, board, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
                break;
            case ROOK:
                addSliding(moves, pos, board, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
                break;
            case QUEEN:
                addSliding(moves, pos, board, new int[][]{
                        {1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}
                });
                break;
            case KING:
                for (int dr = -1; dr <= 1; dr++) for (int dc2 = -1; dc2 <= 1; dc2++) {
                    if (dr==0 && dc2==0) continue;
                    ChessPosition np = new ChessPosition(r+dr, c+dc2);
                    if (board.isEmptyOrEnemy(np, color))
                        moves.add(new ChessMove(pos, np));
                }
                break;
        }
        return moves;
    }

    private void addSliding(List<ChessMove> moves, ChessPosition pos, ChessBoard board, int[][] dirs) {
        int r = pos.getRow(), c = pos.getCol();
        for (int[] d : dirs) {
            int dr = d[0], dc = d[1];
            int nr = r + dr, nc = c + dc;
            while (nr >=1 && nr<=8 && nc>=1 && nc<=8) {
                ChessPosition np = new ChessPosition(nr, nc);
                if (board.isEmpty(np)) {
                    moves.add(new ChessMove(pos, np));
                } else {
                    if (board.hasEnemy(np, color))
                        moves.add(new ChessMove(pos, np));
                    break;
                }
                nr += dr; nc += dc;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece)) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
