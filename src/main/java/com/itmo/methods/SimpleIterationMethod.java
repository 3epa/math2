package com.itmo.methods;

import com.itmo.FunctionHolder;
import com.itmo.MathUtils;

import java.util.function.Function;

public class SimpleIterationMethod extends Method {
    private final Function<Double, Double> df;

    public SimpleIterationMethod(double epsilon, FunctionHolder functionHolder) {
        super(epsilon, functionHolder.getF());
        this.df = functionHolder.getDf();
    }

    private double findLipschitzCoefficient(double a, double b) {
        double max = 0;
        for (double x = a; x < b; x += epsilon) {
            max = Math.max(max, Math.abs(df.apply(x)));
        }
        return max;
    }

    public double phi(double a, double b, double x) {
        return x - checkSignConsistency(df, a, b) / findLipschitzCoefficient(a, b) * f.apply(x);
    }

    @Override
    public double solve(double a, double b) {
        double x = a;
        double xPrev;

        do {
            xPrev = x;
            x = phi(a, b, xPrev);
        } while (!isSolved(x,xPrev));
        return x;
    }


    private double checkSignConsistency(Function<Double, Double> function, double a, double b) {
        double firstSign = Math.signum(function.apply(a));

        for (double x = a; x < b; x += epsilon) {
            double currentSign = Math.signum(function.apply(x));

            if (currentSign != firstSign) {
                return 0;
            }
        }
        return firstSign;
    }
}
