package com.itmo;

public class IterationResult {
    private final int iteration;
    private final double x;
    private final double fValue;

    public IterationResult(int iteration, double x, double fValue) {
        this.iteration = iteration;
        this.x = x;
        this.fValue = fValue;
    }

    public int getIteration() { return iteration; }
    public double getX() { return x; }
    public double getFValue() { return fValue; }

    @Override
    public String toString() {
        return "iteration=" + iteration +
                ", x=" + x +
                ", value=" + fValue;
    }
}
