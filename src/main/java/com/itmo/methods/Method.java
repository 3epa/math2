package com.itmo.methods;

import com.itmo.IncorrectInputException;
import com.itmo.IterationResult;

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

    public abstract IterationResult solve(double a, double b) throws IncorrectInputException;

    protected void check(double a, double b) throws IncorrectInputException {
        double xPrev = a;
        boolean flag = false;
        for (double x = a + epsilon; x < b; x += epsilon) {
            if (f.apply(x) * f.apply(xPrev) < 0) {
                if (flag) {
                    throw new IncorrectInputException("На данном интервале несколько корней уравнения");
                }
                flag = true;
            }
            xPrev = x;
        }
        if (!flag) {
            throw new IncorrectInputException("На данном интервале отсутствует корень уравнения");
        }
    }

    protected boolean isSolved(double x1, double x2) {
        return Math.abs(x1 - x2) <= epsilon || Math.abs(f.apply(x1)) <= epsilon || Math.abs(f.apply(x2)) <= epsilon;
    }
}
