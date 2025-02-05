package models;

import java.util.Objects;

public class Cell {
    public int row, col;

    public Cell(int row, int col) { // Corregido el typo "cold" -> "col"
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "Cell [row=" + row + ", col=" + col + "]";
    }
}