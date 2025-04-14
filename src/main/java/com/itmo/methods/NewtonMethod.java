package com.itmo.methods;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateFunctionDifferentiator;

public class NewtonMethod extends Method {

    public NewtonMethod(double epsilon) {
        super(epsilon);
    }

    @Override
    public double solve(double start, double end, UnivariateFunction function) {
        UnivariateFunctionDifferentiator differentiator = new FiniteDifferencesDifferentiator(7, 0.01);

        UnivariateFunction derivative = differentiator.differentiate(function);

        return derivative.value(start);
    }

}
