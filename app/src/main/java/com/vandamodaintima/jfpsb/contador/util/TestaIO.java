package com.vandamodaintima.jfpsb.contador.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jfpsb on 14/02/2018.
 */

public class TestaIO {
    public static boolean isValidDate(String date, SimpleDateFormat dateFormat) {
        dateFormat.setLenient(false);
        try {
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
