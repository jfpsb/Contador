package com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql;

import com.vandamodaintima.jfpsb.contador.model.TipoContagem;

public class EntidadeMySQLTipoContagem extends EntidadeMySQL {
    private TipoContagem entidadeSalva;

    public TipoContagem getEntidadeSalva() {
        return entidadeSalva;
    }

    public void setEntidadeSalva(TipoContagem entidadeSalva) {
        this.entidadeSalva = entidadeSalva;
    }
}
