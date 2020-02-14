package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.TipoContagem;

import org.simpleframework.xml.Element;

public class EntidadeMySQLTipoContagem extends EntidadeMySQL {
    @Element(name = "EntidadeSalva")
    private TipoContagem entidadeSalva;

    public TipoContagem getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(TipoContagem entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
