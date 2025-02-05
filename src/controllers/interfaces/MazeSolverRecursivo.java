package controllers.interfaces;

import models.Cell;

import java.util.*;

public class MazeSolverRecursivo implements MazeSolver {
    @Override
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        Set<Cell> visitadas = new HashSet<>();
        if (grid == null || grid.length == 0 || !esValida(grid, start) || !esValida(grid, end)) {
            return path;
        }
        if (findPath(grid, start.row, start.col, end, path, visitadas)) {
            Collections.reverse(path); // Asegurar el orden correcto
            return path;
        }
        return new ArrayList<>();
    }

    private boolean esValida(boolean[][] grid, Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }

    private boolean findPath(boolean[][] grid, int row, int col, Cell end, List<Cell> path, Set<Cell> visitadas) {
        Cell current = new Cell(row, col);
        if (!esValida(grid, current) || visitadas.contains(current)) {
            return false;
        }
        visitadas.add(current);
        path.add(current);
        if (current.equals(end)) {
            return true;
        }
        boolean found =
            findPath(grid, row + 1, col, end, path, visitadas) ||
            findPath(grid, row - 1, col, end, path, visitadas) ||
            findPath(grid, row, col + 1, end, path, visitadas) ||
            findPath(grid, row, col - 1, end, path, visitadas);
        if (!found) {
            path.remove(current);
            visitadas.remove(current);
        }
        return found;
    }
}