package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOProduto extends ADAO<Produto> {
    private DAOMarca daoMarca;
    private DAOFornecedor daoFornecedor;
    private DAOProdutoGrade daoProdutoGrade;

    public DAOProduto(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoMarca = new DAOMarca(conexaoBanco);
        daoFornecedor = new DAOFornecedor(conexaoBanco);
        TABELA = "produto";
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco, this);
    }

    public DAOProduto(ConexaoBanco conexaoBanco, DAOProdutoGrade daoProdutoGrade) {
        super(conexaoBanco);
        daoMarca = new DAOMarca(conexaoBanco);
        daoFornecedor = new DAOFornecedor(conexaoBanco);
        this.daoProdutoGrade = daoProdutoGrade;
        TABELA = "produto";
    }

    @Override
    public Boolean inserir(Produto produto) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCodBarra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("ncm", produto.getNcm());

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

            if (produto.getGrades().size() > 0) {
                for (ProdutoGrade grade : produto.getGrades()) {
                    ContentValues gradesValues = new ContentValues();
                    gradesValues.put("cod_barra", grade.getCodBarra());
                    gradesValues.put("produto", grade.getProduto().getCodBarra());
                    gradesValues.put("preco", grade.getPreco());

                    conexaoBanco.conexao().insertOrThrow("produto_grade", null, gradesValues);

                    if (grade.getSubGrades().size() > 0) {
                        for (SubGrade subGrade : grade.getSubGrades()) {
                            ContentValues subGradeValues = new ContentValues();
                            subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                            subGradeValues.put("grade", subGrade.getGrade().getId());
                            conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
                        }
                    }
                }
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
    public Boolean inserir(List<Produto> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Produto p : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cod_barra", p.getCodBarra());
                contentValues.put("descricao", p.getDescricao());
                contentValues.put("preco", p.getPreco());
                contentValues.put("ncm", p.getNcm());

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

                if (p.getGrades().size() > 0) {
                    for (ProdutoGrade grade : p.getGrades()) {
                        ContentValues gradesValues = new ContentValues();
                        gradesValues.put("cod_barra", grade.getCodBarra());
                        gradesValues.put("produto", grade.getProduto().getCodBarra());
                        gradesValues.put("preco", grade.getPreco());

                        conexaoBanco.conexao().insertWithOnConflict("produto_grade", null, gradesValues, SQLiteDatabase.CONFLICT_IGNORE);

                        if (grade.getSubGrades().size() > 0) {
                            for (SubGrade subGrade : grade.getSubGrades()) {
                                ContentValues subGradeValues = new ContentValues();
                                subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                                subGradeValues.put("grade", subGrade.getGrade().getId());
                                conexaoBanco.conexao().insertWithOnConflict("sub_grade", null, subGradeValues, SQLiteDatabase.CONFLICT_IGNORE);
                            }
                        }
                    }
                }
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
    public Boolean atualizar(Produto produto) {
        try {
            String cod_barra = (String) produto.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("ncm", produto.getNcm());

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

            conexaoBanco.conexao().update(TABELA, contentValues, "cod_barra LIKE ?", new String[]{cod_barra});

            conexaoBanco.conexao().delete("produto_grade", "produto LIKE ?", new String[]{cod_barra});

            if (produto.getGrades().size() > 0) {
                for (ProdutoGrade grade : produto.getGrades()) {
                    ContentValues gradesValues = new ContentValues();
                    gradesValues.put("cod_barra", grade.getCodBarra());
                    gradesValues.put("produto", grade.getProduto().getCodBarra());
                    gradesValues.put("preco", grade.getPreco());

                    conexaoBanco.conexao().insertOrThrow("produto_grade", null, gradesValues);

                    if (grade.getSubGrades().size() > 0) {
                        for (SubGrade subGrade : grade.getSubGrades()) {
                            ContentValues subGradeValues = new ContentValues();
                            subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                            subGradeValues.put("grade", subGrade.getGrade().getId());
                            conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
                        }
                    }
                }
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    public ArrayList<Produto> listar() {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarCursor(Produto.getColunas());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Produto listarPorId(Object... ids) {
        Produto p = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Produto.getColunas(), "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            p = new Produto();

            cursor.moveToFirst();

            p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
            p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
            p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
            p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            p.setGrades(daoProdutoGrade.listarPorProduto(p));
        }

        cursor.close();

        return p;
    }

    @Override
    public int getMaxId() {
        int maxId = 0;
        String sql = "SELECT max(CAST(cod_barra as SIGNED)) as maxId from produto";
        Cursor cursor = conexaoBanco.conexao().rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            maxId = cursor.getInt(0);
        }

        cursor.close();

        return maxId;
    }

    public Cursor listarPorCodBarraCursor(String cod_barra) {
        String sql = "SELECT p.cod_barra as _id, * FROM produto AS p LEFT JOIN produto_grade AS pg WHERE p.cod_barra LIKE ? OR pg.cod_barra LIKE ? ORDER BY cod_barra";
        String[] selection = new String[]{"%" + cod_barra + "%", "%" + cod_barra + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorCodBarraCursor(cod_barra);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorDescricaoCursor(String descricao) {
        String sql = "SELECT cod_barra as _id, * FROM produto WHERE descricao LIKE ? ORDER BY descricao";
        String[] selection = new String[]{"%" + descricao + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorDescricaoCursor(descricao);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setGrades(daoProdutoGrade.listarPorProduto(p));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorMarcaCursor(String marca) {
        String sql = "SELECT cod_barra as _id, * FROM produto AS p INNER JOIN marca AS m ON p.marca = m.nome WHERE marca = nome AND nome LIKE ? ORDER BY descricao";
        String[] selection = new String[]{"%" + marca + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorMarca(String marca) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorMarcaCursor(marca);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setGrades(daoProdutoGrade.listarPorProduto(p));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorFornecedorCursor(String fornecedor) {
        String sql = "SELECT cod_barra as _id, * FROM produto AS p INNER JOIN fornecedor AS f ON produto.fornecedor = fornecedor.cnpj WHERE p.fornecedor = f.cnpj AND nome LIKE ? ORDER BY descricao";
        String[] selection = new String[]{"%" + fornecedor + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorFornecedorCursor(fornecedor);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();
        return produtos;
    }
}
