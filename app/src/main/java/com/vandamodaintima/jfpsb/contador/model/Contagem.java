package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contagem implements Serializable {
    private Loja loja;
    private Date data;
    private Boolean finalizada;

    private static final String[] colunas = new String[]{"ROWID as _id", "loja", "data", "finalizada"};

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public String getFullDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(data);
    }

    public String getShortDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(data);
    }

    public Date getData() {
        return data;
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
