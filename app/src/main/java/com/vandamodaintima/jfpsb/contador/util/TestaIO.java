package com.vandamodaintima.jfpsb.contador.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jfpsb on 14/02/2018.
 */

public class TestaIO {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

    public static boolean isValidDouble(String num) {
        try {
            Double.parseDouble(num);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidInt(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
