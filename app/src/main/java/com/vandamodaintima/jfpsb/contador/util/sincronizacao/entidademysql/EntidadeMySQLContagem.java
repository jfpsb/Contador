package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.Contagem;

import org.simpleframework.xml.Element;

public class EntidadeMySQLContagem extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private Contagem entidadeSalva;

    public Contagem getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(Contagem entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
