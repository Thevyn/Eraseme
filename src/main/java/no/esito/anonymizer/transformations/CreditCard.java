/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.transformations;

import no.esito.anonymizer.ITransformation;

/**
 * Credit card transformation. It will replace the last digit of the card number to make it validate with MOD-10
 * checksum.
 */
public class CreditCard implements ITransformation {

    public static final String LABEL = "CreditCard - Adjusts the last digit for MOD10 validation";

    @Override
    public String transform(String input) {
        return toCreditCard(input);
    }

    /**
     * Fix the last digit of a credit card number.
     *
     * @param input number string to be changed
     * @return corrected number
     */
    public static String toCreditCard(String input) {
        if (input != null) {
            String s1 = input.substring(0, input.length() - 1);
            return s1 + mod10CheckDigit(s1);
        }
        return null;
    }

    /**
     * MOD 10 - Luhn algorithm for Credit Card encoding.
     *
     * @param value string of digits
     * @return MOD10 checksum
     */
    public static String mod10CheckDigit(String value) {
        int[] digits = getDigits(value);
        /* double every other starting from right - jumping from 2 in 2 */
        for (int i = digits.length - 1; i >= 0; i -= 2) {
            digits[i] += digits[i];
            /*
             * taking the sum of digits grater than 10 - simple trick by substract 9
             */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }
        /* multiply by 9 step */
        sum = sum * 9;
        /* convert to string to be easier to take the last digit */
        String digit = sum + "";
        return digit.substring(digit.length() - 1);
    }

    /**
     * Transform a string of digits to array of integers.
     *
     * @param chars string of digits
     * @return array of digits
     */
    public static int[] getDigits(String chars) {
        int[] digits = new int[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            digits[i] = Character.getNumericValue(chars.charAt(i));
        }
        return digits;
    }

    /**
     * Test of LUHN algorithm.
     *
     * @param args empty
     */
    public static void main(String[] args) {
        String cc = "4569971008022748";
        System.out.println("Encode CC number '" + cc + "': " + toCreditCard(cc));
    }

}
