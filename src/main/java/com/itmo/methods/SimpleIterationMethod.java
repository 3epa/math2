package com.itmo.methods;

import com.itmo.DTO.FunctionHolder;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.DTO.IterationResult;
import com.itmo.utils.MathUtils;

import java.util.function.Function;

public class SimpleIterationMethod extends Method {
    private final Function<Double, Double> df;

    public SimpleIterationMethod(double epsilon, int MAX_ITERATIONS, FunctionHolder functionHolder) {
        super(epsilon, MAX_ITERATIONS ,functionHolder.getF());
        this.df = functionHolder.getDf();
    }

    private double findLipschitzCoefficient(double a, double b) {
        return 1/MathUtils.findMaxFunction(df, epsilon, a, b);
    }

    @Override
    public IterationResult solve(double a, double b) throws IncorrectInputException {
        Function<Double, Double> phi = x -> x - MathUtils.getFunctionSign(df, epsilon, a, b) * findLipschitzCoefficient(a, b) * f.apply(x);
        check(a,b, phi);
        double x = a;
        double xPrev;

        int iterations = 0;
        do {
            xPrev = x;
            x = phi.apply(x);
            iterations++;
            if (iterations > MAX_ITERATIONS) {
                throw new IncorrectInputException("Метод не сошёлся за разумное количество итераций.");
            }
        } while (!isSolved(x,xPrev));
        return new IterationResult(iterations + 1, x, f.apply(x));
    }

    protected void check(double a, double b, Function<Double, Double> phi ) throws IncorrectInputException {
        super.check(a,b);
        Function<Double, Double> dPhi = x -> 1 - MathUtils.getFunctionSign(df, epsilon, a, b) * findLipschitzCoefficient(a, b) * df.apply(x);
        if (MathUtils.findMaxFunction(dPhi, epsilon, a, b) >= 1) {
            System.out.println(MathUtils.findMaxFunction(phi, epsilon, a, b));
            throw new IncorrectInputException("Не выполняется условие сходимости метода");
        }
    }
}
