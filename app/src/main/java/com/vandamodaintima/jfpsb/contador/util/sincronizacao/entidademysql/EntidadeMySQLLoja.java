package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Loja;

import org.simpleframework.xml.Element;

public class EntidadeMySQLLoja extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private Loja entidadeSalva;

    public Loja getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Loja entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
