package controllers.interfaces;

import java.util.ArrayList;
import java.util.List;

import models.Cell;

public class MazeSolverExtra {
    public List<List<Cell>> getAllPaths(boolean[][] grid, Cell start, Cell end) {
        List<List<Cell>> allPaths = new ArrayList<>();
        List<Cell> currentPath = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        
        // Llamada al método recursivo para encontrar todos los caminos
        findAllPaths(grid, start, end, visited, currentPath, allPaths);
        return allPaths;
    }

    private void findAllPaths(boolean[][] grid, Cell current, Cell end, boolean[][] visited,
                               List<Cell> currentPath, List<List<Cell>> allPaths) {
        // Verificar si la celda actual es válida
        if (current.row < 0 || current.row >= grid.length || current.col < 0 || current.col >= grid[0].length
                || !grid[current.row][current.col] || visited[current.row][current.col]) {
            return;
        }

        // Marcar la celda como visitada y añadirla al camino actual
        visited[current.row][current.col] = true;
        currentPath.add(current);

        // Si llegamos al destino, guardar el camino actual
        if (current.equals(end)) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            // Explorar las cuatro direcciones posibles
            findAllPaths(grid, new Cell(current.row + 1, current.col), end, visited, currentPath, allPaths); // Abajo
            findAllPaths(grid, new Cell(current.row - 1, current.col), end, visited, currentPath, allPaths); // Arriba
            findAllPaths(grid, new Cell(current.row, current.col + 1), end, visited, currentPath, allPaths); // Derecha
            findAllPaths(grid, new Cell(current.row, current.col - 1), end, visited, currentPath, allPaths); // Izquierda
        }

        // Desmarcar la celda actual y eliminarla del camino actual
        visited[current.row][current.col] = false;
        currentPath.remove(currentPath.size() - 1);
    }
}
