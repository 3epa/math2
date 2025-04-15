package com.itmo.methods;

import com.itmo.FunctionHolder;


public class ChordMethod extends Method {

    public ChordMethod(double epsilon, int MAX_ITERATIONS, FunctionHolder functionHolder) {
        super(epsilon, MAX_ITERATIONS ,functionHolder.getF());
    }

    @Override
    public double solve(double a, double b) {
        check(a,b);
        double x0 = a;
        double x1 = b;
        double xNew;

        int iterations = 0;
        do {
            xNew = x0 - f.apply(x0) * (x0 - x1) / (f.apply(x0) - f.apply(x1));

            if (f.apply(xNew) * f.apply(x0) < 0) {
                x1 = xNew;
            } else {
                x0 = xNew;
            }
            iterations++;
            if (iterations > MAX_ITERATIONS) {
                throw new ArithmeticException("Метод не сошёлся за разумное количество итераций.");
            }
        } while (!isSolved(x0, x1));
        return xNew;
    }
}
