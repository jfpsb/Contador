package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

public class DAOContagemProduto implements DAO<ContagemProduto> {
    private static final String TABELA = "contagem_produto";

    private SQLiteDatabase sqLiteDatabase;

    private DAOProduto daoProduto;
    private DAOContagem daoContagem;

    public DAOContagemProduto(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        daoProduto = new DAOProduto(sqLiteDatabase);
        daoContagem = new DAOContagem(sqLiteDatabase);
    }


    @Override
    public Boolean inserir(ContagemProduto contagemProduto) {

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", contagemProduto.getId());
            contentValues.put("produto", contagemProduto.getProduto().getCod_barra());
            contentValues.put("contagem_data", contagemProduto.getContagem().getSQLiteData());
            contentValues.put("contagem_loja", contagemProduto.getContagem().getLoja().getCnpj());
            contentValues.put("quant", contagemProduto.getQuant());

            sqLiteDatabase.insertOrThrow(TABELA, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        }

        return false;
    }

    @Override
    public Boolean atualizar(ContagemProduto contagemProduto, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        String id = String.valueOf(chaves[0]);

        long result = sqLiteDatabase.delete(TABELA, "id = ?", new String[]{id});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return sqLiteDatabase.query(TABELA, ContagemProduto.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public ContagemProduto listarPorId(Object... ids) {
        ContagemProduto contagemProduto = null;

        String id = String.valueOf(ids[0]);

        Cursor cursor = sqLiteDatabase.query(TABELA, ContagemProduto.getColunas(), "id = ?", new String[]{id}, null, null, null, null);

        if (cursor.getCount() > 0) {
            contagemProduto = new ContagemProduto();

            cursor.moveToFirst();

            contagemProduto.setId(Long.parseLong(id));
            contagemProduto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra"))));
            contagemProduto.setContagem(daoContagem.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja")), cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"))));
            contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
        }

        cursor.close();

        return contagemProduto;
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getSQLiteData();

        String sql = "SELECT id as _id, * FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? ORDER BY id";
        String[] selection = new String[]{loja, data};

        return sqLiteDatabase.rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getSQLiteData();

        String sql = "SELECT id as _id, SUM(quant) as quant * FROM contagem_produto, produto WHERE produto = cod_barra GROUP BY produto";

        return sqLiteDatabase.query(TABELA, ContagemProduto.getColunas(), "contagem_loja = ? AND contagem_data = ?", new String[]{loja, data}, null, null, null, null);
    }
}
