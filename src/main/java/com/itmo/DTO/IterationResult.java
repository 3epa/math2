package com.itmo.DTO;

import java.text.DecimalFormat;

public class IterationResult implements SolutionResult {
    private final int iteration;
    private final double x;
    private final double fValue;

    public IterationResult(int iteration, double x, double fValue) {
        this.iteration = iteration;
        this.x = x;
        this.fValue = fValue;
    }

    public int getIteration() {
        return iteration;
    }

    public double getX() {
        return x;
    }

    public double getFValue() {
        return fValue;
    }

    @Override
    public String getFormattedResult() {
        DecimalFormat df = new DecimalFormat("0.######");

        return String.format(
                """
                        Решение найдено:
                        x = %s
                        f(x) = %s
                        Количество итераций: %d""",
                df.format(x),
                df.format(fValue),
                iteration
        );
    }
}
