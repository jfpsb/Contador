package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

public class EntidadeMySQLFornecedor extends EntidadeMySQL {
    private Fornecedor entidadeSalva;

    public Fornecedor getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Fornecedor entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
