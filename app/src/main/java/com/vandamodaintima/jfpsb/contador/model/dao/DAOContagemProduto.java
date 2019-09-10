package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

import java.util.ArrayList;
import java.util.List;

public class DAOContagemProduto implements IDAO<ContagemProduto> {
    private static final String TABELA = "contagem_produto";
    private ConexaoBanco conexaoBanco;
    private DAOProduto daoProduto;
    private DAOContagem daoContagem;

    public DAOContagemProduto(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoProduto = new DAOProduto(conexaoBanco);
        daoContagem = new DAOContagem(conexaoBanco);
    }

    @Override
    public Boolean inserir(ContagemProduto contagemProduto) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", contagemProduto.getId());
            contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
            contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<ContagemProduto> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(ContagemProduto contagemProduto, Object... chaves) {
        try {
            long id = (long) chaves[0];

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
            contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().update(TABELA, contentValues, "id = ?", new String[]{String.valueOf(id)});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR MARCA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        long id = (long) chaves[0];
        int result = conexaoBanco.conexao().delete(TABELA, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, ContagemProduto.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<ContagemProduto> listar() {
        ArrayList<ContagemProduto> contagem_produtos = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagem_produto = new ContagemProduto();

                contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                contagem_produto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

                String contagem_loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
                String contagem_data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
                contagem_produto.setContagem(daoContagem.listarPorId(contagem_loja, contagem_data));

                contagem_produto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));

                contagem_produtos.add(contagem_produto);
            }
        }
        cursor.close();
        return contagem_produtos;
    }

    @Override
    public ContagemProduto listarPorId(Object... ids) {
        ContagemProduto contagem_produto = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ContagemProduto.getColunas(), "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            contagem_produto = new ContagemProduto();
            contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            contagem_produto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

            String contagem_loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
            String contagem_data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
            contagem_produto.setContagem(daoContagem.listarPorId(contagem_loja, contagem_data));

            contagem_produto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
        }

        cursor.close();
        return contagem_produto;
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getDataParaSQLite();

        String sql = "SELECT id as _id, * FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? ORDER BY id";
        String[] selection = new String[]{loja, data};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getDataParaSQLite();

        String sql = "SELECT id as _id, SUM(quant) as quant, produto, descricao, contagem_loja, contagem_data FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? GROUP BY produto ORDER BY descricao";
        String[] selection = new String[]{loja, data};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByProduto(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemGroupByProdutoCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();

                contagemProduto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));

                String loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
                String data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
                contagemProduto.setContagem(daoContagem.listarPorId(loja, data));

                String p = cursor.getString(cursor.getColumnIndexOrThrow("produto"));
                contagemProduto.setProduto(daoProduto.listarPorId(p));

                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }
}
