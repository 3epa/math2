package com.itmo;

import java.util.function.Function;

public class FunctionHolder {
    private final Function<Double, Double> f;
    private final Function<Double, Double> df;
    private final Function<Double, Double> d2f;

    public FunctionHolder(Function<Double, Double> f,
                          Function<Double, Double> df,
                          Function<Double, Double> d2f) {
        this.f = f;
        this.df = df;
        this.d2f = d2f;
    }

    public Function<Double, Double> getF() {
        return f;
    }

    public Function<Double, Double> getDf() {
        return df;
    }

    public Function<Double, Double> getD2f() {
        return d2f;
    }
}
