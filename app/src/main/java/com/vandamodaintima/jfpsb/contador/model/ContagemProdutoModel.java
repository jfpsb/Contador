package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import java.io.Serializable;
import java.util.ArrayList;

public class ContagemProdutoModel implements Serializable, IModel<ContagemProdutoModel> {
    private static final String TABELA = "contagem_produto";
    private ConexaoBanco conexaoBanco;

    private long id;
    private ContagemModel contagem;
    private ProdutoModel produto;
    private int quant;

    public ContagemProdutoModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        contagem = new ContagemModel(conexaoBanco);
        produto = new ProdutoModel(conexaoBanco);
    }

    private static final String[] colunas = new String[] { "id as _id", "produtoModel", "contagem_data", "contagem_loja", "quant" };

    public long getId() {
        return id;
    }

    public String getIdString() {
        return String.valueOf(getId());
    }

    public void setId(long id) {
        this.id = id;
    }

    public ContagemModel getContagem() {
        return contagem;
    }

    public void setContagem(ContagemModel contagem) {
        this.contagem = contagem;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produtoModel) {
        this.produto = produtoModel;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public static String[] getColunas() {
        return colunas;
    }

    @Override
    public Boolean inserir() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", getId());
            contentValues.put("produto", getProduto().getCod_barra());
            contentValues.put("contagem_data", getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", getContagem().getLoja().getCnpj());
            contentValues.put("quant", getQuant());

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
    public Boolean atualizar() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("produto", getProduto().getCod_barra());
            contentValues.put("contagem_data", getContagem().getDataParaSQLite());
            contentValues.put("contagem_loja", getContagem().getLoja().getCnpj());
            contentValues.put("quant", getQuant());

            conexaoBanco.conexao().update(TABELA, contentValues, "id = ?", new String[]{getIdString()});
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
    public Boolean deletar() {
        int result = conexaoBanco.conexao().delete(TABELA, "id = ?", new String[]{getIdString()});

        if(result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public ArrayList<ContagemProdutoModel> listar() {
        ArrayList<ContagemProdutoModel> contagem_produtos = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProdutoModel contagem_produto = new ContagemProdutoModel(conexaoBanco);

                contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                contagem_produto.setProduto(produto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

                String contagem_loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
                String contagem_data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
                contagem_produto.setContagem(contagem.listarPorId(contagem_loja, contagem_data));

                contagem_produto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));

                contagem_produtos.add(contagem_produto);
            }
        }
        cursor.close();
        return contagem_produtos;
    }

    @Override
    public ContagemProdutoModel listarPorId(Object... ids) {
        ContagemProdutoModel contagem_produto = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            contagem_produto = new ContagemProdutoModel(conexaoBanco);

            contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            contagem_produto.setProduto(produto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

            String contagem_loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
            String contagem_data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
            contagem_produto.setContagem(contagem.listarPorId(contagem_loja, contagem_data));

            contagem_produto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
        }

        cursor.close();
        return contagem_produto;
    }

    @Override
    public void load(Object... ids) {
        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            setProduto(produto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

            String contagem_loja = cursor.getString(cursor.getColumnIndexOrThrow("contagem_loja"));
            String contagem_data = cursor.getString(cursor.getColumnIndexOrThrow("contagem_data"));
            setContagem(contagem.listarPorId(contagem_loja, contagem_data));

            setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
        }

        cursor.close();
    }

    public Cursor listarPorContagemCursor(ContagemModel contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getDataParaSQLite();

        String sql = "SELECT id as _id, * FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? ORDER BY id";
        String[] selection = new String[]{loja, data};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(ContagemModel contagem) {
        String loja = contagem.getLoja().getCnpj();
        String data = contagem.getDataParaSQLite();

        String sql = "SELECT id as _id, SUM(quant) as quant, cod_barra, descricao FROM contagem_produto, produto WHERE produto = cod_barra AND contagem_loja = ? AND contagem_data = ? GROUP BY produto ORDER BY descricao";
        String[] selection = new String[]{loja, data};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }
}
