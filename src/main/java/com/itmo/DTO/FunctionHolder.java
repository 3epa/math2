package com.itmo.DTO;

import java.util.function.Function;

public class FunctionHolder {
    private final String description;
    private final Function<Double, Double> f;
    private final Function<Double, Double> df;
    private final Function<Double, Double> d2f;

    public FunctionHolder(String description,
                          Function<Double, Double> f,
                          Function<Double, Double> df,
                          Function<Double, Double> d2f) {
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
