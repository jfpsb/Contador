package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

public class EntidadeMySQLContagemProduto extends EntidadeMySQL {
    private ContagemProduto entidadeSalva;

    public ContagemProduto getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(ContagemProduto entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
