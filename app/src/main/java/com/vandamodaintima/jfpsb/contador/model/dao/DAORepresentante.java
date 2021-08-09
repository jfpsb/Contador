package com.vandamodaintima.jfpsb.contador.model.dao;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Representante;

import java.util.List;

public class DAORepresentante extends ADAO<Representante> {
    DAORepresentante(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
    }

    @Override
    public Boolean inserir(Representante representante) {
        return null;
    }

    @Override
    public Boolean inserir(List<Representante> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(Representante representante) {
        return null;
    }

    @Override
    public List<Representante> listar() {
        return null;
    }

    @Override
    public Representante listarPorId(Object... ids) {
        return null;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
