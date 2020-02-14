package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

import org.simpleframework.xml.Element;

public class EntidadeMySQLFornecedor extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private Fornecedor entidadeSalva;

    public Fornecedor getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Fornecedor entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
