package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.ArrayList;

public class DAOProduto implements DAO<Produto> {

    private static final String TABELA = "produto";

    private SQLiteDatabase sqLiteDatabase;

    public DAOProduto(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public Boolean inserir(Produto produto) {
        try {
            sqLiteDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCod_barra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());

            if (produto.getFornecedor() != null) {
                contentValues.put("fornecedor", produto.getFornecedor().getCnpj());
            } else {
                contentValues.putNull("fornecedor");
            }

            if (produto.getMarca() != null) {
                contentValues.put("marca", produto.getMarca().getId());
            } else {
                contentValues.putNull("marca");
            }

            sqLiteDatabase.insertOrThrow(TABELA, null, contentValues);

            for (int i = 0; i < produto.getCod_barra_fornecedor().size(); i++) {
                String codigo = produto.getCod_barra_fornecedor().get(i);

                ContentValues content = new ContentValues();

                content.put("produto", produto.getCod_barra());
                content.put("codigo", codigo);

                sqLiteDatabase.insertOrThrow("codbarrafornecedor", null, content);
            }

            sqLiteDatabase.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(Produto produto, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABELA, null, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
                produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            }
        }

        cursor.close();

        return produtos;
    }

    @Override
    public ArrayList<Produto> listar(String selection, String[] args) {
        return null;
    }

    @Override
    public Produto listarPorId(Object... ids) {
        Produto produto = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            produto = new Produto();

            produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

            produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
            produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();

        return produto;
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

//        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND cod_barra LIKE ? ORDER BY descricao";
//
//        String[] selection = new String[]{"%" + cod_barra + "%"};

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cod_barra LIKE ?", new String[]{"%" + cod_barra + "%"}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        ArrayList<Produto> produtos = new ArrayList<>();

        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND descricao LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + descricao + "%"};

        Cursor cursor = sqLiteDatabase.rawQuery(sql, selection);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }

    public ArrayList<Produto> listarPorMarca(String marca) {
        ArrayList<Produto> produtos = new ArrayList<>();

        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN marca ON produto.marca = marca.codigo WHERE (marca = codigo OR marca IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + marca + "%"};

        Cursor cursor = sqLiteDatabase.rawQuery(sql, selection);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        ArrayList<Produto> produtos = new ArrayList<>();

        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + fornecedor + "%"};

        Cursor cursor = sqLiteDatabase.rawQuery(sql, selection);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }
}