package chess;

public class ChessPosition {
    private final int row, col;
    public ChessPosition(int row, int col) { this.row = row; this.col = col; }
    public int getRow() { return row; }
    public int getColumn() { return col; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPosition)) return false;
        ChessPosition p = (ChessPosition) o;
        return row == p.row && col == p.col;
    }
    @Override public int hashCode() { return 31 * row + col; }
}
