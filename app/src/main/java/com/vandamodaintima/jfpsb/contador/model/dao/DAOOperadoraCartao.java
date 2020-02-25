package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.OperadoraCartao;

import java.util.List;

public class DAOOperadoraCartao implements IDAO<OperadoraCartao> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "operadoracartao";
    private DAOFornecedor daoFornecedor;

    public DAOOperadoraCartao(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @Override
    public Boolean inserir(OperadoraCartao operadoraCartao) {
        return null;
    }

    @Override
    public Boolean inserir(List<OperadoraCartao> lista) {
        return null;
    }

    @Override
    public Boolean inserirOuAtualizar(OperadoraCartao operadoraCartao) {
        return null;
    }

    @Override
    public Boolean inserirOuAtualizar(List<OperadoraCartao> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(OperadoraCartao operadoraCartao, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public Cursor listarCursor() {
        return null;
    }

    @Override
    public List<OperadoraCartao> listar() {
        return null;
    }

    @Override
    public OperadoraCartao listarPorId(Object... ids) {
        return null;
    }
}
