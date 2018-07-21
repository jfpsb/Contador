package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem implements Serializable {
    private int idcontagem;
    private Loja loja;
    private Date datainicio;
    private Date datafinal;

    public int getIdcontagem() {
        return idcontagem;
    }

    public void setIdcontagem(int idcontagem) {
        this.idcontagem = idcontagem;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public Date getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(Date datainicio) {
        this.datainicio = datainicio;
    }

    public Date getDatafinal() {
        return datafinal;
    }

    public void setDatafinal(Date datafinal) {
        this.datafinal = datafinal;
    }

    public static String[] getColunas() {
        return new String[] {"idcontagem as _id", "loja", "datainicio", "datafinal"};
    }
}
