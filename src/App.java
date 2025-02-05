import controllers.interfaces.*;
import models.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Definición del laberinto
        boolean[][] laberinto = {
            {true, true, true, true},
            {false, true, true, true},
            {true, true, false, false},
            {true, true, true, true},
        };

        Maze maze = new Maze(laberinto);
        System.out.println("Laberinto cargado:");
        maze.printMaze();

        // Puntos de inicio y fin
        Cell start = new Cell(0, 3);
        Cell end = new Cell(3, 3);

        // Lista de solucionadores disponibles
        List<MazeSolver> soluciones = Arrays.asList(
            new MazeSolverRecursivo(),
            new MazeSolverBFS(),
            new MazeSolverDFS(),
            new MazeSolverCache()
        );

        // Menú para seleccionar el algoritmo
        try (Scanner scanner = new Scanner(System.in)) { // Uso de try-with-resources
            System.out.println("\nSelecciona el algoritmo:");
            System.out.println("1: Recursivo");
            System.out.println("2: BFS");
            System.out.println("3: DFS");
            System.out.println("4: Cache");

            int opcion = scanner.nextInt();
            if (opcion < 1 || opcion > soluciones.size()) {
                System.out.println("Opción no válida");
                return;
            }

            // Obtener el solucionador seleccionado
            MazeSolver solver = soluciones.get(opcion - 1);
            List<Cell> path = solver.getPath(laberinto, start, end);

            // Mostrar resultados
            if (path.isEmpty()) {
                System.out.println("No se encontró camino.");
            } else {
                System.out.println("Camino encontrado:");
                for (Cell cell : path) {
                    System.out.println(cell);
                }
                maze.printMazeSolver(path);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}