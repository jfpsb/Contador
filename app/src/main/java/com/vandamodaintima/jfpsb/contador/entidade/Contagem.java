package com.vandamodaintima.jfpsb.contador.entidade;

import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem {
    private int idcontagem;
    private int loja;
    private String datainicio;
    private String datafim;

    public int getIdcontagem() {
        return idcontagem;
    }

    public void setIdcontagem(int idcontagem) {
        this.idcontagem = idcontagem;
    }

    public int getLoja() {
        return loja;
    }

    public void setLoja(int idloja) {
        this.loja = loja;
    }

    public String getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(String datainicio) {
        this.datainicio = datainicio;
    }

    public String getDatafim() {
        return datafim;
    }

    public void setDatafim(String datafim) {
        this.datafim = datafim;
    }
}
