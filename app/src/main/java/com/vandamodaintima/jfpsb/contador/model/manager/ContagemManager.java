package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContagemManager implements IManager<Contagem> {
    private Contagem contagem;
    private DAOContagem daoContagem;
    private ConexaoBanco conexaoBanco;

    public ContagemManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        contagem = new Contagem();
        daoContagem = new DAOContagem(conexaoBanco);
    }

    public Contagem getContagem() {
        return contagem;
    }

    public void setContagem(Contagem contagem) {
        this.contagem = contagem;
    }

    @Override
    public void resetaModelo() {
        contagem = new Contagem();
    }

    @Override
    public Boolean salvar() {
        return daoContagem.inserir(contagem);
    }

    @Override
    public Boolean salvar(List<Contagem> lista) {
        return daoContagem.inserir(lista);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoContagem.atualizar(contagem, ids);
    }

    @Override
    public Boolean deletar() {
        return daoContagem.deletar(contagem.getLoja().getCnpj(), contagem.getDataParaSQLite());
    }

    @Override
    public List<Contagem> listar() {
        return daoContagem.listar();
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        return daoContagem.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        contagem = daoContagem.listarPorId(ids);
    }

    public Cursor listarPorLojaPeriodoCursor(String loja, Calendar dataInicial, Calendar dataFinal) {
        return daoContagem.listarPorLojaPeriodoCursor(loja, dataInicial, dataFinal);
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String cnpj, Calendar dataInicial, Calendar dataFinal) {
        return daoContagem.listarPorLojaPeriodo(cnpj, dataInicial, dataFinal);
    }
}