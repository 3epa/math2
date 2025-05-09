package com.itmo.DTO;

import java.text.DecimalFormat;

public class SystemIterationResult implements SolutionResult {
    private final int iteration;
    private final Double[] x;
    private final Double[] fValue;
    private final Double[] dX;

    public SystemIterationResult(int iteration, Double[] x, Double[] fValue, Double[] dX) {
        this.iteration = iteration;
        this.x = x;
        this.fValue = fValue;
        this.dX = dX;
    }

    public Double[] getfValue() {
        return fValue;
    }

    public Double[] getdX() {
        return dX;
    }

    public int getIteration() {
        return iteration;
    }

    public Double[] getX() {
        return x;
    }

    @Override
    public String getFormattedResult() {
        DecimalFormat df = new DecimalFormat("0.######");

        StringBuilder sb = new StringBuilder();
        sb.append("Решение системы найдено:\n\n");

        sb.append("Переменные:\n");
        for (int i = 0; i < x.length; i++) {
            sb.append(String.format("x%d = %s\n", i + 1, df.format(x[i])));
        }

        sb.append("\nЗначения функций:\n");
        for (int i = 0; i < fValue.length; i++) {
            sb.append(String.format("f%d(x) = %s\n", i + 1, df.format(fValue[i])));
        }

        sb.append("\nИзменения переменных на последней итерации:\n");
        for (int i = 0; i < dX.length; i++) {
            sb.append(String.format("Δx%d = %s\n", i + 1, df.format(dX[i])));
        }

        sb.append(String.format("\nКоличество итераций: %d", iteration));

        return sb.toString();
    }
}
