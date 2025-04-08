package com.itmo.methods;

public abstract class Method {
    private double epsilon;

    public Method(double epsilon) {
        this.epsilon = epsilon;
    }

    public abstract void solve();

    public boolean check(double left, double right, Function function) {
        return function.execute(left) * function.execute(right) < 0;
    }

    public boolean isSolved(double x1, double x2) {
        return Math.abs(x1 - x2) < epsilon;
    }
}
