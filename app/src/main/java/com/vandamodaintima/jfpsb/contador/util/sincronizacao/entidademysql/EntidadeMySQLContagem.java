package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Contagem;

public class EntidadeMySQLContagem extends EntidadeMySQL {
    private Contagem entidadeSalva;

    public Contagem getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Contagem entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
