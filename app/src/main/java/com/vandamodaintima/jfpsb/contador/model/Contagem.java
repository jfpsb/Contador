package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem implements Serializable {
    private Loja loja;
    private Date data;
    private Boolean finalizada;

    private static final String[] colunas = new String[] { "loja", "data", "finalizada" };

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public String getDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(data);
    }

    public static String getDataString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public Date getData() {
        return data;
    }

    public static Date getData(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return simpleDateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public static String[] getColunas() {
        return colunas;
    }

    public static String getDataSQLite(Date data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(data);
    }
}
