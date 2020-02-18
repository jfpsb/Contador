package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Marca;

public class EntidadeMySQLMarca extends EntidadeMySQL {
    private Marca entidadeSalva;

    public Marca getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Marca entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
