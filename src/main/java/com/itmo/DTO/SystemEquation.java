package com.itmo.DTO;

import java.util.function.Function;

public class SystemEquation {
    private final String description;
    private final Function<Double[], Double>[] f;
    private final Function<Double[], Double>[] phi;
    private final Function<Double[], Double>[][] dPhi;

    public SystemEquation(String description,
                          Function<Double[], Double>[] f,
                          Function<Double[], Double>[] phi,
                          Function<Double[], Double>[][] dPhi) {
        this.description = description;
        this.f = f;
        this.phi = phi;
        this.dPhi = dPhi;
    }

    public String getDescription() {
        return description;
    }

    public Function<Double[], Double>[] getF() {
        return f;
    }

    public Function<Double[], Double>[] getPhi() {
        return phi;
    }

    public Function<Double[], Double>[][] getDPhi() {
        return dPhi;
    }
}