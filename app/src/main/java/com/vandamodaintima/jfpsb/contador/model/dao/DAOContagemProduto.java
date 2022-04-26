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

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            contagemProduto.setId(UUID.randomUUID());
            contentValues.put("uuid", contagemProduto.getId().toString());

            if (contagemProduto.getProdutoGrade() != null) {
                contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId().toString());
            } else {
                contentValues.putNull("produto_grade");
            }

            contentValues.put("contagem", contagemProduto.getContagem().getId().toString());
            contentValues.put("quant", contagemProduto.getQuant());

            LocalDateTime now = LocalDateTime.now();
            contentValues.put("criadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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
                contagemProduto.setId(UUID.randomUUID());
                contentValues.put("uuid", contagemProduto.getId().toString());
                contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId().toString());
                contentValues.put("contagem", contagemProduto.getContagem().getId().toString());
                contentValues.put("quant", contagemProduto.getQuant());

                LocalDateTime now = LocalDateTime.now();
                contentValues.put("criadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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
            UUID id = (UUID) contagemProduto.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("produto_grade", contagemProduto.getProdutoGrade().getId().toString());
            contentValues.put("contagem", contagemProduto.getContagem().getId().toString());
            contentValues.put("quant", contagemProduto.getQuant());

            LocalDateTime now = LocalDateTime.now();
            contentValues.put("modificadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{id.toString()});
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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ContagemProduto.getColunas(), "deletado = false", null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagem_produto = new ContagemProduto();

                contagem_produto.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagem_produto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto_grade"))));
                String contagem = cursor.getString(cursor.getColumnIndexOrThrow("contagem"));
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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ContagemProduto.getColunas(), "uuid = ? AND deletado = false", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            contagem_produto = new ContagemProduto();
            contagem_produto.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
            contagem_produto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto_grade"))));
            String contagem = cursor.getString(cursor.getColumnIndexOrThrow("contagem"));
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
        String sql = "SELECT cp.uuid as _id, * FROM contagem_produto AS cp INNER JOIN produto_grade AS pg ON pg.uuid = cp.produto_grade WHERE contagem = ? AND cp.deletado = false ORDER BY cp.uuid";
        String[] selection = new String[]{contagem.getId().toString()};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagem) {
        String sql = "SELECT cp.uuid as _id, cp.produto_grade AS produto_grade, cp.contagem AS contagem, SUM(cp.quant) AS quant, " +
                "p.descricao AS descricao " +
                "FROM contagem_produto AS cp " +
                "INNER JOIN produto_grade AS pg ON cp.produto_grade = pg.uuid " +
                "INNER JOIN produto AS p ON pg.produto = p.uuid " +
                "WHERE contagem = ? AND cp.deletado = false GROUP BY pg.produto ORDER BY p.descricao";

        String[] selection = new String[]{contagem.getId().toString()};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public Cursor listarPorContagemGroupByProdutoGradeCursor(Contagem contagem) {
        String sql = "SELECT cp.uuid as _id, produto_grade, contagem, SUM(cp.quant) AS quant FROM contagem_produto WHERE contagem = ? AND deletado = false GROUP BY produto_grade";
        String[] selection = new String[]{contagem.getId().toString()};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByProduto(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemGroupByProdutoCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();
                contagemProduto.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("produto_grade"))));
                String contagemId = cursor.getString(cursor.getColumnIndexOrThrow("contagem"));
                contagemProduto.setContagem(daoContagem.listarPorId(contagemId));
                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }

    public ArrayList<ContagemProduto> listarPorContagemGroupByProdutoGrade(Contagem contagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();
        Cursor cursor = listarPorContagemGroupByProdutoGradeCursor(contagem);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemProduto contagemProduto = new ContagemProduto();
                contagemProduto.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("produto_grade"))));
                String contagemId = cursor.getString(cursor.getColumnIndexOrThrow("contagem"));
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
                contagemProduto.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagemProduto.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("produto_grade"))));
                String contagemId = cursor.getString(cursor.getColumnIndexOrThrow("contagem"));
                contagemProduto.setContagem(daoContagem.listarPorId(contagemId));
                contagemProduto.setQuant(cursor.getInt(cursor.getColumnIndexOrThrow("quant")));
                contagemProdutos.add(contagemProduto);
            }
        }

        return contagemProdutos;
    }
}
