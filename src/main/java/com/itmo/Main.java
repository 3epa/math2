package com.itmo;


import com.itmo.DTO.FunctionHolder;
import com.itmo.DTO.SystemEquation;

import javax.swing.*;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        FunctionHolder[] holders = {
                new FunctionHolder(
                        "4,45x³+7,81x²-9,62x-8,17",
                        x -> 4.45 * x * x * x + 7.81 * x * x - 9.62 * x - 8.17,
                        x -> 13.35 * x * x + 15.62 * x - 9.62,
                        x -> 26.7 * x + 15.62
                ),
                new FunctionHolder(
                        "x³-x+4",
                        x -> x * x * x - x + 4,
                        x -> 3 * x * x - 1,
                        x -> 6 * x
                ),
                new FunctionHolder(
                        "eˣ - 1",
                        x -> Math.exp(x) - 1,
                        Math::exp,
                        Math::exp
                ),
                new FunctionHolder(
                        "e⁻ˣ - sin(x)",
                        x -> Math.exp(-x) - Math.sin(x),
                        x -> -Math.exp(-x) - Math.cos(x),
                        x -> Math.exp(-x) + Math.sin(x)
                )
        };
        SystemEquation[] systems = {
                new SystemEquation(
                        "0.1x² + x + 0.2y² - 0.3 = 0\n0.2x² + y + 0.1xy - 0.7 = 0",
                        new Function[]{
                                (Function<Double[], Double>) vars -> 0.1 * vars[0] * vars[0] + vars[0] + 0.2 * vars[1] * vars[1] - 0.3,
                                (Function<Double[], Double>) vars -> 0.2 * vars[0] * vars[0] + vars[1] + 0.1 * vars[0] * vars[1] - 0.7
                        },
                        new Function[]{
                                (Function<Double[], Double>) vars -> -0.1 * vars[0] * vars[0] - 0.2 * vars[1] * vars[1] + 0.3,
                                (Function<Double[], Double>) vars -> -0.2 * vars[0] * vars[0] - 0.1 * vars[0] * vars[1] + 0.7
                        },
                        new Function[][]{
                                {
                                        (Function<Double[], Double>) vars -> -0.2 * vars[0],
                                        (Function<Double[], Double>) vars -> 0.4 * vars[1]
                                },
                                {
                                        (Function<Double[], Double>) vars -> -0.4 * vars[0] - 0.1 * vars[1],
                                        (Function<Double[], Double>) vars -> -0.1 * vars[0]
                                }
                        }
                ),
                new SystemEquation(
                        "x² + y² - 1 = 0\n0.1x - y = 0",
                        new Function[]{
                                (Function<Double[], Double>) vars -> vars[0] * vars[0] + vars[1] * vars[1] - 1,
                                (Function<Double[], Double>) vars -> 0.1 * vars[0] - vars[1]
                        },
                        new Function[]{
                                (Function<Double[], Double>) vars -> Math.sqrt(1 - vars[1] * vars[1]),
                                (Function<Double[], Double>) vars -> 0.1 * vars[0]
                        },
                        new Function[][]{
                                {
                                        (Function<Double[], Double>) vars -> 0.0,
                                        (Function<Double[], Double>) vars -> -2 * vars[1] / Math.sqrt(1 - vars[1] * vars[1])
                                },
                                {
                                        (Function<Double[], Double>) vars -> 0.1,
                                        (Function<Double[], Double>) vars -> 0.0
                                }
                        }
                )
        };
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI(holders, systems);
            gui.setVisible(true);
        });
    }
}