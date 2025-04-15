package com.itmo.methods;

import com.itmo.FunctionHolder;


public class ChordMethod extends Method {

    public ChordMethod(double epsilon, FunctionHolder functionHolder) {
        super(epsilon, functionHolder.getF());
    }

    @Override
    public double solve(double a, double b) {
        if (!check(a, b)) {
            return 1;
        }
        double x0 = a;
        double x1 = b;
        double xNew;
        do {
            xNew = x1 - f.apply(x1) * (x1 - x0) / (f.apply(x1) - f.apply(x0));

            if (f.apply(xNew) * f.apply(x0) < 0) {
                x1 = xNew;
            } else {
                x0 = xNew;
            }
        } while (!isSolved(xNew, x1));
        return xNew;
    }
}
