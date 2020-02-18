package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Loja;

public class EntidadeMySQLLoja extends EntidadeMySQL {
    private Loja entidadeSalva;

    public Loja getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Loja entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
