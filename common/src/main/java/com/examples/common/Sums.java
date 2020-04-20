package com.examples.common;

public class Sums {
    private Sums() {
        // hide default constructor
    }

    public static int firstN(final int n) {
        return n * (n + 1) / 2;
    }

    public static int firstNSquares(final int n) {
        return n * (n + 1) * (2*n + 1) / 6;
    }
}
