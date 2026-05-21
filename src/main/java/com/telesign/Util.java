package com.telesign;

import java.security.SecureRandom;

/**
 * Utility class for helper functions.
 */
public class Util {

    private static SecureRandom random = new SecureRandom();

    /**
     * Constructor for Util.
     */
    private Util() {
    }

    /**
     * Helper function to generate a random number n digits in length using a system random.
     * 
     * @param n
     *            The number of digits the random number should be.
     * @return A random number n digits in length.
     */
    public static String randomWithNDigits(int n) {

        n = Math.abs(n);

        return String.format("%s", ((int) (Math.pow(10, (n - 1))) + random.nextInt((9 * (int) (Math.pow(10, (n - 1)))))));
    }
}

