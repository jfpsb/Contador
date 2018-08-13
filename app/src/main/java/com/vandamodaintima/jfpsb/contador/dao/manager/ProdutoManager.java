package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.CodBarraFornecedorArrayAdapter;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

import java.util.ArrayList;

public class ProdutoManager extends Manager<Produto> {
    FornecedorManager fornecedorManager;
    MarcaManager marcaManager;
    CodBarraFornecedorManager codBarraFornecedorManager;

    public ProdutoManager(ConexaoBanco conexao) {
        daoEntidade = new DAOProduto(conexao.conexao());
        fornecedorManager = new FornecedorManager(conexao);
        marcaManager = new MarcaManager(conexao);
        codBarraFornecedorManager = new CodBarraFornecedorManager(conexao);
    }

    @Override
    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor c = listarCursor();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(c.getString(c.getColumnIndexOrThrow("_id")));

                Fornecedor fornecedor = fornecedorManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("fornecedor")));
                produto.setFornecedor(fornecedor);

                Marca marca = marcaManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("marca")));
                produto.setMarca(marca);

                produto.setDescricao(c.getString(c.getColumnIndexOrThrow("descricao")));
                produto.setPreco(c.getDouble(c.getColumnIndexOrThrow("preco")));

                ArrayList<CodBarraFornecedor> codigos = codBarraFornecedorManager.listarCodBarraFornecedorPorProduto(produto);

                for (CodBarraFornecedor codigo : codigos) {
                    codigo.setProduto(produto);
                }

                produto.setCod_barra_fornecedor(codigos);

                produtos.add(produto);
            }
        }

        return produtos;
    }

    @Override
    public Cursor listarCursor() {
        return daoEntidade.select(null, null, null, null, "descricao", null);
    }

    public Cursor listarCursorPorCodBarra(String cod_barra) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND cod_barra LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + cod_barra + "%"};

        return daoEntidade.selectRaw(sql, selection);
    }

    public Cursor listarCursorPorDescricao(String descricao) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND descricao LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + descricao + "%"};

        return daoEntidade.selectRaw(sql, selection);
    }

    public Cursor listarCursorPorFornecedor(String nome) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + nome + "%"};

        return daoEntidade.selectRaw(sql, selection);
    }

    @Override
    public Produto listarPorChave(Object... chaves) {
        Produto produto = null;
        Cursor c = listarCursorPorChave(chaves[0]);

        if (c.getCount() > 0) {
            c.moveToFirst();

            produto = new Produto();

            produto.setCod_barra(c.getString(c.getColumnIndexOrThrow("_id")));

            Fornecedor fornecedor = fornecedorManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("fornecedor")));
            produto.setFornecedor(fornecedor);

            Marca marca = marcaManager.listarPorChave(c.getLong(c.getColumnIndexOrThrow("marca")));
            produto.setMarca(marca);

            produto.setDescricao(c.getString(c.getColumnIndexOrThrow("descricao")));
            produto.setPreco(c.getDouble(c.getColumnIndexOrThrow("preco")));

            ArrayList<CodBarraFornecedor> codigos = codBarraFornecedorManager.listarCodBarraFornecedorPorProduto(produto);

            for (CodBarraFornecedor codigo : codigos) {
                codigo.setProduto(produto);
            }

            produto.setCod_barra_fornecedor(codigos);
        }

        return produto;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        return daoEntidade.select("cod_barra = ?", new String[]{String.valueOf(chaves[0])}, null, null, "descricao", null);
    }
}