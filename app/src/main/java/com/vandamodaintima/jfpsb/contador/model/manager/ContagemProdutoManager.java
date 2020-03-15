package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;

import java.util.ArrayList;
import java.util.List;

public class ContagemProdutoManager implements IManager<ContagemProduto> {
    private ContagemProduto contagemProduto;
    private DAOContagemProduto daoContagemProduto;
    private ConexaoBanco conexaoBanco;

    public ContagemProdutoManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        contagemProduto = new ContagemProduto();
        daoContagemProduto = new DAOContagemProduto(conexaoBanco);

    }

    public ContagemProduto getContagemProduto() {
        return contagemProduto;
    }

    public void setContagemProduto(ContagemProduto contagemProduto) {
        this.contagemProduto = contagemProduto;
    }

    @Override
    public void resetaModelo() {
        contagemProduto = new ContagemProduto();
    }

    @Override
    public Boolean salvar() {
        return daoContagemProduto.inserir(contagemProduto, true, true);
    }

    @Override
    public Boolean salvar(List<ContagemProduto> lista) {
        return daoContagemProduto.inserir(lista, true, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoContagemProduto.atualizar(contagemProduto, true, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoContagemProduto.deletar(contagemProduto, true, true);
    }

    @Override
    public List<ContagemProduto> listar() {
        return daoContagemProduto.listar();
    }

    @Override
    public ContagemProduto listarPorId(Object... ids) {
        return daoContagemProduto.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        contagemProduto = listarPorId(ids);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagem) {
        return daoContagemProduto.listarPorContagemGroupByProdutoCursor(contagem);
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByProduto(Contagem contagem) {
        return daoContagemProduto.listarPorContagemGroupByProduto(contagem);
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        return daoContagemProduto.listarPorContagemCursor(contagem);
    }
}
