package com.itmo.methods;

import java.util.function.Function;

public abstract class Method {
    protected final double epsilon;
    protected final int MAX_ITERATIONS;

    protected Function<Double, Double> f;

    public Method(double epsilon, int MAX_ITERATIONS, Function<Double, Double> f) {
        this.epsilon = epsilon;
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        this.f = f;
    }

    public abstract double solve(double a, double b);

    protected void check(double a, double b) {
        if (f.apply(a) * f.apply(b) > 0) {
            throw new ArithmeticException("На данном интервале отсутствует корень уравнения");
        }
        double xPrev = a;
        boolean flag = false;
        for (double x = a + epsilon; x < b; x += epsilon) {
            if (f.apply(x) * f.apply(xPrev) < 0) {
                if (flag) {
                    throw new ArithmeticException("На данном интервале несколько корней уравнения");
                }
                flag = true;
            }
            xPrev = x;
        }
    }

    protected boolean isSolved(double x1, double x2) {
        return Math.abs(x1 - x2) <= epsilon;
    }
}
