package com.telesign;

import junit.framework.TestCase;

public class UtilTest extends TestCase {

    public void testRandomWithNDigits() {

        String randomWith5Digits = Util.randomWithNDigits(5);
        String randomWith3Digits = Util.randomWithNDigits(3);

        try {
            Integer.parseInt(randomWith5Digits);
        } catch (NumberFormatException e) {
            fail("randomWith5Digits is not digits");
        }

        assertEquals("randomWith5Digits is not requested length", 5, randomWith5Digits.length());
        assertEquals("randomWith3Digits is not requested length", 3, randomWith3Digits.length());
    }
}
