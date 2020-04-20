package com.examples.common;

public class Utils {
    private Utils() {
        // hide default constructor
    }

    public static Integer toInteger(final Object object) {
        if (object == null) {
            return null;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String percentage(final int numerator, final int denominator, final int decimals) {
        if (denominator == 0) {
            throw new SampleException("denominator is zero");
        }
        return String.format("%." + decimals + "f", (100.0f * numerator) / denominator) + "%";
    }
}
