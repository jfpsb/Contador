package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;

import java.util.ArrayList;
import java.util.List;

public class TipoContagemManager implements IManager<TipoContagem> {
    private TipoContagem tipoContagem;
    private DAOTipoContagem daoTipoContagem;
    private ConexaoBanco conexaoBanco;

    public TipoContagemManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        tipoContagem = new TipoContagem();
        daoTipoContagem = new DAOTipoContagem(conexaoBanco);
    }

    @Override
    public void resetaModelo() {
        tipoContagem = new TipoContagem();
    }

    @Override
    public Boolean salvar() {
        return daoTipoContagem.inserir(tipoContagem, true, true);
    }

    @Override
    public Boolean salvar(List<TipoContagem> lista) {
        return daoTipoContagem.inserir(lista, true, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoTipoContagem.atualizar(tipoContagem, true, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoTipoContagem.deletar(tipoContagem, true, true);
    }

    @Override
    public List<TipoContagem> listar() {
        return daoTipoContagem.listar();
    }

    @Override
    public TipoContagem listarPorId(Object... ids) {
        return daoTipoContagem.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        tipoContagem = daoTipoContagem.listarPorId(ids);
    }

    public Cursor listarPorNomeCursor(String nome) {
        return daoTipoContagem.listarPorNomeCursor(nome);
    }

    public ArrayList<TipoContagem> listarPorNome(String nome) {
        return daoTipoContagem.listarPorNome(nome);
    }
}
