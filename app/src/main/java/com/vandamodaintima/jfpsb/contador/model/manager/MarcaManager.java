package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.CadastrarFornecedorController;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;

import java.util.ArrayList;
import java.util.List;

public class MarcaManager implements IManager<Marca> {
    private Marca marca;
    private DAOMarca daoMarca;
    private ConexaoBanco conexaoBanco;

    public MarcaManager(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        marca = new Marca();
        daoMarca = new DAOMarca(conexaoBanco);
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public void resetaModelo() {
        marca = new Marca();
    }

    @Override
    public Boolean salvar() {
        return daoMarca.inserir(marca, true);
    }

    @Override
    public Boolean salvar(List<Marca> lista) {
        return daoMarca.inserir(lista, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoMarca.atualizar(marca, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoMarca.deletar(marca, true);
    }

    @Override
    public List<Marca> listar() {
        return daoMarca.listar();
    }

    @Override
    public Marca listarPorId(Object... ids) {
        return daoMarca.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        marca = daoMarca.listarPorId(ids);
    }

    public Cursor listarPorNomeCursor(String nome) {
        return daoMarca.listarPorNomeCursor(nome);
    }

    public ArrayList<Marca> listarPorNome(String nome) {
        return daoMarca.listarPorNome(nome);
    }
}
