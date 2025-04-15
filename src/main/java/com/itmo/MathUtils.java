package com.itmo;

import java.util.function.Function;

public class MathUtils {
    public static boolean checkSignConsistency(Function<Double, Double> function, double epsilon, double a, double b) {
        double firstSign = Math.signum(function.apply(a));

        for (double x = a; x < b; x += epsilon) {
            double currentSign = Math.signum(function.apply(x));

            if (currentSign != firstSign) {
                return false;
            }
        }
        return true;
    }
    public static double getFunctionSign(Function<Double, Double> function, double epsilon, double a, double b) {
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
