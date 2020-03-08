package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;

import org.threeten.bp.LocalDateTime;

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
        return daoContagem.inserir(contagem, true, true);
    }

    @Override
    public Boolean salvar(List<Contagem> lista) {
        return daoContagem.inserir(lista, true, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoContagem.atualizar(contagem, true, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoContagem.deletar(contagem, true, true);
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

    public Cursor listarPorLojaPeriodoCursor(String loja, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return daoContagem.listarPorLojaPeriodoCursor(loja, dataInicial, dataFinal);
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String cnpj, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return daoContagem.listarPorLojaPeriodo(cnpj, dataInicial, dataFinal);
    }
}
