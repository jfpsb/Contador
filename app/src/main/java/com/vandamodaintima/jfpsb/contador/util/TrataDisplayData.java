package com.vandamodaintima.jfpsb.contador.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrataDisplayData {
    private static final SimpleDateFormat FormatoBD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat FormatoDataDisplay = new SimpleDateFormat("dd/MM/yyyy");

    public static Date getData(String dataEmString) {
        try {
            Date data = FormatoBD.parse(dataEmString);
            return data;
        }
        catch (ParseException e) {
            Log.i("Contador", e.getMessage());
            return null;
        }
    }

    public static String getDataEmStringDisplay(Date date) {
        return FormatoDataDisplay.format(date);
    }

    public static String getDataEmString(Date date) {
        return FormatoBD.format(date);
    }

    //TODO: fazer método para mostrar data no formato dd-MM-yyyy
}
