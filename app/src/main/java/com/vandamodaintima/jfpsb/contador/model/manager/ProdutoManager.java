package com.vandamodaintima.jfpsb.contador.model.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoManager implements IManager<Produto> {
    private Produto produto;
    private DAOProduto daoProduto;

    public ProdutoManager(ConexaoBanco conexaoBanco) {
        produto = new Produto();
        daoProduto = new DAOProduto(conexaoBanco);
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Boolean salvar() {
        return daoProduto.inserir(produto, true, true);
    }

    @Override
    public Boolean salvar(List<Produto> lista) {
        return daoProduto.inserir(lista, true, true);
    }

    @Override
    public Boolean atualizar(Object... ids) {
        return daoProduto.atualizar(produto, true, true, ids);
    }

    @Override
    public Boolean deletar() {
        return daoProduto.deletar(produto, true, true);
    }

    public List<Produto> listar() {
        return daoProduto.listar();
    }

    @Override
    public Produto listarPorId(Object... ids) {
        return daoProduto.listarPorId(ids);
    }

    @Override
    public void resetaModelo() {
        produto = new Produto();
    }

    @Override
    public void load(Object... ids) {
        produto = daoProduto.listarPorId(ids);
    }

    public Cursor listarPorCodBarraCursor(String cod_barra) {
        return daoProduto.listarPorCodBarraCursor(cod_barra);
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        return daoProduto.listarPorCodBarra(cod_barra);
    }

    public Cursor listarPorDescricaoCursor(String descricao) {
        return daoProduto.listarPorDescricaoCursor(descricao);
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        return daoProduto.listarPorDescricao(descricao);
    }

    public Cursor listarPorMarcaCursor(String marca) {
        return daoProduto.listarPorMarcaCursor(marca);
    }

    public Cursor listarPorFornecedorCursor(String fornecedor) {
        return daoProduto.listarPorFornecedorCursor(fornecedor);
    }

    public ArrayList<Produto> listarPorMarca(String marca) {
        return daoProduto.listarPorMarca(marca);
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        return daoProduto.listarPorFornecedor(fornecedor);
    }
}
