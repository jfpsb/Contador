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

import java.util.ArrayList;
import java.util.List;

public class DAOContagemProduto extends ADAO<ContagemProduto> {
    private DAOProdutoGrade daoProdutoGrade;
    private DAOProduto daoProduto;
    private DAOContagem daoContagem;

    public DAOContagemProduto(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "contagem_produto";
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        daoContagem = new DAOContagem(conexaoBanco);
        daoProduto = new DAOProduto(conexaoBanco);
    }

    @Override
    public Boolean inserir(ContagemProduto contagemProduto) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", contagemProduto.getId());

            if (contagemProduto.getProdutoGrade() != null) {
                contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId());
            } else {
                contentValues.putNull("produto_grade");
            }
            if(contagemProduto.getProduto() != null) {
                contentValues.put("produto", contagemProduto.getProduto().getId());
            } else {
                contentValues.putNull("produto");
            }

            contentValues.put("contagem", contagemProduto.getContagem().getId());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<ContagemProduto> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (ContagemProduto contagemProduto : lista) {

                ContentValues contentValues = new ContentValues();

                contentValues.put("id", contagemProduto.getId());

                if (contagemProduto.getProdutoGrade() != null) {
                    contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId());
                } else {
                    contentValues.putNull("produto_grade");
                }
                contentValues.put("produto", contagemProduto.getProduto().getId());
                contentValues.put("contagem", contagemProduto.getContagem().getId());
                contentValues.put("quant", contagemProduto.getQuant());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            conexaoBanco.conexao().setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(ContagemProduto contagemProduto) {
        try {
            long id = (long) contagemProduto.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            if (contagemProduto.getProdutoGrade() != null) {
                contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId());
            } else {
                contentValues.putNull("produto_grade");
            }
            contentValues.put("produto", contagemProduto.getProduto().getCodBarra());
            contentValues.put("contagem", contagemProduto.getContagem().getId());
            contentValues.put("quant", contagemProduto.getQuant());

            conexaoBanco.conexao().update(TABELA, contentValues, "id = ?", new String[]{String.valueOf(id)});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, "ERRO AO ATUALIZAR CONTAGEM DE PRODUTO", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public List<ContagemProduto> listar() {
        ArrayList<ContagemProduto> contagem_produtos = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ContagemProduto.getColunas(), null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagem_produto = new ContagemProduto();

                contagem_produto.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
                contagem_produto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto_grade"))));
                contagem_produto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
                long contagem = cursor.getLong(cursor.getColumnIndexOrThrow("contagem"));
                contagem_produto.setContagem(daoContagem.listarPorId(contagem));

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
            contagem_produto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto_grade"))));
            contagem_produto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
            long contagem = cursor.getLong(cursor.getColumnIndexOrThrow("contagem"));
            contagem_produto.setContagem(daoContagem.listarPorId(contagem));
            contagem_produto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
        }

        cursor.close();
        return contagem_produto;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        String sql = "SELECT cp.id as _id, * FROM contagem_produto AS cp INNER JOIN produto_grade AS pg ON pg.id = cp.produto_grade WHERE contagem = ? ORDER BY cp.id";
        String[] selection = new String[]{String.valueOf(contagem.getId())};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagem) {
        String sql = "SELECT cp.id as _id, cp.produto_grade AS produto_grade, cp.contagem AS contagem, SUM(cp.quant) AS quant, cp.produto AS produto, " +
                "p.descricao AS descricao " +
                "FROM contagem_produto AS cp " +
                "LEFT JOIN produto_grade AS pg ON cp.produto_grade = pg.id " +
                "INNER JOIN produto AS p ON cp.produto = p.id " +
                "WHERE contagem = ? GROUP BY cp.produto ORDER BY p.descricao";

        String[] selection = new String[]{String.valueOf(contagem.getId())};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByGradeCursor(Contagem contagem) {
        String sql = "SELECT cp.id as _id, cp.produto_grade AS produto_grade, pg.cod_barra AS produto_grade_cod_barra, p.cod_barra AS produto_cod_barra, cp.contagem AS contagem, SUM(cp.quant) AS quant, cp.produto AS produto, " +
                "p.descricao AS descricao " +
                "FROM contagem_produto AS cp " +
                "LEFT JOIN produto_grade AS pg ON cp.produto_grade = pg.id " +
                "INNER JOIN produto AS p ON cp.produto = p.id " +
                "WHERE contagem = ? GROUP BY cp.produto, cp.produto_grade ORDER BY p.descricao";

        String[] selection = new String[]{String.valueOf(contagem.getId())};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByProduto(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemGroupByProdutoCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();

                contagemProduto.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndex("produto_grade"))));
                contagemProduto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

                long contagemId = cursor.getLong(cursor.getColumnIndexOrThrow("contagem"));
                contagemProduto.setContagem(daoContagem.listarPorId(contagemId));

                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByGrade(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemGroupByGradeCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();

                contagemProduto.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndex("produto_grade"))));
                contagemProduto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

                long contagemId = cursor.getLong(cursor.getColumnIndexOrThrow("contagem"));
                contagemProduto.setContagem(daoContagem.listarPorId(contagemId));

                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }

    public List<ContagemProduto> listarPorContagem(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();

                contagemProduto.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndex("produto_grade"))));
                contagemProduto.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));

                long contagemId = cursor.getLong(cursor.getColumnIndexOrThrow("contagem"));
                contagemProduto.setContagem(daoContagem.listarPorId(contagemId));

                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));

                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }
}
