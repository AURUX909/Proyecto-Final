package models;

import java.util.Arrays;
import java.util.List;

public class Maze {
    private boolean[][] grid;

    // Constructor para inicializar un laberinto de tamaño específico
    public Maze(int size) {
        grid = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], true); // Inicializa todas las celdas como transitables
        }
    }

    // Constructor para inicializar un laberinto con una cuadrícula predefinida
    public Maze(boolean[][] predefinedGrid) {
        this.grid = predefinedGrid;
    }

    // Método para obtener la cuadrícula del laberinto
    public boolean[][] getGrid() {
        return grid;
    }

    // Método para imprimir el laberinto en la consola
    public void printMaze() {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? " - " : " * "); // Transitables (-) y barreras (*)
            }
            System.out.println();
        }
    }

    // Método para imprimir el laberinto con el camino resaltado
    public void printMazeSolver(List<Cell> path) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Cell cell = new Cell(i, j);
                if (path.contains(cell)) {
                    System.out.print(" > "); // Camino encontrado
                } else if (grid[i][j]) {
                    System.out.print(" - "); // Celda transitable
                } else {
                    System.out.print(" * "); // Barrera
                }
            }
            System.out.println();
        }
    }
}