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
        super(epsilon, MAX_ITERATIONS ,functionHolder.getF());
        this.df = functionHolder.getDf();
    }

    private double findLipschitzCoefficient(double a, double b) {
        return 1/MathUtils.findMaxFunction(df, epsilon, a, b);
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
        Function<Double, Double> phi;
        try {
            phi = x -> x - MathUtils.getFunctionSign(df, epsilon, a, b) * findLipschitzCoefficient(a, b) * f.apply(x);
        } catch (ArithmeticException e) {
            throw new IncorrectInputException("Не удалось вычислить функцию phi, потому что производная " + e.getMessage().toLowerCase());
        }
        check(a,b, phi);
        double x = a;
        Function<Double, Double> phi = phi(a, b);
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
        if (MathUtils.findMaxFunction(dPhi, epsilon, a, b) >= 0.9) {
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
