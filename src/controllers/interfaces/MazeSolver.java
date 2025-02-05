package controllers.interfaces;

import java.util.List;
import models.Cell;

public interface MazeSolver {
    public List<Cell> getPath(boolean[][] grid, Cell start, Cell end);
}