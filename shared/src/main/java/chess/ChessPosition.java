package chess;

import java.util.Objects;

public class ChessPosition {
    private final int row;
    private final int column;

    public ChessPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPosition)) return false;
        ChessPosition p = (ChessPosition) o;
        return row == p.row && column == p.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}

