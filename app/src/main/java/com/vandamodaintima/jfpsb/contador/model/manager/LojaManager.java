package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;

import java.util.ArrayList;
import java.util.List;

public class LojaManager implements IManager<Loja> {
    private Loja loja;
    private DAOLoja daoLoja;
    private ConexaoBanco conexaoBanco;

    public LojaManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        loja = new Loja();
        daoLoja = new DAOLoja(conexaoBanco);
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    @Override
    public void resetaModelo() {
        loja = new Loja();
    }

    @Override
    public Boolean salvar() {
        return daoLoja.inserir(loja);
    }

    @Override
    public Boolean salvar(List<Loja> lista) {
        return daoLoja.inserir(lista);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoLoja.atualizar(loja, ids);
    }

    @Override
    public Boolean deletar() {
        return daoLoja.deletar(loja.getCnpj());
    }

    @Override
    public List<Loja> listar() {
        return daoLoja.listar();
    }

    @Override
    public Loja listarPorId(Object... ids) {
        return daoLoja.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        loja = listarPorId(ids);
    }

    public ArrayList<Loja> listarPorNomeCnpj(String termo) {
        return daoLoja.listarPorNomeCnpj(termo);
    }

    public ArrayList<Loja> listarMatrizes() {
        return daoLoja.listarMatrizes();
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return daoLoja.listarPorNomeCnpjCursor(termo);
    }
}
