package com.vandamodaintima.jfpsb.contador.entidade;

import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem {
    private int idcontagem;
    private int loja;
    private Date datainicio;
    private Date datafim;

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

    public Date getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(Date datainicio) {
        this.datainicio = datainicio;
    }

    public Date getDatafim() {
        return datafim;
    }

    public void setDatafim(Date datafim) {
        this.datafim = datafim;
    }
}
