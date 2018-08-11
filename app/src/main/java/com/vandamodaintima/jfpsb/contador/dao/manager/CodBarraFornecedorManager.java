package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOCodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import java.util.ArrayList;

public class CodBarraFornecedorManager extends Manager<CodBarraFornecedor> {
    public CodBarraFornecedorManager(ConexaoBanco conexao) {
        daoEntidade = new DAOCodBarraFornecedor(conexao.conexao());
    }

    @Override
    public ArrayList<CodBarraFornecedor> listar() {
        throw new NotImplementedException("Esse Método Não É Necessário");
    }

    @Override
    public Cursor listarCursor() {
        throw new NotImplementedException("Esse Método Não É Necessário");
    }

    @Override
    public CodBarraFornecedor listarPorChave(Object... chaves) {
        throw new NotImplementedException("Esse Método Não É Necessário");
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        throw new NotImplementedException("Esse Método Não É Necessário");
    }

    public ArrayList<CodBarraFornecedor> listarCodBarraFornecedorPorProduto(Produto produto) {
        ArrayList<CodBarraFornecedor> codigos = new ArrayList<>();

        Cursor c = listarCursorCodBarraFornecedorPorProduto(produto);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                CodBarraFornecedor codigo = new CodBarraFornecedor();

                codigo.setCodigo(c.getString(c.getColumnIndexOrThrow("codigo")));

                codigos.add(codigo);
            }
        }

        return codigos;
    }

    public Cursor listarCursorCodBarraFornecedorPorProduto(Produto produto) {
        String cod_barra = produto.getCod_barra();

        return daoEntidade.select("produto = ?", new String[]{cod_barra}, null, null, "codigo", null);
    }
}
