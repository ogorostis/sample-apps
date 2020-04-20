package com.examples.common;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SumsTest {
    @Test
    public void testSumN4() {
        Assert.assertEquals(Sums.firstN(4), 10);
    }

    @Test
    public void testSumN1() {
        Assert.assertEquals(Sums.firstN(1), 1);
    }

    @Test
    public void testSumNSquares4() {
        Assert.assertEquals(Sums.firstNSquares(4), 30);
    }

    @Test
    public void testSumNSquares1() {
        Assert.assertEquals(Sums.firstNSquares(1), 1);
    }
}
