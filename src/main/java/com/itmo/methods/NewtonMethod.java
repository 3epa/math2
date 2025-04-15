package com.itmo.methods;

import com.itmo.DTO.FunctionHolder;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.DTO.IterationResult;

import java.util.function.Function;

public class NewtonMethod extends Method {

    private final Function<Double, Double> df;
    private final Function<Double, Double> d2f;

    public NewtonMethod(double epsilon, int MAX_ITERATIONS, FunctionHolder functionHolder) {
        super(epsilon, MAX_ITERATIONS ,functionHolder.getF());
        this.df = functionHolder.getDf();
        this.d2f = functionHolder.getD2f();
    }

    @Override
    public IterationResult solve(double a, double b) throws IncorrectInputException {
        check(a,b);
        double x = choose(a, b);
        double xPrev;
        int iterations = 0;
        do {
            xPrev = x;
            x = xPrev - f.apply(xPrev) / df.apply(x);
            iterations++;
            if (iterations > MAX_ITERATIONS) {
                throw new IncorrectInputException("Метод не сошёлся за разумное количество итераций.");
            }
        } while (!isSolved(x,xPrev));
        return new IterationResult(iterations + 1, x, f.apply(x));
    }

    private double choose(double a, double b) {
        if (f.apply(a) * d2f.apply(a) > 0) {
            return a;
        }
        return b;
    }
}
