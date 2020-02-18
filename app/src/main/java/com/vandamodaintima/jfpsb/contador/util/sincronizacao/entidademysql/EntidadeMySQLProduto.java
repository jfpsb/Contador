package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Produto;

public class EntidadeMySQLProduto extends EntidadeMySQL {
    private Produto entidadeSalva;

    public Produto getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Produto entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
