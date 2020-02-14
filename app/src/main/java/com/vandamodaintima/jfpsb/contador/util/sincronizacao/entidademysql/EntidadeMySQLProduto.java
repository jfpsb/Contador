package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Produto;

import org.simpleframework.xml.Element;

public class EntidadeMySQLProduto extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private Produto entidadeSalva;

    public Produto getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Produto entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
