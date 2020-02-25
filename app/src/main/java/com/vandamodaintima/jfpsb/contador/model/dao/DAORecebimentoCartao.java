package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;

import java.util.List;

public class DAORecebimentoCartao implements IDAO<RecebimentoCartao> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "recebimentocartao";
    private DAOFornecedor daoFornecedor;

    public DAORecebimentoCartao(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @Override
    public Boolean inserir(RecebimentoCartao recebimentoCartao) {
        return null;
    }

    @Override
    public Boolean inserir(List<RecebimentoCartao> lista) {
        return null;
    }

    @Override
    public Boolean inserirOuAtualizar(RecebimentoCartao recebimentoCartao) {
        return null;
    }

    @Override
    public Boolean inserirOuAtualizar(List<RecebimentoCartao> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(RecebimentoCartao recebimentoCartao, Object... chaves) {
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
    public List<RecebimentoCartao> listar() {
        return null;
    }

    @Override
    public RecebimentoCartao listarPorId(Object... ids) {
        return null;
    }
}
