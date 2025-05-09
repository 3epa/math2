package com.itmo;

import com.itmo.DTO.*;
import com.itmo.methods.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainGUI extends JFrame {
    private JComboBox<String> systemComboBox;
    private JComboBox<String> functionComboBox;
    private JComboBox<String> methodComboBox;
    private JTextField xLeftField;
    private JTextField xRightField;
    private JTextField yLeftField;
    private JTextField yRightField;
    private JTextArea resultArea;
    private JTextField epsilonField;
    private JPanel chartPanel;
    private JButton solveButton;
    private JButton saveToFileButton;
    private String lastResult = "";
    private JButton drawGraphButton;
    private JTextField xInitApproximationField;
    private JTextField yInitApproximationField;

    private final FunctionHolder[] holders;
    private final SystemEquation[] systems;

    private static final String[] METHODS = {
            "Метод простой итерации",
            "Метод Ньютона",
            "Метод хорд"
    };
    private static final String[] TASK_TYPES = {
            "Одно уравнение",
            "Система уравнений"
    };

    public MainGUI(FunctionHolder[] holders, SystemEquation[] systems) {
        this.holders = holders;
        this.systems = systems;

        setTitle("Вычмат ЛР2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        add(resultScrollPane, BorderLayout.CENTER);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setPreferredSize(new Dimension(640, 320));
        add(chartPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
    }

    private JPanel createTypeSelectionPanel() {
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        typePanel.add(new JLabel("Выберите тип задачи:"));
        systemComboBox = new JComboBox<>(TASK_TYPES);
        systemComboBox.addActionListener(this::handleTaskTypeChange);
        typePanel.add(systemComboBox);
        return typePanel;
    }

    private void handleTaskTypeChange(ActionEvent e) {
        if (systemComboBox.getSelectedIndex() == 1) {
            updateFunctionComboBoxForSystems();
            methodComboBox.setSelectedIndex(0);
            methodComboBox.setEnabled(false);
            yLeftField.setEnabled(true);
            yRightField.setEnabled(true);
            xInitApproximationField.setEnabled(true);
            yInitApproximationField.setEnabled(true);
        } else {
            updateFunctionComboBoxForSingleEquation();
            methodComboBox.setEnabled(true);
            yLeftField.setEnabled(false);
            yRightField.setEnabled(false);
            xInitApproximationField.setEnabled(false);
            yInitApproximationField.setEnabled(false);
        }
    }

    private void updateFunctionComboBoxForSystems() {
        functionComboBox.removeAllItems();
        for (SystemEquation system : systems) {
            String htmlDescription = "<html>" + system.getDescription().replace("\n", "<br>") + "</html>";
            functionComboBox.addItem(htmlDescription);
        }
    }

    private void updateFunctionComboBoxForSingleEquation() {
        functionComboBox.removeAllItems();
        for (FunctionHolder holder : holders) {
            functionComboBox.addItem(holder.getDescription());
        }
    }

    private JPanel createMethodSelectionPanel() {
        JPanel methodPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        methodPanel.add(new JLabel("Выберите метод:"));
        methodComboBox = new JComboBox<>(METHODS);
        methodPanel.add(methodComboBox);
        return methodPanel;
    }

    private JPanel createFunctionSelectionPanel() {
        JPanel functionPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        functionPanel.add(new JLabel("Выберите функцию/систему:"));
        functionComboBox = new JComboBox<>();
        for (FunctionHolder holder : holders) {
            functionComboBox.addItem(holder.getDescription());
        }
        functionPanel.add(functionComboBox);
        return functionPanel;
    }

    private JPanel createBoundsPanel() {
        JPanel boundsPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JLabel xLeftLabel = new JLabel("Левая граница x:");
        JLabel xRightLabel = new JLabel("Правая граница x:");
        JLabel yLeftLabel = new JLabel("Левая граница y:");
        JLabel yRightLabel = new JLabel("Правая граница y:");

        boundsPanel.add(xLeftLabel);
        boundsPanel.add(xLeftField);
        boundsPanel.add(xRightLabel);
        boundsPanel.add(xRightField);
        boundsPanel.add(yLeftLabel);
        boundsPanel.add(yLeftField);
        boundsPanel.add(yRightLabel);
        boundsPanel.add(yRightField);
        return boundsPanel;
    }

    private JPanel createEpsilonPanel() {
        JPanel epsilonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        epsilonPanel.add(new JLabel("Точность (ε):"));
        epsilonPanel.add(epsilonField);
        return epsilonPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        solveButton = new JButton("Решить");
        solveButton.addActionListener(new SolveButtonListener());
        buttonPanel.add(solveButton);

        saveToFileButton = new JButton("Сохранить в файл");
        saveToFileButton.addActionListener(new SaveToFileListener());
        buttonPanel.add(saveToFileButton);

        drawGraphButton = new JButton("Нарисовать график");
        drawGraphButton.addActionListener(new UpdateChartListener());
        buttonPanel.add(drawGraphButton);
        return buttonPanel;
    }

    private void initInputFields() {
        xLeftField = new JTextField("-1");
        xRightField = new JTextField("1");
        yLeftField = new JTextField("-1");
        yRightField = new JTextField("1");
        epsilonField = new JTextField("0.01");
        xInitApproximationField = new JTextField("0");
        yInitApproximationField = new JTextField("0");

        yLeftField.setEnabled(false);
        yRightField.setEnabled(false);
        xInitApproximationField.setEnabled(false);
        yInitApproximationField.setEnabled(false);
    }

    private JPanel createInitApproximationPanel() {
        JPanel boundsPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        JLabel xInitApproximationLabel = new JLabel("Начальное приближение x:");
        JLabel yInitApproximationLabel = new JLabel("Начальное приближение y:");

        boundsPanel.add(xInitApproximationLabel);
        boundsPanel.add(xInitApproximationField);
        boundsPanel.add(yInitApproximationLabel);
        boundsPanel.add(yInitApproximationField);
        return boundsPanel;
    }

    private JPanel createInputPanel() {
        initInputFields();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTypeSelectionPanel());
        panel.add(createMethodSelectionPanel());
        panel.add(createFunctionSelectionPanel());
        panel.add(createBoundsPanel());
        panel.add(createInitApproximationPanel());
        panel.add(createEpsilonPanel());
        panel.add(createButtonPanel());

        return panel;
    }

    private class SaveToFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (lastResult.isEmpty()) {
                JOptionPane.showMessageDialog(MainGUI.this,
                        "Нет результатов для сохранения",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Сохранить результаты");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String defaultFileName = "results_" + dateFormat.format(new Date()) + ".txt";
            fileChooser.setSelectedFile(new File(defaultFileName));

            int userSelection = fileChooser.showSaveDialog(MainGUI.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                    writer.write(lastResult);
                    JOptionPane.showMessageDialog(MainGUI.this,
                            "Результаты успешно сохранены в файл: " + fileToSave.getAbsolutePath(),
                            "Сохранено",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainGUI.this,
                            "Ошибка при сохранении файла: " + ex.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class UpdateChartListener implements ActionListener {
        private static final double EPSILON = 0.001;
        private static final int POINTS_COUNT = 5000;
        private static final double MARKER_SIZE = 2.0;

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                chartPanel.removeAll();

                int systemType = systemComboBox.getSelectedIndex();
                int functionIndex = functionComboBox.getSelectedIndex();

                double xLeft = parseDoubleField(xLeftField);
                double xRight = parseDoubleField(xRightField);
                double yLeft = parseDoubleField(yLeftField);
                double yRight = parseDoubleField(yRightField);

                JFreeChart chart = systemType == 0
                        ? createFunctionChart(functionIndex, xLeft, xRight)
                        : createSystemChart(functionIndex, xLeft, xRight, yLeft, yRight);

                chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
                chartPanel.revalidate();
                chartPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(chartPanel, "Введите числовые данные", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(chartPanel, "Не удалось построить график, попробуйте ввести другие данные", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private double parseDoubleField(JTextField field) {
            return Double.parseDouble(field.getText().replaceAll(",", "."));
        }

        private JFreeChart createFunctionChart(int functionIndex, double xLeft, double xRight) {
            XYSeries series = new XYSeries("Функция");
            double step = (xRight - xLeft) / POINTS_COUNT;

            for (double x = xLeft; x <= xRight; x += step) {
                series.add(x, holders[functionIndex].getF().apply(x));
            }

            XYSeriesCollection dataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "График функции", "x", "f(x)", dataset,
                    PlotOrientation.VERTICAL, true, true, false
            );

            addZeroMarker(chart, series);
            return chart;
        }

        private JFreeChart createSystemChart(int functionIndex, double xLeft, double xRight, double yLeft, double yRight) {
            XYSeries series1 = new XYSeries("Уравнение 1");
            XYSeries series2 = new XYSeries("Уравнение 2");

            double xStep = (xRight - xLeft) / POINTS_COUNT;
            double yStep = (yRight - yLeft) / POINTS_COUNT;

            for (double x = xLeft; x <= xRight; x += xStep) {
                for (double y = yLeft; y <= yRight; y += yStep) {
                    Double[] point = {x, y};
                    double f1 = systems[functionIndex].getF()[0].apply(point);
                    double f2 = systems[functionIndex].getF()[1].apply(point);

                    if (Math.abs(f1) < EPSILON) series1.add(x, y);
                    if (Math.abs(f2) < EPSILON) series2.add(x, y);
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series1);
            dataset.addSeries(series2);

            JFreeChart chart = ChartFactory.createScatterPlot(
                    "График системы уравнений", "x", "y", dataset,
                    PlotOrientation.VERTICAL, true, true, false
            );

            styleSystemChart(chart);
            return chart;
        }

        private void addZeroMarker(JFreeChart chart, XYSeries series) {
            XYPlot plot = chart.getXYPlot();
            double yMin = series.getMinY();
            double yMax = series.getMaxY();

            if (yMin <= 0 && yMax >= 0) {
                ValueMarker zeroMarker = new ValueMarker(0);
                zeroMarker.setStroke(new BasicStroke(1.5f));
                zeroMarker.setPaint(Color.BLACK);
                plot.addRangeMarker(zeroMarker);
            }
        }

        private void styleSystemChart(JFreeChart chart) {
            XYPlot plot = chart.getXYPlot();
            XYItemRenderer renderer = plot.getRenderer();

            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.BLUE);

            double size = MARKER_SIZE / 2;
            Shape shape = new Ellipse2D.Double(-size, -size, MARKER_SIZE, MARKER_SIZE);
            renderer.setSeriesShape(0, shape);
            renderer.setSeriesShape(1, shape);
        }
    }


    private class SolveButtonListener implements ActionListener {
        private static final int MAX_ITERATIONS = 100000;

        private SolutionResult solveProblem() throws Exception {
            int systemType = systemComboBox.getSelectedIndex();
            if (systemType == 0) {
                return solveSingleEquation();
            } else {
                return solveEquationSystem();
            }
        }

        private IterationResult solveSingleEquation() throws Exception {
            int functionIndex = functionComboBox.getSelectedIndex();
            int methodIndex = methodComboBox.getSelectedIndex();

            double epsilon = parseDoubleField(epsilonField);
            double xLeft = parseDoubleField(xLeftField);
            double xRight = parseDoubleField(xRightField);

            validateInterval(xLeft, xRight);

            Method method = createMethod(methodIndex, epsilon, holders[functionIndex]);
            return method.solve(xLeft, xRight);
        }

        private void validateInterval(double left, double right) {
            if (left >= right) {
                throw new IllegalArgumentException("Левая граница должна быть меньше правой");
            }
        }

        private Method createMethod(int methodIndex, double epsilon, FunctionHolder holder) {
            return switch (methodIndex) {
                case 0 -> new SimpleIterationMethod(epsilon, MAX_ITERATIONS, holder);
                case 1 -> new NewtonMethod(epsilon, MAX_ITERATIONS, holder);
                case 2 -> new ChordMethod(epsilon, MAX_ITERATIONS, holder);
                default -> throw new IllegalArgumentException("Выбран неизвестный метод");
            };
        }

        private SystemIterationResult solveEquationSystem() throws Exception {
            int functionIndex = functionComboBox.getSelectedIndex();
            int methodIndex = methodComboBox.getSelectedIndex();

            if (methodIndex != 0) {
                throw new IllegalArgumentException("Для систем уравнений доступен только метод простой итерации");
            }
            double epsilon = parseDoubleField(epsilonField);
            double xLeft = parseDoubleField(xLeftField);
            double xRight = parseDoubleField(xRightField);
            double yLeft = parseDoubleField(yLeftField);
            double yRight = parseDoubleField(yRightField);
            double xInit = parseDoubleField(xInitApproximationField);
            double yInit = parseDoubleField(yInitApproximationField);

            validateInterval(xLeft, xRight);
            validateInterval(yLeft, yRight);
            validateInitialApproximation(xInit, xLeft, xRight, "X");
            validateInitialApproximation(yInit, yLeft, yRight, "Y");
            SystemSimpleIterationMethod method = new SystemSimpleIterationMethod(
                    epsilon, MAX_ITERATIONS, systems[functionIndex]);

            Double[] initials = {xInit, yInit};
            double[][] bounds = {{xLeft, xRight}, {yLeft, yRight}};
            return method.solve(initials, bounds);
        }

        private double parseDoubleField(JTextField field) {
            return Double.parseDouble(field.getText().replaceAll(",", "."));
        }

        private void validateInitialApproximation(double value, double left, double right, String axis) {
            if (value < left || value > right) {
                throw new IllegalArgumentException(
                        String.format("Начальное приближение %s должно быть в указанном диапазоне", axis));
            }
        }

        private void displayResult(SolutionResult result) {
            StringBuilder output = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.######");

            String problemType = systemComboBox.getSelectedIndex() == 0 ?
                    "Решение нелинейного уравнения" :
                    "Решение системы нелинейных уравнений";
            output.append("=== ").append(problemType).append(" ===\n\n");

            output.append("Метод решения: ")
                    .append(methodComboBox.getSelectedItem())
                    .append("\n");

            String functionInfo = functionComboBox.getSelectedItem().toString()
                    .replace("<html>", "")
                    .replace("</html>", "")
                    .replace("<br>", "\n");

            output.append("Функция:       \n").append(functionInfo).append("\n");

            output.append("\nПараметры решения:\n");

            output.append(String.format("Точность:     %s\n",
                    df.format(parseDoubleField(epsilonField))));

            if (systemComboBox.getSelectedIndex() == 0) {
                output.append(String.format("Интервал:     [%s, %s]\n",
                        df.format(parseDoubleField(xLeftField)),
                        df.format(parseDoubleField(xRightField))));
            } else {
                output.append(String.format("Нач. приближ: (%s, %s)\n",
                        df.format(parseDoubleField(xInitApproximationField)),
                        df.format(parseDoubleField(yInitApproximationField))));
                output.append(String.format("Границы X:    [%s, %s]\n",
                        df.format(parseDoubleField(xLeftField)),
                        df.format(parseDoubleField(xRightField))));
                output.append(String.format("Границы Y:    [%s, %s]\n",
                        df.format(parseDoubleField(yLeftField)),
                        df.format(parseDoubleField(yRightField))));
            }
            output.append("\n").append("-".repeat(50)).append("\n\n");

            output.append(result.getFormattedResult());

            output.append("\n\n").append("=".repeat(50));
            resultArea.setText(output.toString());
            lastResult = output.toString();
        }

        private void showError(String message) {
            JOptionPane.showMessageDialog(MainGUI.this,
                    message,
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                SolutionResult result = solveProblem();
                displayResult(result);
            } catch (NumberFormatException ex) {
                showError("Пожалуйста, введите валидные числовые данные");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }
    }
}