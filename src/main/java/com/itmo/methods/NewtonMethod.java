package com.itmo.methods;

import com.itmo.FunctionHolder;
import com.itmo.MathUtils;

import java.util.function.Function;

public class NewtonMethod extends Method {

    private final Function<Double, Double> df;
    private final Function<Double, Double> d2f;

    public NewtonMethod(double epsilon, FunctionHolder functionHolder) {
        super(epsilon, functionHolder.getF());
        this.df = functionHolder.getDf();
        this.d2f = functionHolder.getD2f();
    }

    @Override
    public double solve(double a, double b) {
        if (!check(a, b)) {
            return 1;
        }
        double x = choose(a, b);
        double xPrev;
        do {
            xPrev = x;
            x = xPrev - f.apply(xPrev) / df.apply(x);
        } while (!isSolved(x,xPrev));
        return x;
    }

    private double choose(double a, double b) {
        if (f.apply(a) * d2f.apply(a) > 0) {
            return a;
        }
        return b;
    }

    @Override
    protected boolean check(double a, double b) {
        return super.check(a,b) && MathUtils.checkSignConsistency(df, epsilon, a, b) && MathUtils.checkSignConsistency(d2f, epsilon, a,b)&& checkFunctionNonZero(df, a, b);
    }


    private boolean checkFunctionNonZero(Function<Double, Double> function, double a, double b) {

        for (double x = a; x < b; x += epsilon) {

            if (Math.abs(function.apply(x)) < 1e-12) {
                return false;
            }
        }
        return true;
    }

}
