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
    private JCheckBox delayCheckBox; // CheckBox para Delay
    private boolean[][] grid;
    private Cell startCell = null;
    private Cell endCell = null;

    public MazeSolverUI() {
        initializeMaze();
        buildUI();
    }

    private void initializeMaze() {
        // Ventana emergente para configurar el tamaño del laberinto
        String inputRows = JOptionPane.showInputDialog(this, "Ingrese el número de filas:", "Configuración del Laberinto", JOptionPane.PLAIN_MESSAGE);
        String inputCols = JOptionPane.showInputDialog(this, "Ingrese el número de columnas:", "Configuración del Laberinto", JOptionPane.PLAIN_MESSAGE);

        int rows = inputRows == null || inputRows.isEmpty() ? 5 : Integer.parseInt(inputRows);
        int cols = inputCols == null || inputCols.isEmpty() ? 5 : Integer.parseInt(inputCols);

        grid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], true); // Todas las celdas son transitables inicialmente
        }
    }

    private void buildUI() {
        setTitle("Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(17, 24, 39)); // bg-gray-900

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(31, 41, 55)); // bg-gray-800
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear el grid del laberinto
        createMazeGrid();

        // Crear el panel de controles
        JPanel controlPanel = createControlPanel();

        // Botón de actualizar coordenadas
        JButton updateButton = new JButton("Actualizar Coordenadas");
        updateButton.setBackground(new Color(37, 99, 235)); // bg-blue-600
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
                        if (e.getButton() == MouseEvent.BUTTON1) { // Botón izquierdo
                            if (startCell == null) {
                                startCell = new Cell(row, col);
                                cell.setBackground(Color.BLUE);
                                startRowField.setText(String.valueOf(row + 1));
                                startColField.setText(String.valueOf(col + 1));
                            } else if (endCell == null && !startCell.equals(new Cell(row, col))) {
                                endCell = new Cell(row, col);
                                cell.setBackground(Color.BLUE);
                                endRowField.setText(String.valueOf(row + 1));
                                endColField.setText(String.valueOf(col + 1));
                            } else {
                                grid[row][col] = !grid[row][col]; // Alternar estado
                                cell.setBackground(grid[row][col] ? Color.WHITE : Color.BLACK);
                            }
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
        JPanel panel = new JPanel(new GridLayout(1, 5, 5, 0));
        panel.setBackground(new Color(31, 41, 55));

        String[] algorithms = {"BFS", "DFS", "Recursivo", "Cache", "Reset"};
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
        // Restaurar colores originales
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                JPanel cell = (JPanel) mazePanel.getComponent(row * grid[0].length + col);
                cell.setBackground(grid[row][col] ? Color.WHITE : Color.BLACK);
            }
        }

        // Pintar el camino según el algoritmo
        Color pathColor = getPathColor(algorithm);
        for (Cell cell : path) {
            JPanel cellPanel = (JPanel) mazePanel.getComponent(cell.row * grid[0].length + cell.col);
            cellPanel.setBackground(pathColor);

            if (delayCheckBox.isSelected()) {
                try {
                    Thread.sleep(200); // Retraso para visualizar paso a paso
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        mazePanel.revalidate();
        mazePanel.repaint();

        // Mostrar métricas
        JOptionPane.showMessageDialog(this,
                "Algoritmo: " + algorithm
                + "\nTiempo: " + time + "ms"
                + "\nPasos: " + steps,
                "Métricas", JOptionPane.INFORMATION_MESSAGE);
    }

    private Color getPathColor(String algorithm) {
        return switch (algorithm) {
            case "MazeSolverBFS" ->
                Color.GREEN;  // Ruta óptima
            case "MazeSolverDFS" ->
                Color.YELLOW; // Ruta menos óptima
            case "MazeSolverRecursivo" ->
                Color.ORANGE; // Ruta intermedia
            case "MazeSolverCache" ->
                Color.RED;  // Ruta ineficiente
            default ->
                Color.GRAY;               // Por defecto
        };
    }

    private boolean isValidCell(Cell cell) {
        return cell.row >= 0 && cell.row < grid.length
                && cell.col >= 0 && cell.col < grid[0].length
                && grid[cell.row][cell.col];
    }

    private void resetMaze() {
        // Reiniciar el laberinto
        initializeMaze();
        createMazeGrid();
        mazePanel.revalidate();
        mazePanel.repaint();

        // Reiniciar los campos de texto
        startRowField.setText("1");
        startColField.setText("1");
        endRowField.setText("5");
        endColField.setText("5");

        // Reiniciar el CheckBox
        delayCheckBox.setSelected(false);

        // Reiniciar las celdas de inicio y fin
        startCell = null;
        endCell = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeSolverUI mazeSolver = new MazeSolverUI();
            mazeSolver.setVisible(true);
        });
    }
}
