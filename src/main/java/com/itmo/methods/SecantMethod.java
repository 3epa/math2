package com.itmo.methods;

import java.util.function.Function;

public class SecantMethod extends Method {

    private final Function<Double, Double> d2f;
    public SecantMethod(double epsilon, Function<Double, Double> f, Function<Double, Double> d2f) {
        super(epsilon, f);
        this.d2f = d2f;
    }

    @Override
    public double solve(double a, double b) {
        if (!check(a, b)) {
            return 1;
        }
        double xPrev = choose(a,b);
        double x = xPrev + epsilon;
        double temp;
        do {
            temp = x;
            x = x - f.apply(x) * (x - xPrev) / (f.apply(x)- f.apply(xPrev));
            xPrev = temp;
        } while (!isSolved(x, xPrev));
        return x;
    }
    private double choose(double a, double b) {
        if (f.apply(a) * d2f.apply(a) > 0) {
            return a;
        }
        return b;
    }
}
