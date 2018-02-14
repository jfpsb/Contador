package com.vandamodaintima.jfpsb.contador.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jfpsb on 14/02/2018.
 */

public class TestaIO {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isValidDate(String date) {
        dateFormat.setLenient(false);
        try {
            if(date.isEmpty())
                return true;

            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isStringEmpty(String string) {
        if(string.isEmpty())
            return  true;

        return false;
    }
}
