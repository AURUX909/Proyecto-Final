package controllers.interfaces;

import models.Cell;
import java.util.*;

public class MazeSolverDFS implements MazeSolver {
    @Override
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        if (dfs(grid, start.row, start.col, end, visited, path)) {
            return path;
        }
        return new ArrayList<>(); // Retornar vacío si no se encontró un camino
    }

    private boolean dfs(boolean[][] grid, int row, int col, Cell end, boolean[][] visited, List<Cell> path) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || !grid[row][col] || visited[row][col]) {
            return false;
        }
        if (row == end.row && col == end.col) {
            path.add(new Cell(row, col));
            return true;
        }

        visited[row][col] = true;
        path.add(new Cell(row, col));

        // Explorar vecinos: Arriba, derecha, abajo, izquierda
        if (dfs(grid, row - 1, col, end, visited, path) || 
            dfs(grid, row, col + 1, end, visited, path) ||
            dfs(grid, row + 1, col, end, visited, path) ||
            dfs(grid, row, col - 1, end, visited, path)) {
            return true;
        }

        path.remove(path.size() - 1); // Retroceder si no encontró camino
        return false;
    }
}