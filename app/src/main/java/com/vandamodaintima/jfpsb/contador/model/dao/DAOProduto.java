package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import java.util.ArrayList;

public class DAOProduto implements DAO<Produto> {

    private static final String TABELA = "produto";

    private SQLiteDatabase sqLiteDatabase;

    private DAOFornecedor daoFornecedor;
    private DAOMarca daoMarca;

    public DAOProduto(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
        daoMarca = new DAOMarca(sqLiteDatabase);
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

                sqLiteDatabase.insertOrThrow("cod_barra_fornecedor", null, content);
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

    public Boolean importarDeExcel(ArrayList<Produto> produtos, TelaProduto.ImportarProdutoAsyncTask.Progresso progresso) {
        try {
            if(produtos.size() == 0) {
                throw new Exception("Lista de Produtos Está Vazia");
            }

            sqLiteDatabase.beginTransaction();

            for (Produto produto : produtos) {
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

                sqLiteDatabase.insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                for (int i = 0; i < produto.getCod_barra_fornecedor().size(); i++) {
                    String codigo = produto.getCod_barra_fornecedor().get(i);

                    ContentValues content = new ContentValues();

                    content.put("produto", produto.getCod_barra());
                    content.put("codigo", codigo);

                    sqLiteDatabase.insertWithOnConflict("cod_barra_fornecedor", null, content, SQLiteDatabase.CONFLICT_IGNORE);
                }

                progresso.publish("Produto " + produto.getCod_barra() + " - " + produto.getDescricao() + " Cadastrado");
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
        String cod_barra = String.valueOf(chaves[0]);

        try {
            sqLiteDatabase.beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());

            sqLiteDatabase.delete("cod_barra_fornecedor", "produto = ?", new String[]{cod_barra});

            for (String codigo : produto.getCod_barra_fornecedor()) {
                ContentValues contentCodigos = new ContentValues();

                contentCodigos.put("produto", cod_barra);
                contentCodigos.put("codigo", codigo);

                sqLiteDatabase.insertOrThrow("cod_barra_fornecedor", null, contentCodigos);
            }

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

            sqLiteDatabase.update(TABELA, contentValues, "cod_barra = ?", new String[]{cod_barra});

            sqLiteDatabase.setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR PRODUTO", ex);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        String cod_barra = String.valueOf(chaves[0]);

        int result = sqLiteDatabase.delete(TABELA, "cod_barra = ?", new String[]{cod_barra});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return sqLiteDatabase.query(TABELA, null, null, null, null, null, null, null);
    }

    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", null, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    produto.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                produto.setMarca(daoMarca.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
                produto.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
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

            Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", new String[]{"ROWID as _id", "codigo", "produto"}, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

            if (cursorCodigoBarraFornecedor.getCount() > 0) {
                ArrayList<String> codigos = new ArrayList<>();

                while (cursorCodigoBarraFornecedor.moveToNext()) {
                    codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                }

                produto.setCod_barra_fornecedor(codigos);
            }

            cursorCodigoBarraFornecedor.close();

            produto.setMarca(daoMarca.listarPorId(cursor.getLong(cursor.getColumnIndexOrThrow("marca"))));
            produto.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();

        return produto;
    }

    public Cursor listarPorCodBarraCursor(String cod_barra) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN cod_barra_fornecedor ON produto.cod_barra = cod_barra_fornecedor.produto WHERE produto.cod_barra LIKE ? OR cod_barra_fornecedor.codigo LIKE ? ORDER BY cod_barra";

        String[] selection = new String[] { "%" + cod_barra + "%", "%" + cod_barra + "%" };

        return sqLiteDatabase.rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorCodBarraCursor(cod_barra);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", null, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    produto.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                produto.setMarca(daoMarca.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
                produto.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorDescricaoCursor(String descricao) {
        String sql = "SELECT cod_barra as _id, fornecedor, marca, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND descricao LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + descricao + "%"};

        return sqLiteDatabase.rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorDescricaoCursor(descricao);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", null, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    produto.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                produto.setMarca(daoMarca.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
                produto.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorMarcaCursor(String marca) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN marca ON produto.marca = marca.id WHERE (marca = id OR marca IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + marca + "%"};

        Cursor cursor = sqLiteDatabase.rawQuery(sql, selection);

        return cursor;
    }

    private ArrayList<Produto> listarPorMarca(String marca) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorMarcaCursor(marca);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", null, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    produto.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                produto.setMarca(daoMarca.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));
                produto.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorFornecedorCursor(String fornecedor) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + fornecedor + "%"};

        return sqLiteDatabase.rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorFornecedorCursor(fornecedor);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto produto = new Produto();

                produto.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                produto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                produto.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = sqLiteDatabase.query("cod_barra_fornecedor", null, "produto = ?", new String[]{produto.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    produto.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                produto.setFornecedor(new DAOFornecedor(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                produto.setMarca(new DAOMarca(sqLiteDatabase).listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(produto);
            }
        }

        cursor.close();

        return produtos;
    }
}