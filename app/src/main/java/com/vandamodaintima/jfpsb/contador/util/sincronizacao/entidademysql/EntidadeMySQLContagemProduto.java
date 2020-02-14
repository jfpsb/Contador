package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

import org.simpleframework.xml.Element;

public class EntidadeMySQLContagemProduto extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private ContagemProduto entidadeSalva;

    public ContagemProduto getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(ContagemProduto entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
