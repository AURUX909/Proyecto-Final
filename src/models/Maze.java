package models;

import java.util.Arrays;
import java.util.List;

public class Maze {

    private final boolean[][] grid; // Campo declarado como final
    private final int size;
    private String[][] displayGrid;

    public Maze(int size) {
        this.size = size;
        grid = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(grid[i], true);
        }
        initializeDisplayGrid();
    }

    public Maze(boolean[][] predefinedGrid) {
        this.size = predefinedGrid.length;
        this.grid = predefinedGrid;
        initializeDisplayGrid();
    }

    private void initializeDisplayGrid() {
        displayGrid = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                displayGrid[i][j] = grid[i][j] ? " - " : " * ";
            }
        }
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public void updateMaze(Cell current, Cell start, Cell end) {
        if (current.equals(start)) {
            displayGrid[current.row][current.col] = " S ";
        } else if (current.equals(end)) {
            displayGrid[current.row][current.col] = " E ";
        } else {
            displayGrid[current.row][current.col] = " > ";
        }
        printMaze();
        try {
            Thread.sleep(200); // Pequeño retraso para animación
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printMaze() {
        System.out.println("\nEstado actual del laberinto:");
        for (String[] row : displayGrid) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    public void configureMaze(java.util.Scanner scanner) {
        System.out.println("Configure el laberinto. Ingrese coordenadas de celdas no transitables (fila, columna). Escriba -1 para terminar:");
        while (true) {
            System.out.print("Ingrese fila: ");
            int row = scanner.nextInt();
            if (row == -1) {
                break;
            }
            System.out.print("Ingrese columna: ");
            int col = scanner.nextInt();
            grid[row][col] = false;
        }
    }

    public void printMazeWithPath(List<Cell> path) {
        String[][] displayGridCopy = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                displayGridCopy[i][j] = grid[i][j] ? " - " : " * ";
            }
        }
        for (Cell cell : path) {
            displayGridCopy[cell.row][cell.col] = " = ";
        }
        for (String[] row : displayGridCopy) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}
