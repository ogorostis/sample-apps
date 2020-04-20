package com.examples.common;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SetsTest {
    @Test
    public void testACountSameNumbers() {
        Assert.assertEquals(Sets.A(4, 4), 1);
    }

    @Test
    public void testACount3A2() {
        Assert.assertEquals(Sets.A(3, 2), 6);
    }

    @Test
    public void testACount4A2() {
        Assert.assertEquals(Sets.A(4, 2), 12);
    }

    @Test
    public void testCCount3A2() {
        Assert.assertEquals(Sets.C(3, 2), 3);
    }

    @Test
    public void testFC() {
        Assert.assertEquals(Sets.factorial(7), Sets.P(7));
    }

    @Test
    public void testAC() {
        Assert.assertEquals(
                Sets.factorial(7) * Sets.C(10, 7),
                Sets.A(10, 7));
    }

    /**
     * C(n,p) = C(n,n-p)
     */
    @Test
    public void testCCount() {
        final int n = 10;
        final int p = 7;
        Assert.assertEquals(
                Sets.C(n, p),
                Sets.C(n, n-p));
    }

    /**
     * C(n,p) = C(n-1,p) + C(n-1,p-1)
     */
    @Test
    public void testPascalTriangle() {
        final int n = 10;
        final int p = 7;
        Assert.assertEquals(
                Sets.C(n, p),
                Sets.C(n-1, p) + Sets.C(n-1, p-1));
    }
}
