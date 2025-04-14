package com.itmo.methods;

import java.util.function.Function;

public abstract class Method {
    protected final double epsilon;

    protected Function<Double, Double> f;

    public Method(double epsilon, Function<Double, Double> f) {
        this.epsilon = epsilon;
        this.f = f;
    }

    public abstract double solve(double a, double b);

    protected boolean check(double a, double b) {
        return f.apply(a) * f.apply(b) < 0;
    }

    protected boolean isSolved(double x1, double x2) {
        return Math.abs(x1 - x2) <= epsilon;
    }
}
