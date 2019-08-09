package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import java.io.Serializable;
import java.util.ArrayList;

public class ProdutoModel implements Serializable, IModel<ProdutoModel> {
    private static final String TABELA = "produto";
    private ConexaoBanco conexaoBanco;
    private MarcaModel marcaModel;
    private FornecedorModel fornecedorModel;

    private String cod_barra;
    private ArrayList<String> cod_barra_fornecedor = new ArrayList<>();
    private MarcaModel marca;
    private FornecedorModel fornecedor;
    private String descricao;
    private Double preco;

    public ProdutoModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        marcaModel = new MarcaModel(conexaoBanco);
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    public ArrayList<String> getCod_barra_fornecedor() {
        return cod_barra_fornecedor;
    }

    public void setCod_barra_fornecedor(ArrayList<String> cod_barra_fornecedor) {
        this.cod_barra_fornecedor = cod_barra_fornecedor;
    }

    public MarcaModel getMarca() {
        return marca;
    }

    public void setMarca(MarcaModel marca) {
        this.marca = marca;
    }

    public FornecedorModel getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(FornecedorModel fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean inserir() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", getCod_barra());
            contentValues.put("descricao", getDescricao());
            contentValues.put("preco", getPreco());

            if (getFornecedor() != null) {
                contentValues.put("fornecedor", getFornecedor().getCnpj());
            } else {
                contentValues.putNull("fornecedor");
            }

            if (getMarca() != null) {
                contentValues.put("marca", getMarca().getNome());
            } else {
                contentValues.putNull("marca");
            }

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);

            for (int i = 0; i < getCod_barra_fornecedor().size(); i++) {
                String codigo = getCod_barra_fornecedor().get(i);

                ContentValues content = new ContentValues();

                content.put("produto", getCod_barra());
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
    public Boolean inserir(ArrayList<ProdutoModel> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for(ProdutoModel p : lista) {
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

    public Boolean atualizar() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("descricao", getDescricao());
            contentValues.put("preco", getPreco());

            conexaoBanco.conexao().delete("cod_barra_fornecedor", "produto = ?", new String[]{cod_barra});

            for (String codigo : getCod_barra_fornecedor()) {
                ContentValues contentCodigos = new ContentValues();

                contentCodigos.put("produto", getCod_barra());
                contentCodigos.put("codigo", codigo);

                conexaoBanco.conexao().insertOrThrow("cod_barra_fornecedor", null, contentCodigos);
            }

            if (getFornecedor() != null) {
                contentValues.put("fornecedor", getFornecedor().getCnpj());
            } else {
                contentValues.putNull("fornecedor");
            }

            if (getMarca() != null) {
                contentValues.put("marca", getMarca().getNome());
            } else {
                contentValues.putNull("marca");
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "cod_barra = ?", new String[]{cod_barra});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR PRODUTO", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    public Boolean deletar() {
        int result = conexaoBanco.conexao().delete(TABELA, "cod_barra = ?", new String[]{getCod_barra()});

        if (result > 0)
            return true;

        return false;
    }

    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, null, null, null, null, null, null, null);
    }

    public ArrayList<ProdutoModel> listar() {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoModel p = new ProdutoModel(conexaoBanco);

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
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

                p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public ProdutoModel listarPorId(Object... ids) {
        ProdutoModel p = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            p = new ProdutoModel(conexaoBanco);

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

            p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
            p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();

        return p;
    }

    @Override
    public void load(Object... ids) {
        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));

            Cursor cursorCodigoBarraFornecedor = conexaoBanco.conexao().query("cod_barra_fornecedor", new String[]{"ROWID as _id", "codigo", "produto"}, "produto = ?", new String[]{getCod_barra()}, null, null, null, null);

            if (cursorCodigoBarraFornecedor.getCount() > 0) {
                ArrayList<String> codigos = new ArrayList<>();

                while (cursorCodigoBarraFornecedor.moveToNext()) {
                    codigos.add(cursorCodigoBarraFornecedor.getString(cursorCodigoBarraFornecedor.getColumnIndexOrThrow("codigo")));
                }

                setCod_barra_fornecedor(codigos);
            }

            cursorCodigoBarraFornecedor.close();

            setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
            setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();
    }

    public Cursor listarPorCodBarraCursor(String cod_barra) {
        String sql = "SELECT cod_barra as _id, * FROM produto LEFT JOIN cod_barra_fornecedor ON produto.cod_barra = cod_barra_fornecedor.produto WHERE produto.cod_barra LIKE ? OR cod_barra_fornecedor.codigo LIKE ? GROUP BY produto ORDER BY cod_barra";

        String[] selection = new String[] { "%" + cod_barra + "%", "%" + cod_barra + "%" };

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<ProdutoModel> listarPorCodBarra(String cod_barra) {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();

        Cursor cursor = listarPorCodBarraCursor(cod_barra);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoModel p = new ProdutoModel(conexaoBanco);

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
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

                p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));

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

    public ArrayList<ProdutoModel> listarPorDescricao(String descricao) {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();

        Cursor cursor = listarPorDescricaoCursor(descricao);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoModel p = new ProdutoModel(conexaoBanco);

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
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

                p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
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

    private ArrayList<ProdutoModel> listarPorMarca(String marca) {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();

        Cursor cursor = listarPorMarcaCursor(marca);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoModel p = new ProdutoModel(conexaoBanco);

                p.setCod_barra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
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

                p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
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

    public ArrayList<ProdutoModel> listarPorFornecedor(String fornecedor) {
        ArrayList<ProdutoModel> produtos = new ArrayList<>();

        Cursor cursor = listarPorFornecedorCursor(fornecedor);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoModel p = new ProdutoModel(conexaoBanco);

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

                p.setFornecedor(fornecedorModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setMarca(marcaModel.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));

                produtos.add(p);
            }
        }

        cursor.close();
        return produtos;
    }
}
