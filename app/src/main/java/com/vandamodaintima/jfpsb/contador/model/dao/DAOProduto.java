package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class DAOProduto implements IDAO<Produto> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "produto";

    private DAOMarca daoMarca;
    private DAOFornecedor daoFornecedor;

    public DAOProduto(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoMarca = new DAOMarca(conexaoBanco);
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @Override
    public Boolean inserir(Produto produto) {
        try {
            conexaoBanco.conexao().beginTransaction();

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
                contentValues.put("marca", produto.getMarca().getNome());
            } else {
                contentValues.putNull("marca");
            }

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);

            for (int i = 0; i < produto.getCod_barra_fornecedor().size(); i++) {
                String codigo = produto.getCod_barra_fornecedor().get(i);

                ContentValues content = new ContentValues();

                content.put("produto", produto.getCod_barra());
                content.put("codigo", codigo);

                conexaoBanco.conexao().insertOrThrow("cod_barra_fornecedor", null, content);
            }

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
    public Boolean inserir(List<Produto> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Produto p : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cod_barra", p.getCod_barra());
                contentValues.put("descricao", p.getDescricao());
                contentValues.put("preco", p.getPreco());

                if (p.getFornecedor() != null) {
                    contentValues.put("fornecedor", p.getFornecedor().getCnpj());
                } else {
                    contentValues.putNull("fornecedor");
                }

                if (p.getMarca() != null) {
                    contentValues.put("marca", p.getMarca().getNome());
                } else {
                    contentValues.putNull("marca");
                }

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                for (int i = 0; i < p.getCod_barra_fornecedor().size(); i++) {
                    String codigo = p.getCod_barra_fornecedor().get(i);

                    ContentValues content = new ContentValues();

                    content.put("produto", p.getCod_barra());
                    content.put("codigo", codigo);

                    conexaoBanco.conexao().insertWithOnConflict("cod_barra_fornecedor", null, content, SQLiteDatabase.CONFLICT_IGNORE);
                }
            }

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
    public Boolean atualizar(Produto produto, Object... chaves) {
        try {
            String cod_barra = (String) chaves[0];

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());

            conexaoBanco.conexao().delete("cod_barra_fornecedor", "produto = ?", new String[]{cod_barra});

            for (String codigo : produto.getCod_barra_fornecedor()) {
                ContentValues contentCodigos = new ContentValues();

                contentCodigos.put("produto", produto.getCod_barra());
                contentCodigos.put("codigo", codigo);

                conexaoBanco.conexao().insertOrThrow("cod_barra_fornecedor", null, contentCodigos);
            }

            if (produto.getFornecedor() != null) {
                contentValues.put("fornecedor", produto.getFornecedor().getCnpj());
            } else {
                contentValues.putNull("fornecedor");
            }

            if (produto.getMarca() != null) {
                contentValues.put("marca", produto.getMarca().getNome());
            } else {
                contentValues.putNull("marca");
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "cod_barra = ?", new String[]{cod_barra});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR PRODUTO", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        String cod_barra = (String) chaves[0];
        int result = conexaoBanco.conexao().delete(TABELA, "cod_barra = ?", new String[]{cod_barra});
        return result > 0;
    }

    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, Produto.getColunas(), null, null, null, null, null, null);
    }

    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", null, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    p.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Produto listarPorId(Object... ids) {
        Produto p = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            p = new Produto();

            cursor.moveToFirst();

            p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

            Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", new String[]{"ROWID as _id", "codigo", "produto"}, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

            if (cursorCodigoBarraFornecedor.getCount() > 0) {
                ArrayList<String> codigos = new ArrayList<>();

                while (cursorCodigoBarraFornecedor.moveToNext()) {
                    codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                }

                p.setCod_barra_fornecedor(codigos);
            }

            cursorCodigoBarraFornecedor.close();

            p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
            p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();

        return p;
    }

    public Cursor listarPorCodBarraCursor(String cod_barra) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN cod_barra_fornecedor ON produto.cod_barra = cod_barra_fornecedor.produto WHERE produto.cod_barra LIKE ? OR cod_barra_fornecedor.codigo LIKE ? GROUP BY produto ORDER BY cod_barra";

        String[] selection = new String[]{"%" + cod_barra + "%", "%" + cod_barra + "%"};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorCodBarraCursor(cod_barra);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", null, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    p.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorDescricaoCursor(String descricao) {
        String sql = "SELECT cod_barra as _id, fornecedor, marca, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND descricao LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + descricao + "%"};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorDescricaoCursor(descricao);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", null, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    p.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorMarcaCursor(String marca) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN marca ON produto.marca = marca.nome WHERE (marca = nome OR marca IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + marca + "%"};

        Cursor cursor = conexaoBanco.conexao().rawQuery(sql, selection);

        return cursor;
    }

    public ArrayList<Produto> listarPorMarca(String marca) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorMarcaCursor(marca);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", null, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    p.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorFornecedorCursor(String fornecedor) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND nome LIKE ? ORDER BY descricao";

        String[] selection = new String[]{"%" + fornecedor + "%"};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorFornecedorCursor(fornecedor);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

                Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", null, "produto = ?", new String[]{p.getCod_barra()}, null, null, null, null);

                if (cursorCodigoBarraFornecedor.getCount() > 0) {
                    ArrayList<String> codigos = new ArrayList<>();

                    while (cursorCodigoBarraFornecedor.moveToNext()) {
                        codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                    }

                    p.setCod_barra_fornecedor(codigos);
                }

                cursorCodigoBarraFornecedor.close();

                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(p);
            }
        }

        cursor.close();
        return produtos;
    }
}
