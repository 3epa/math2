package com.itmo.methods;

import com.itmo.DTO.FunctionHolder;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.DTO.IterationResult;
import com.itmo.utils.MathUtils;

import java.util.function.Function;

public class SimpleIterationMethod extends Method {
    private final Function<Double, Double> df;
    private final Function<Double, Double> d2f;
    private Double q;

    public SimpleIterationMethod(double epsilon, int MAX_ITERATIONS, FunctionHolder functionHolder) {
        super(epsilon, MAX_ITERATIONS, functionHolder.getF());
        this.df = functionHolder.getDf();
        this.d2f = functionHolder.getD2f();
    }

    private double findLipschitzCoefficient(double a, double b) {
        return 1 / MathUtils.findMaxFunction(df, epsilon, a, b);
    }

    private Function<Double, Double> phi(double a, double b) throws IncorrectInputException {
        double sign;
        try {
            sign = MathUtils.getFunctionSign(df, epsilon, a, b);
        } catch (IncorrectInputException e) {
            throw new IncorrectInputException("Производная функции меняет знак на указанном диапазоне");
        }
        return x -> x - sign * findLipschitzCoefficient(a, b) * f.apply(x);
    }

    private Function<Double, Double> dPhi(double a, double b) throws IncorrectInputException {
        double sign;
        try {
            sign = MathUtils.getFunctionSign(df, epsilon, a, b);
        } catch (IncorrectInputException e) {
            throw new IncorrectInputException("Производная функции меняет знак на указанном диапазоне");
        }
        return x -> 1 - sign * findLipschitzCoefficient(a, b) * df.apply(x);
    }

    @Override
    public IterationResult solve(double a, double b) throws IncorrectInputException {
        check(a, b);
        Function<Double, Double> phi = phi(a, b);
        double x = choose(a, b);
        double xPrev;

        int iterations = 0;
        do {
            xPrev = x;
            x = phi.apply(x);
            iterations++;
            if (iterations > MAX_ITERATIONS) {
                throw new IncorrectInputException("Метод не сошёлся за разумное количество итераций.");
            }
        } while (!isSolved(x, xPrev));
        return new IterationResult(iterations + 1, x, f.apply(x));
    }

    private double choose(double a, double b) {
        if (f.apply(a) * d2f.apply(a) > 0) {
            return a;
        }
        return b;
    }

    @Override
    protected boolean isSolved(double x1, double x2) {
        if (0 < q && q <= 0.5) {
            return Math.abs(x1 - x2) <= epsilon || Math.abs(f.apply(x1)) < epsilon;
        } else {
            return Math.abs(x1 - x2) <= (1 - q) / q * epsilon || Math.abs(f.apply(x1)) < epsilon;
        }
    }

    @Override
    protected void check(double a, double b) throws IncorrectInputException {
        super.check(a, b);
        Function<Double, Double> dPhi = dPhi(a, b);
        this.q = MathUtils.findMaxFunction(dPhi, epsilon, a, b);
        if (this.q > 1) {
            throw new IncorrectInputException("Не выполняется условие сходимости метода");
        }
    }
}
