package com.itmo.methods;

import org.apache.commons.math3.analysis.UnivariateFunction;

public abstract class Method {
    private double epsilon;

    public Method(double epsilon) {
        this.epsilon = epsilon;
    }

    public abstract double solve(double start, double end, UnivariateFunction function);

    protected boolean check(double left, double right, UnivariateFunction function) {
        return function.value(left) * function.value(right) > 0;
    }

    protected boolean isSolved(double x1, double x2) {
        return Math.abs(x1 - x2) <= epsilon;
    }
}
