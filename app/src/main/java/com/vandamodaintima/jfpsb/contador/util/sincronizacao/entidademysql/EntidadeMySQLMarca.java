package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Marca;

import org.simpleframework.xml.Element;

public class EntidadeMySQLMarca extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private Marca entidadeSalva;

    public Marca getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Marca entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
