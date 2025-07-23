package com.samha.util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GenerateAlphaNumericString {
    public static String getRandomString(int i) {

        // bind the length
        byte[] bytearray = new byte[1000];
        String mystring;
        StringBuilder thebuffer;
        String theAlphaNumericS;

        new Random().nextBytes(bytearray);

        mystring = new String(bytearray, StandardCharsets.UTF_8);

        thebuffer = new StringBuilder();

        //remove all spacial char
        theAlphaNumericS = mystring.replaceAll("[^A-Z0-9]", "");

        //random selection
        for (int m = 0; m < theAlphaNumericS.length(); m++) {

            if (Character.isLetter(theAlphaNumericS.charAt(m)) && (i > 0) || Character.isDigit(theAlphaNumericS.charAt(m)) && (i > 0)) {
                thebuffer.append(theAlphaNumericS.charAt(m));
                i--;
            }
        }

        // the resulting string
        return thebuffer.toString();
    }
}
