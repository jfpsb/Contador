package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DAOContagemProduto extends ADAO<ContagemProduto> {
    private DAOProduto daoProduto;
    private DAOContagem daoContagem;

    public DAOContagemProduto(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "contagem_produto";
        daoProduto = new DAOProduto(conexaoBanco);
        daoContagem = new DAOContagem(conexaoBanco);
    }

    @Override
    public Boolean inserir(ContagemProduto contagemProduto, boolean writeToJson, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", (long) contagemProduto.getIdentifier());
            contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
            contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(contagemProduto, writeToJson, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<ContagemProduto> lista, boolean writeToJson, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (ContagemProduto contagemProduto : lista) {

                ContentValues contentValues = new ContentValues();

                contentValues.put("id", (long) contagemProduto.getIdentifier());
                contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
                contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
                contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
                contentValues.put("quant", contagemProduto.getQuant());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(lista, writeToJson, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(ContagemProduto contagemProduto, boolean writeToJson, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", (long) contagemProduto.getIdentifier());
            contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
            contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserirOuAtualizar(contagemProduto, writeToJson, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(List<ContagemProduto> lista, boolean writeToJson, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (ContagemProduto contagemProduto : lista) {

                ContentValues contentValues = new ContentValues();

                contentValues.put("id", (long) contagemProduto.getIdentifier());
                contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
                contentValues.put("contagem_data", contagemProduto.getContagem().getDataParaSQLite());
                contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
                contentValues.put("quant", contagemProduto.getQuant());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserirOuAtualizar(lista, writeToJson, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(ContagemProduto contagemProduto, boolean writeToJson, boolean sendToServer, Object... chaves) {
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

            return super.atualizar(contagemProduto, writeToJson, sendToServer, chaves);
        } catch (SQLException | IOException ex) {
            Log.e(ActivityBaseView.LOG, "ERRO AO ATUALIZAR MARCA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
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

                contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
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

        String sql = "SELECT id as _id, SUM(quant) as quant, preco, produto, descricao, contagem_loja, contagem_data FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? GROUP BY produto ORDER BY descricao";
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
