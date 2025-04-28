package com.itmo;

import com.itmo.DTO.FunctionHolder;
import com.itmo.DTO.IterationResult;
import com.itmo.methods.ChordMethod;
import com.itmo.methods.Method;
import com.itmo.methods.NewtonMethod;
import com.itmo.methods.SimpleIterationMethod;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class MainGUI extends JFrame {
    private JComboBox<String> functionComboBox;
    private JComboBox<String> methodComboBox;
    private JTextField leftBoundField;
    private JTextField rightBoundField;
    private JTextArea resultArea;
    private JTextField epsilonField;
    private JPanel chartPanel;
    private JButton solveButton;
    private final FunctionHolder[] holders = {
            new FunctionHolder(
                    x -> 4.45 * x * x * x + 7.81 * x * x - 9.62 * x - 8.17,
                    x -> 13.35 * x * x + 15.62 * x - 9.62,
                    x -> 26.7 * x + 15.62
            ),
            new FunctionHolder(
                    x -> x * x * x - x + 4,
                    x -> 3 * x * x - 1,
                    x -> 6 * x
            ),
            new FunctionHolder(
                    x -> Math.exp(x) - 1,
                    Math::exp,
                    Math::exp
            )
    };

    public MainGUI() {
        setTitle("Вычмат ЛР2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 650);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        add(resultScrollPane, BorderLayout.CENTER);

        chartPanel = new JPanel(new BorderLayout());

        add(chartPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        panel.add(new JLabel("Выберите функцию:"));
        functionComboBox = new JComboBox<>(new String[]{
                "4.45x³ + 7.81x² - 9.62x - 8.17",
                "x³ - x + 4",
                "eˣ - 1"
        });
        panel.add(functionComboBox);

        panel.add(new JLabel("Выберите метод:"));
        methodComboBox = new JComboBox<>(new String[]{
                "Метод простой итерации:",
                "Метод Ньютона",
                "Метод хорд"
        });
        panel.add(methodComboBox);

        panel.add(new JLabel("Приближение (ε):"));
        epsilonField = new JTextField("0.01");
        panel.add(epsilonField);

        panel.add(new JLabel("Левая граница:"));
        leftBoundField = new JTextField("-2");
        panel.add(leftBoundField);

        panel.add(new JLabel("Правая граница:"));
        rightBoundField = new JTextField("-1");
        panel.add(rightBoundField);

        solveButton = new JButton("Найти корень");
        solveButton.addActionListener(new SolveButtonListener());
        panel.add(new JLabel());
        panel.add(solveButton);

        return panel;
    }

    private void updateChart(int functionIndex, double leftBound, double rightBound) {
        XYSeries series = new XYSeries("Function");
        double step = (rightBound - leftBound) / 200;
        for (double x = leftBound; x <= rightBound; x += step) {
            series.add(x, holders[functionIndex].getF().apply(x));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Function Graph",
                "x",
                "f(x)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();


        double yMin = series.getMinY();
        double yMax = series.getMaxY();
        if (yMin <= 0 && yMax >= 0) {
            org.jfree.chart.plot.ValueMarker zeroMarker = new org.jfree.chart.plot.ValueMarker(0);
            plot.addRangeMarker(zeroMarker);
            zeroMarker.setStroke(new BasicStroke(1.5f));
            zeroMarker.setPaint(Color.BLACK);
        }

        chartPanel.removeAll();
        chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int functionIndex = functionComboBox.getSelectedIndex();
                int methodIndex = methodComboBox.getSelectedIndex();

                double epsilon = Double.parseDouble(epsilonField.getText().replaceAll(",", "."));
                double leftBound = Double.parseDouble(leftBoundField.getText().replaceAll(",", "."));
                double rightBound = Double.parseDouble(rightBoundField.getText().replaceAll(",", "."));

                if (leftBound >= rightBound) {
                    throw new IllegalArgumentException("Левая граница должна быть меньше правой");
                }

                updateChart(functionIndex, leftBound, rightBound);

                Method method;
                switch (methodIndex) {
                    case 0:
                        method = new SimpleIterationMethod(epsilon, 100000, holders[functionIndex]);
                        break;
                    case 1:
                        method = new NewtonMethod(epsilon, 100000, holders[functionIndex]);
                        break;
                    case 2:
                        method = new ChordMethod(epsilon, 100000, holders[functionIndex]);
                        break;
                    default:
                        throw new IllegalArgumentException("Выбран неизвестный мтеод");
                }


                IterationResult result = method.solve(leftBound, rightBound);


                DecimalFormat df = new DecimalFormat("0.######");
                resultArea.setText(String.format(
                        "Функция: %s\nМетод: %s\nИнтервал: [%s, %s]\n\nРезультат: x = %s\nf(x) = %s\nКоличество итераций:%s",
                        functionComboBox.getSelectedItem(),
                        methodComboBox.getSelectedItem(),
                        df.format(leftBound),
                        df.format(rightBound),
                        df.format(result.getX()),
                        df.format(result.getFValue()),
                        df.format(result.getIteration())
                ));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainGUI.this,
                        "Пожалуйста, введите валидные данные",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(MainGUI.this,
                        ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                resultArea.setText("Ошибка: " + ex.getMessage() + ". Попробуйте другой интервал");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}