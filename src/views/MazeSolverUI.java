package views;

import controllers.interfaces.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MazeSolverUI {
    private JFrame frame;
    private JPanel mazePanel;
    private JTextField startRowField, startColField, endRowField, endColField, delayField;
    private boolean[][] grid;

    public MazeSolverUI() {
        initializeMaze();
        buildUI();
    }

    private void initializeMaze() {
        grid = new boolean[][]{
                {true, true, true, true, true},
                {true, false, false, true, true},
                {true, false, true, true, false},
                {true, true, true, false, true},
                {false, true, true, true, true}
        };
    }

    private void buildUI() {
        frame = new JFrame("Maze Solver - Interfaz Gráfica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Panel para opciones de configuración
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new GridLayout(6, 2, 5, 5)); // Ajustado para mejor distribución
        configPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        configPanel.setBackground(Color.DARK_GRAY);

        // Etiquetas y campos de texto
        configPanel.add(createLabel("Fila de inicio:", Color.WHITE));
        startRowField = createTextField("1"); // Inicia en 1 para usuario
        configPanel.add(startRowField);

        configPanel.add(createLabel("Columna de inicio:", Color.WHITE));
        startColField = createTextField("1");
        configPanel.add(startColField);

        configPanel.add(createLabel("Fila de fin:", Color.WHITE));
        endRowField = createTextField("5");
        configPanel.add(endRowField);

        configPanel.add(createLabel("Columna de fin:", Color.WHITE));
        endColField = createTextField("5");
        configPanel.add(endColField);

        configPanel.add(createLabel("Delay (ms):", Color.WHITE));
        delayField = createTextField("200");
        configPanel.add(delayField);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 5, 5));
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton bfsButton = createStyledButton("BFS", e -> solveMaze(new MazeSolverBFS()));
        buttonPanel.add(bfsButton);

        JButton dfsButton = createStyledButton("DFS", e -> solveMaze(new MazeSolverDFS()));
        buttonPanel.add(dfsButton);

        JButton recButton = createStyledButton("Recursivo", e -> solveMaze(new MazeSolverRecursivo()));
        buttonPanel.add(recButton);

        JButton cacheButton = createStyledButton("Cache", e -> solveMaze(new MazeSolverCache()));
        buttonPanel.add(cacheButton);

        JButton resetButton = createStyledButton("Reiniciar", e -> resetMaze());
        buttonPanel.add(resetButton);

        // Agregar paneles al frame
        frame.add(configPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Panel del laberinto
        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(grid.length, grid[0].length, 1, 1));
        mazePanel.setBackground(Color.BLACK);
        updateMazePanel();

        // Centrar el laberinto
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(mazePanel);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(color);
        return label;
    }

    private JTextField createTextField(String defaultValue) {
        JTextField textField = new JTextField(defaultValue);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        return textField;
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        return button;
    }

    private void updateMazePanel() {
        mazePanel.removeAll();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                JPanel cellPanel = new JPanel(new GridBagLayout());
                cellPanel.setPreferredSize(new Dimension(40, 40)); // Tamaño reducido
                cellPanel.setBackground(grid[row][col] ? Color.WHITE : Color.BLACK);

                // Etiqueta dentro de la celda
                JLabel cellLabel = new JLabel(row + "," + col, SwingConstants.CENTER);
                cellLabel.setForeground(grid[row][col] ? Color.BLACK : Color.WHITE);
                cellPanel.add(cellLabel);

                mazePanel.add(cellPanel);
            }
        }
        mazePanel.revalidate();
        mazePanel.repaint();
    }

    private void solveMaze(MazeSolver solver) {
        try {
            int startRow = Integer.parseInt(startRowField.getText()) - 1; // Ajuste para índice 0
            int startCol = Integer.parseInt(startColField.getText()) - 1;
            int endRow = Integer.parseInt(endRowField.getText()) - 1;
            int endCol = Integer.parseInt(endColField.getText()) - 1;

            Cell start = new Cell(startRow, startCol);
            Cell end = new Cell(endRow, endCol);

            if (!esValida(start) || !esValida(end)) {
                JOptionPane.showMessageDialog(frame, "Celdas inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Cell> path = solver.getPath(grid, start, end);
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No se encontró un camino.", "Resultado", JOptionPane.WARNING_MESSAGE);
            } else {
                displayPath(path);
                JOptionPane.showMessageDialog(frame, "Camino encontrado con éxito.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error en la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean esValida(Cell cell) {
        return cell.row >= 0 && cell.row < grid.length &&
               cell.col >= 0 && cell.col < grid[0].length &&
               grid[cell.row][cell.col];
    }

    private void displayPath(List<Cell> path) {
        // Restaurar colores originales
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                JPanel cellPanel = (JPanel) mazePanel.getComponent(row * grid[0].length + col);
                cellPanel.setBackground(grid[row][col] ? Color.WHITE : Color.BLACK);
            }
        }

        // Pintar el camino de verde
        for (Cell celda : path) {
            int index = celda.row * grid[0].length + celda.col;
            if (index >= 0 && index < mazePanel.getComponentCount()) {
                JPanel cellPanel = (JPanel) mazePanel.getComponent(index);
                cellPanel.setBackground(new Color(0, 200, 0)); // Verde oscuro
            }
        }
        mazePanel.revalidate();
        mazePanel.repaint();
    }

    private void resetMaze() {
        initializeMaze();
        updateMazePanel();
    }

    public static void main(String[] args) {
        new MazeSolverUI();
    }
}