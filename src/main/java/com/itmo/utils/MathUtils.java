package com.itmo.utils;

import java.util.function.Function;

public class MathUtils {
    public static double findMaxFunction(Function<Double, Double> function, double epsilon, double a, double b) {
        double max = 0;
        for (double x = a; x < b; x += epsilon) {
            max = Math.max(max, Math.abs(function.apply(x)));
        }
        return max;
    }
    public static double getFunctionSign(Function<Double, Double> function, double epsilon, double a, double b) {
        double firstSign = Math.signum(function.apply(a));

        for (double x = a; x < b; x += epsilon) {
            double currentSign = Math.signum(function.apply(x));

            if (currentSign != firstSign) {
                throw new ArithmeticException("Функция меняет знак в указанном диапазоне");
            }
        }
        return firstSign;
    }
}
