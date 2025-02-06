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
        if (!esValida(grid, current)) return Integer.MAX_VALUE;
        if (current.equals(end)) {
            path.add(current);
            return 0;
        }
        if (cache.containsKey(current)) return cache.get(current);

        path.add(current);
        int minSteps = Integer.MAX_VALUE;
        List<Cell> tempPath = new ArrayList<>(path);

        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        for (int[] dir : directions) {
            Cell next = new Cell(current.row + dir[0], current.col + dir[1]);
            if (path.contains(next)) continue; // Evitar ciclos
            
            int steps = findShortestPath(grid, next, end, cache, tempPath);
            if (steps != Integer.MAX_VALUE && steps + 1 < minSteps) {
                minSteps = steps + 1;
                path.clear();
                path.addAll(tempPath);
            }
            tempPath = new ArrayList<>(path);
        }

        path.remove(path.size() - 1);
        cache.put(current, minSteps);
        return minSteps;
    }

    private boolean esValida(boolean[][] grid, Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }
}