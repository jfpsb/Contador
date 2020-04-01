package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;

import java.util.ArrayList;
import java.util.List;

public class FornecedorManager implements IManager<Fornecedor> {
    private Fornecedor fornecedor;
    private DAOFornecedor daoFornecedor;
    private ConexaoBanco conexaoBanco;

    public FornecedorManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        fornecedor = new Fornecedor();
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public void resetaModelo() {
        fornecedor = new Fornecedor();
    }

    @Override
    public Boolean salvar() {
        return daoFornecedor.inserir(fornecedor, true);
    }

    @Override
    public Boolean salvar(List<Fornecedor> lista) {
        return daoFornecedor.inserir(lista, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoFornecedor.atualizar(fornecedor, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoFornecedor.deletar(fornecedor, true);
    }

    @Override
    public List<Fornecedor> listar() {
        return daoFornecedor.listar();
    }

    @Override
    public Fornecedor listarPorId(Object... ids) {
        return daoFornecedor.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        fornecedor = daoFornecedor.listarPorId(ids);
    }

    public Fornecedor listarPorIdOuNome(String termo) {
        return daoFornecedor.listarPorIdOuNome(termo);
    }

    public Cursor listarPorCnpjNomeFantasiaCursor(String termo) {
        return daoFornecedor.listarPorCnpjNomeFantasiaCursor(termo);
    }

    public ArrayList<Fornecedor> listarPorCnpjNomeFantasia(String termo) {
        return daoFornecedor.listarPorCnpjNomeFantasia(termo);
    }
}
