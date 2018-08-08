package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem implements Serializable {
    private int rowid;
    private Loja loja;
    private Date data;
    private Boolean finalizada;

    private static final String[] colunas = new String[] { "rowid as _id", "loja", "data", "finalizada" };

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
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
}
