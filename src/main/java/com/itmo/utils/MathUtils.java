package com.itmo.utils;

import com.itmo.exceptions.IncorrectInputException;

import java.util.function.Function;

public class MathUtils {
    public static double findMaxFunction(Function<Double, Double> function, double epsilon, double a, double b) {
        double max = 0;
        for (double x = a; x <= b; x += epsilon) {
            max = Math.max(max, Math.abs(function.apply(x)));
        }
        return max;
    }

    public static double findMaxNFunction(Function<Double[], Double> function, double epsilon, double[][] bounds) {
        int dimensions = bounds.length;
        Double[] currentPoint = new Double[dimensions];
        double max = Double.NEGATIVE_INFINITY;
        return searchMax(function, epsilon, bounds, 0, max, currentPoint);
    }

    private static double searchMax(Function<Double[], Double> function, double epsilon, double[][] bounds, int currentDim, double currentMax, Double[] currentPoint) {
        if (currentDim == bounds.length) {
            double value = Math.abs(function.apply(currentPoint));
            return Math.max(currentMax, value);
        }
        double max = currentMax;
        double current = bounds[currentDim][0];
        while (current <= bounds[currentDim][1]) {
            currentPoint[currentDim] = current;
            max = searchMax(function, epsilon, bounds, currentDim + 1, max, currentPoint);
            current += epsilon;
        }
        return max;
    }

    public static double getFunctionSign(Function<Double, Double> function, double epsilon, double a, double b) throws IncorrectInputException {
        double firstSign = Math.signum(function.apply(a));

        for (double x = a; x <= b; x += epsilon) {
            double currentSign = Math.signum(function.apply(x));

            if (currentSign != firstSign) {
                throw new IncorrectInputException("Функция меняет знак в указанном диапазоне");
            }
        }
        return firstSign;
    }
}
