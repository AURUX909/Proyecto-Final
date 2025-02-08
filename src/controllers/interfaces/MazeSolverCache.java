package controllers.interfaces;

import models.Cell;

import java.util.*;

public class MazeSolverCache implements MazeSolver {
    @Override
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end) {
        Map<Cell, Integer> cache = new HashMap<>();
        List<Cell> shortestPath = new ArrayList<>();
        findShortestPath(grid, start, end, cache, shortestPath);
        return shortestPath;
    }

    private int findShortestPath(boolean[][] grid, Cell current, Cell end, Map<Cell, Integer> cache, List<Cell> path) {
        // Verificar si la celda actual es válida
        if (!isValid(grid, current)) {
            return Integer.MAX_VALUE;
        }

        // Si llegamos al destino, retornar 0 pasos
        if (current.equals(end)) {
            path.add(current);
            return 0;
        }

        // Usar el valor almacenado en el caché si existe
        if (cache.containsKey(current)) {
            return cache.get(current);
        }

        // Agregar la celda actual al camino temporal
        path.add(current);

        // Inicializar el número mínimo de pasos como un valor grande
        int minSteps = Integer.MAX_VALUE;

        // Explorar las cuatro direcciones posibles
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : directions) {
            int newRow = current.row + dir[0];
            int newCol = current.col + dir[1];
            Cell next = new Cell(newRow, newCol);

            // Evitar ciclos revisando si la celda ya está en el camino actual
            if (path.contains(next)) {
                continue;
            }

            // Calcular recursivamente los pasos para llegar al destino desde la celda vecina
            int steps = findShortestPath(grid, next, end, cache, path);

            // Actualizar el número mínimo de pasos si encontramos un camino más corto
            if (steps != Integer.MAX_VALUE && steps + 1 < minSteps) {
                minSteps = steps + 1;
            }
        }

        // Remover la celda actual del camino temporal
        path.remove(path.size() - 1);

        // Almacenar el resultado en el caché
        cache.put(current, minSteps);

        return minSteps;
    }

    private boolean isValid(boolean[][] grid, Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }
}