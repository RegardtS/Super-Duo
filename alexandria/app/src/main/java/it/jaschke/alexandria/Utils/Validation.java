package it.jaschke.alexandria.Utils;

import android.widget.Toast;

/**
 * Created by regardtschindler on 2015/10/22.
 */
public class Validation {

    public boolean validateIsbn10(String isbn) {
        if (isbn == null) {
            return false;
        }

        //remove any hyphens
        isbn = isbn.replaceAll("-", "");

        //must be a 10 digit ISBN
        if (isbn.length() != 10) {
            return false;
        }

        try {
            int tot = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                tot += ((10 - i) * digit);
            }

            String checksum = Integer.toString((11 - (tot % 11)) % 11);
            if ("10".equals(checksum)) {
                checksum = "X";
            }

            return checksum.equals(isbn.substring(9));
        } catch (NumberFormatException nfe) {
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
    }


}
