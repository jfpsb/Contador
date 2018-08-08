package com.vandamodaintima.jfpsb.contador.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrataDisplayData {
    private static final SimpleDateFormat FormatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat FormatoDataDisplay = new SimpleDateFormat("dd/MM/yyyy");

    public static Date getDataBD(String dataEmString) {
        try {
            Date data = FormatoBD.parse(dataEmString);
            return data;
        }
        catch (ParseException e) {
            Log.i("Contador", e.getMessage());
            return null;
        }
    }

    public static Date getDataDisplay(String dataEmString) {
        try {
            Date data = FormatoDataDisplay.parse(dataEmString);
            return data;
        }
        catch (ParseException e) {
            Log.i("Contador", e.getMessage());
            return null;
        }
    }

    public static String getDataFormatoDisplay(Date date) {
        return FormatoDataDisplay.format(date);
    }

    public static String getDataFormatoDisplay(String dataEmFormatoBD) {
        Date date = getDataBD(dataEmFormatoBD);
        return FormatoDataDisplay.format(date);
    }

    public static String getDataFormatoBD(Date date) {
        return FormatoBD.format(date);
    }
}
