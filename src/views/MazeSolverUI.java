package views;

import controllers.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import models.*;

public class MazeSolverUI extends JFrame {
    private JPanel mazePanel;
    private JTextField startRowField, startColField, endRowField, endColField;
    private JCheckBox delayCheckBox;
    private boolean[][] grid;
    private Cell startCell = null;
    private Cell endCell = null;
    private Color ORANGE_BRIGHT = new Color(255, 165, 0);

    public MazeSolverUI() {
        initializeMaze();
        buildUI();
    }

    private void initializeMaze() {
        // Ventana emergente estilizada para tamaño del laberinto
        JPanel sizePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        sizePanel.setBackground(new Color(31, 41, 55));
        JLabel rowsLabel = new JLabel("Filas:");
        rowsLabel.setForeground(Color.WHITE);
        JTextField rowsField = new JTextField("5");
        styleTextField(rowsField);
        JLabel colsLabel = new JLabel("Columnas:");
        colsLabel.setForeground(Color.WHITE);
        JTextField colsField = new JTextField("5");
        styleTextField(colsField);
        sizePanel.add(rowsLabel);
        sizePanel.add(rowsField);
        sizePanel.add(colsLabel);
        sizePanel.add(colsField);
        int result = JOptionPane.showConfirmDialog(null, sizePanel, "Configurar Laberinto", 
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            grid = new boolean[rows][cols];
            for (int i = 0; i < rows; i++) {
                Arrays.fill(grid[i], true);
            }
        } else {
            grid = new boolean[5][5]; // Tamaño por defecto
        }
    }

    private void buildUI() {
        setTitle("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(17, 24, 39));

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(31, 41, 55));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear el grid del laberinto
        createMazeGrid();

        // Crear el panel de controles
        JPanel controlPanel = createControlPanel();

        // Botón de actualizar coordenadas
        JButton updateButton = new JButton("Actualizar Coordenadas");
        updateButton.setBackground(new Color(37, 99, 235));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> resetMaze());

        // Panel de botones de algoritmos
        JPanel algorithmPanel = createAlgorithmPanel();

