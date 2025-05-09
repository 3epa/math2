package com.itmo.methods;


import com.itmo.DTO.SystemEquation;
import com.itmo.DTO.SystemIterationResult;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.utils.MathUtils;

import java.util.function.Function;

public class SystemSimpleIterationMethod {
    private final Double epsilon;
    private final int MAX_ITERATIONS;
    private final Function<Double[], Double>[] f;
    private final Function<Double[], Double>[] phi;
    private final Function<Double[], Double>[][] dPhi;

    public SystemSimpleIterationMethod(double epsilon, int MAX_ITERATIONS, SystemEquation systemEquation) {
        this.epsilon = epsilon;
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        this.f = systemEquation.getF();
        this.phi = systemEquation.getPhi();
        this.dPhi = systemEquation.getDPhi();

    }

    public SystemIterationResult solve(Double[] initialGuess, double[][] bounds) throws IncorrectInputException {
        check(bounds);
        int iterations = 0;
        Double[] x = initialGuess.clone();
        Double[] xPrev = initialGuess.clone();
        Double[][] dX = new Double[f.length][2];

        do {
            System.arraycopy(x, 0, xPrev, 0, x.length);

            for (int i = 0; i < f.length; i++) {
                x[i] = phi[i].apply(x);
                dX[i][0] = x[i];
                dX[i][1] = xPrev[i];
            }

            iterations++;
            if (iterations >= MAX_ITERATIONS) {
                throw new IncorrectInputException("Метод не сошёлся за разумное количество итераций.");
            }
        } while (!isSolved(dX));

        Double[] result = new Double[f.length];
        for (int i = 0; i < f.length; i++) {
            result[i] = f[i].apply(x);
        }

        Double[] dX1 = new Double[dX.length];
        for (int i = 0; i < dX.length; i++) {
            dX1[i] = Math.abs(dX[i][1] - dX[i][0]);
        }

        return new SystemIterationResult(iterations, x, result, dX1);
    }

    private void check(double[][] bounds) throws IncorrectInputException {
        for (int i = 0; i < dPhi.length; i++) {
            double q = 0.0;
            for (int j = 0; j < dPhi.length; j++) {
                q += MathUtils.findMaxNFunction(dPhi[i][j], epsilon, bounds);
            }
            if (q >= 1) {
                throw new IncorrectInputException("Не выполняется условие сходимости метода");
            }
        }
    }

    private boolean isSolved(Double[][] dX) {
        for (int i = 0; i < dX.length; i++) {
            if (Math.abs(dX[i][0] - dX[i][1]) > epsilon) {
                return false;
            }
        }
        return true;
    }
}
