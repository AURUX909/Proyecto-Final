package controllers.interfaces;

import models.Cell;

import java.util.*;

public class MazeSolverBFS implements MazeSolver {
    @Override
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end) {
        if (grid == null || grid.length == 0 || !esValida(grid, start) || !esValida(grid, end)) {
            return new ArrayList<>();
        }
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visitado = new boolean[rows][cols];
        Queue<Cell> cola = new ArrayDeque<>(); // Mejora: Usar ArrayDeque en lugar de LinkedList
        Map<Cell, Cell> padres = new HashMap<>();
        cola.offer(start);
        visitado[start.row][start.col] = true;

        int[][] direcciones = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!cola.isEmpty()) {
            Cell actual = cola.poll();
            if (actual.equals(end)) {
                return reconstruirCamino(padres, actual);
            }
            for (int[] dir : direcciones) {
                int nuevaFila = actual.row + dir[0];
                int nuevaCol = actual.col + dir[1];
                Cell vecino = new Cell(nuevaFila, nuevaCol);
                if (esValida(grid, vecino) && !visitado[nuevaFila][nuevaCol]) {
                    visitado[nuevaFila][nuevaCol] = true;
                    padres.put(vecino, actual);
                    cola.offer(vecino);
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Cell> reconstruirCamino(Map<Cell, Cell> padres, Cell end) {
        LinkedList<Cell> camino = new LinkedList<>();
        Cell actual = end;
        while (actual != null) {
            camino.addFirst(actual);
            actual = padres.get(actual);
        }
        return camino;
    }

    private boolean esValida(boolean[][] grid, Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }
}