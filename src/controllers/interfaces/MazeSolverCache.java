package controllers.interfaces;

import models.Cell;

import java.util.*;

public class MazeSolverCache implements MazeSolver {
    @Override
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end) {
        Map<Cell, Boolean> cache = new HashMap<>();
        List<Cell> path = new ArrayList<>();
        if (findPath(grid, start, end, cache, path)) {
            return path;
        }
        return new ArrayList<>();
    }

    private boolean findPath(boolean[][] grid, Cell current, Cell end, Map<Cell, Boolean> cache, List<Cell> path) {
        if (cache.containsKey(current)) {
            return cache.get(current);
        }
        if (!esValida(grid, current)) {
            cache.put(current, false);
            return false;
        }
        if (current.equals(end)) {
            path.add(current);
            cache.put(current, true);
            return true;
        }
        path.add(current);
        boolean found =
            findPath(grid, new Cell(current.row + 1, current.col), end, cache, path) ||
            findPath(grid, new Cell(current.row - 1, current.col), end, cache, path) ||
            findPath(grid, new Cell(current.row, current.col + 1), end, cache, path) ||
            findPath(grid, new Cell(current.row, current.col - 1), end, cache, path);
        if (!found) {
            path.remove(path.size() - 1);
        }
        cache.put(current, found);
        return found;
    }

    private boolean esValida(boolean[][] grid, Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }
}