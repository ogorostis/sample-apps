package com.examples.common;

public class Sets {
    private Sets() {
        // hide default constructor
    }

    public static long factorial(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("negative number");
        }

        long f = 1L;
        for (int i=1; i<=n; i++) {
            f = f * i;
        }
        return f;
    }

    /**]
     * Number of permutations
     * @param n set size
     * @return Number of permutations
     */
    public static long P(final int n) {
        return factorial(n);
    }

    /**
     * Number of p-size combinations in an n-size set
     * @param n set size
     * @param p combination size
     * @return number of combinations
     */
    public static long C(final int n, final int p) {
        return A(n, p) / factorial(p);
    }

    /**
     * Number of p-size arrangements in an n-size set
     * This is n! divided by (n-p)!
     * @param n set size
     * @param p arrangement size
     * @return number of arrangements
     */
    public static long A(final int n, final int p) {
        if (n < 0 || p < 0) {
            throw new IllegalArgumentException("negative number");
        }
        if (p > n) {
            throw new IllegalArgumentException("arrangement size out of range");
        }
        if (n == p) {
            return 1;
        }

        // Let's NOT calculate n! and (n-p)! ...
        int a = 1;
        int position = n;
        while (position > n - p) {
            a = a * position--;
        }
        return a;
    }
}