        // Agregar todos los componentes al panel principal
        mainPanel.add(mazePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(controlPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(updateButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(algorithmPanel);

        // Agregar el panel principal al frame
        add(new JScrollPane(mainPanel));

        // Configurar el tamaño y visualización
        setSize(800, 900);
        setLocationRelativeTo(null);
    }

    private void createMazeGrid() {
        mazePanel = new JPanel(new GridLayout(grid.length, grid[0].length, 2, 2));
        mazePanel.setBorder(BorderFactory.createLineBorder(new Color(75, 85, 99), 1));
        mazePanel.setBackground(new Color(31, 41, 55));
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setBackground(grid[i][j] ? Color.WHITE : Color.BLACK);
                JLabel label = new JLabel((i + 1) + "," + (j + 1), SwingConstants.CENTER);
                label.setForeground(Color.BLACK);
                cell.add(label);
                final int row = i;
                final int col = j;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) { // Click derecho
                            if (startCell == null) {
                                startCell = new Cell(row, col);
                                cell.setBackground(ORANGE_BRIGHT);
                                startRowField.setText(String.valueOf(row + 1));
                                startColField.setText(String.valueOf(col + 1));
                            } else if (endCell == null && !startCell.equals(new Cell(row, col))) {
                                endCell = new Cell(row, col);
                                cell.setBackground(ORANGE_BRIGHT);
                                endRowField.setText(String.valueOf(row + 1));
                                endColField.setText(String.valueOf(col + 1));
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON1) { // Click izquierdo
                            grid[row][col] = !grid[row][col];
                            cell.setBackground(grid[row][col] ? Color.WHITE : Color.BLACK);
                        }
                    }
                });
                mazePanel.add(cell);
            }
        }
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(31, 41, 55));

        // Crear campos de texto
        startRowField = createTextField("1");
        startColField = createTextField("1");
        endRowField = createTextField("5");
        endColField = createTextField("5");
        delayCheckBox = new JCheckBox("Mostrar Tiempo");
        delayCheckBox.setBackground(new Color(55, 65, 81));
        delayCheckBox.setForeground(Color.WHITE);

        // Agregar campos con etiquetas
        controlPanel.add(createLabeledField("Fila de inicio:", startRowField));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(createLabeledField("Columna de inicio:", startColField));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(createLabeledField("Fila de fin:", endRowField));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(createLabeledField("Columna de fin:", endColField));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(createLabeledField("Mostrar Tiempo:", delayCheckBox));
        return controlPanel;
    }

    private JPanel createAlgorithmPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 5, 0)); // Cambiado a 6 para incluir el nuevo botón
        panel.setBackground(new Color(31, 41, 55));
        String[] algorithms = {"BFS", "DFS", "Recursivo", "Cache", "Extra", "Reset"}; // Añadido "Extra"
        for (String algorithm : algorithms) {
            JButton button = new JButton(algorithm);
            button.setBackground(new Color(55, 65, 81));
            button.setForeground(Color.WHITE);
            switch (algorithm) {
                case "BFS" ->
                    button.addActionListener(e -> solveMaze(new MazeSolverBFS()));
                case "DFS" ->
                    button.addActionListener(e -> solveMaze(new MazeSolverDFS()));
                case "Recursivo" ->
                    button.addActionListener(e -> solveMaze(new MazeSolverRecursivo()));
                case "Cache" ->
                    button.addActionListener(e -> solveMaze(new MazeSolverCache()));
                case "Extra" -> // Nuevo botón "Extra"
                    button.addActionListener(e -> solveAllPaths());
                case "Reset" ->
                    button.addActionListener(e -> resetMaze());
            }
            panel.add(button);
        }
        return panel;
    }

    private JTextField createTextField(String defaultValue) {
        JTextField field = new JTextField(defaultValue);
        field.setBackground(new Color(55, 65, 81));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(31, 41, 55));
        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(209, 213, 219));
        label.setPreferredSize(new Dimension(120, 25));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(field);
        return panel;
    }

    private boolean isValidCell(Cell cell) {
        // Verificar si la celda está dentro de los límites del laberinto
        if (cell.row < 0 || cell.row >= grid.length || cell.col < 0 || cell.col >= grid[0].length) {
            return false;
        }
        // Verificar si la celda no está bloqueada
        return grid[cell.row][cell.col];
    }

    private void solveMaze(MazeSolver solver) {
        try {
            int startRow = Integer.parseInt(startRowField.getText()) - 1;
            int startCol = Integer.parseInt(startColField.getText()) - 1;
            int endRow = Integer.parseInt(endRowField.getText()) - 1;
            int endCol = Integer.parseInt(endColField.getText()) - 1;
            Cell start = new Cell(startRow, startCol);
            Cell end = new Cell(endRow, endCol);
            if (!isValidCell(start) || !isValidCell(end)) {
                JOptionPane.showMessageDialog(this, "Celdas inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            long startTime = System.nanoTime();
            List<Cell> path = solver.getPath(grid, start, end);
            long duration = (System.nanoTime() - startTime) / 1_000_000;
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontró un camino.", "Resultado", JOptionPane.WARNING_MESSAGE);
            } else {
                displayPath(path, solver.getClass().getSimpleName(), duration, path.size());
                JOptionPane.showMessageDialog(this, "Camino encontrado con éxito.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Error en la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPath(List<Cell> path, String algorithm, long time, int steps) {
        resetCellColors();
        Color pathColor = getPathColor(algorithm);
        long startTime = System.nanoTime();
        for (Cell cell : path) {
            JPanel cellPanel = getCellPanel(cell.row, cell.col);
            cellPanel.setBackground(pathColor);
            if (delayCheckBox.isSelected()) {
                try {
                    Thread.sleep(200);
                    mazePanel.repaint();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        highlightStartEndCells();
        showMetrics(algorithm, duration, steps);
    }

    private void solveAllPaths() {
        try {
            int startRow = Integer.parseInt(startRowField.getText()) - 1;
            int startCol = Integer.parseInt(startColField.getText()) - 1;
            int endRow = Integer.parseInt(endRowField.getText()) - 1;
            int endCol = Integer.parseInt(endColField.getText()) - 1;
            Cell start = new Cell(startRow, startCol);
            Cell end = new Cell(endRow, endCol);
            if (!isValidCell(start) || !isValidCell(end)) {
                JOptionPane.showMessageDialog(this, "Celdas inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener todos los caminos posibles
            MazeSolverExtra solver = new MazeSolverExtra();
            List<List<Cell>> allPaths = solver.getAllPaths(grid, start, end);

            if (allPaths.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún camino.", "Resultado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mostrar todos los caminos
            resetCellColors();
            for (List<Cell> path : allPaths) {
                if (path == allPaths.get(0)) {
                    // Camino óptimo (verde)
                    for (Cell cell : path) {
                        JPanel cellPanel = getCellPanel(cell.row, cell.col);
                        cellPanel.setBackground(Color.GREEN);
                    }
                } else {
                    // Caminos menos eficientes (amarillo)
                    for (Cell cell : path) {
                        JPanel cellPanel = getCellPanel(cell.row, cell.col);
                        if (cellPanel.getBackground() != Color.GREEN) {
                            cellPanel.setBackground(Color.YELLOW);
                        }
                    }
                }
            }

            // Colorear las celdas no usadas (rojo)
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    JPanel cellPanel = getCellPanel(i, j);
                    if (cellPanel.getBackground() != Color.GREEN && cellPanel.getBackground() != Color.YELLOW) {
                        cellPanel.setBackground(Color.RED);
                    }
                }
            }

            highlightStartEndCells();
            JOptionPane.showMessageDialog(this, "Todos los caminos mostrados.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Error en la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetCellColors() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                JPanel cell = getCellPanel(i, j);
                cell.setBackground(grid[i][j] ? Color.WHITE : Color.BLACK);
            }
        }
    }

    private void highlightStartEndCells() {
        if (startCell != null) {
            getCellPanel(startCell.row, startCell.col).setBackground(ORANGE_BRIGHT);
        }
        if (endCell != null) {
            getCellPanel(endCell.row, endCell.col).setBackground(ORANGE_BRIGHT);
        }
    }

    private JPanel getCellPanel(int row, int col) {
        return (JPanel) mazePanel.getComponent(row * grid[0].length + col);
    }

    private void showMetrics(String algorithm, long time, int steps) {
        JOptionPane.showMessageDialog(this,
        "Algoritmo: " + algorithm + "\n" +
        "Tiempo total: " + time + " ms\n" +
        "Pasos en el camino: " + steps,
        "Métricas de Rendimiento",
        JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetMaze() {
        initializeMaze();
        startCell = null;
        endCell = null;
        getContentPane().removeAll();
        buildUI();
        revalidate();
        repaint();
    }

    private Color getPathColor(String algorithm) {
        return switch (algorithm) {
            case "MazeSolverBFS" -> new Color(0, 200, 0);    // Verde oscuro
            case "MazeSolverDFS" -> new Color(144, 238, 144); // Verde claro
            case "MazeSolverRecursivo" -> new Color(173, 216, 230); // Azul claro
            case "MazeSolverCache" -> new Color(255, 182, 193);    // Rosa
            default -> Color.GRAY;
        };
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(55, 65, 81));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(75, 85, 99)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MazeSolverUI().setVisible(true);
        });
    }
}