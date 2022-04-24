package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DAOProduto extends ADAO<Produto> {
    private DAOMarca daoMarca;
    private DAOFornecedor daoFornecedor;

    public DAOProduto(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoMarca = new DAOMarca(conexaoBanco);
        daoFornecedor = new DAOFornecedor(conexaoBanco);
        TABELA = "produto";
    }

    @Override
    public Boolean inserir(Produto produto) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            produto.setId(UUID.randomUUID());
            contentValues.put("uuid", produto.getId().toString());
            contentValues.put("cod_barra", produto.getCodBarra());
            contentValues.put("descricao", produto.getDescricao());
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

            //Insere em produto_grade
            if (produto.getProdutoGrades().size() > 0) {
                for (ProdutoGrade produtoGrade : produto.getProdutoGrades()) {
                    ContentValues gradesValues = new ContentValues();

                    produtoGrade.setId(UUID.randomUUID());
                    gradesValues.put("uuid", produtoGrade.getId().toString());
                    gradesValues.put("cod_barra", produtoGrade.getCodBarra());
                    gradesValues.put("cod_barra_alternativo", produtoGrade.getCodBarraAlternativo());
                    gradesValues.put("produto", produto.getId().toString());
                    gradesValues.put("preco_custo", produtoGrade.getPreco_custo());
                    gradesValues.put("preco_venda", produtoGrade.getPreco_venda());

                    conexaoBanco.conexao().insertOrThrow("produto_grade", null, gradesValues);

                    // Insere em sub_grade
                    if (produtoGrade.getGrades().size() > 0) {
                        for (SubGrade subGrade : produtoGrade.getGrades()) {
                            ContentValues subGradeValues = new ContentValues();

                            subGrade.setId(UUID.randomUUID());
                            subGradeValues.put("uuid", subGrade.getId().toString());
                            subGradeValues.put("produto_grade", produtoGrade.getId().toString());
                            subGradeValues.put("grade", subGrade.getGrade().getId().toString());
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

            for (Produto produto : lista) {
                ContentValues contentValues = new ContentValues();

                produto.setId(UUID.randomUUID());
                contentValues.put("uuid", produto.getId().toString());
                contentValues.put("cod_barra", produto.getCodBarra());
                contentValues.put("descricao", produto.getDescricao());
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

                //Insere em produto_grade
                if (produto.getProdutoGrades().size() > 0) {
                    for (ProdutoGrade produtoGrade : produto.getProdutoGrades()) {
                        ContentValues gradesValues = new ContentValues();

                        produtoGrade.setId(UUID.randomUUID());
                        gradesValues.put("uuid", produtoGrade.getId().toString());
                        gradesValues.put("cod_barra", produtoGrade.getCodBarra());
                        gradesValues.put("cod_barra_alternativo", produtoGrade.getCodBarraAlternativo());
                        gradesValues.put("produto", produto.getId().toString());
                        gradesValues.put("preco_custo", produtoGrade.getPreco_custo());
                        gradesValues.put("preco_venda", produtoGrade.getPreco_venda());

                        conexaoBanco.conexao().insertOrThrow("produto_grade", null, gradesValues);

                        // Insere em sub_grade
                        if (produtoGrade.getGrades().size() > 0) {
                            for (SubGrade subGrade : produtoGrade.getGrades()) {
                                ContentValues subGradeValues = new ContentValues();

                                subGrade.setId(UUID.randomUUID());
                                subGradeValues.put("uuid", subGrade.getId().toString());
                                subGradeValues.put("produto_grade", produtoGrade.getId().toString());
                                subGradeValues.put("grade", subGrade.getGrade().getId().toString());
                                conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
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
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("descricao", produto.getDescricao());
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

            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{produto.getId().toString()});
            //No banco de dados está configurado na tabela sub_grade o cascade para quando apagar produto_grade, então não é necessário apagar as subgrades desse produto_grade
            conexaoBanco.conexao().delete("produto_grade", "produto = ?", new String[]{produto.getId().toString()});

            for (ProdutoGrade produtoGrade : produto.getProdutoGrades()) {
                ContentValues gradesValues = new ContentValues();
                produtoGrade.setId(UUID.randomUUID());
                gradesValues.put("uuid", produtoGrade.getId().toString());
                gradesValues.put("cod_barra", produtoGrade.getCodBarra());
                gradesValues.put("cod_barra_alternativo", produtoGrade.getCodBarraAlternativo());
                gradesValues.put("produto", produto.getId().toString());
                gradesValues.put("preco_custo", produtoGrade.getPreco_custo());
                gradesValues.put("preco_venda", produtoGrade.getPreco_venda());

                conexaoBanco.conexao().insertOrThrow("produto_grade", null, gradesValues);

                for (SubGrade subGrade : produtoGrade.getGrades()) {
                    ContentValues subGradeValues = new ContentValues();
                    subGrade.setId(UUID.randomUUID());
                    subGradeValues.put("uuid", subGrade.getId().toString());
                    subGradeValues.put("produto_grade", produtoGrade.getId().toString());
                    subGradeValues.put("grade", subGrade.getGrade().getId().toString());
                    conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
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
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);

        ArrayList<Produto> produtos = new ArrayList<>();

        String sql = "SELECT uuid as _id, * FROM produto ORDER BY descricao";
        Cursor cursor = conexaoBanco.conexao().rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Produto listarPorId(Object... ids) {
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        Produto p = null;

        String sql = "SELECT uuid as _id, * FROM produto WHERE uuid = ? ORDER BY descricao";
        Cursor cursor = conexaoBanco.conexao().rawQuery(sql, new String[]{String.valueOf(ids[0])});

        if (cursor.getCount() > 0) {
            p = new Produto();

            cursor.moveToFirst();

            p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
            p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
            p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
            p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
            p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
            p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));
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
        String sql = "SELECT p.uuid AS _id, p.* FROM produto AS p INNER JOIN produto_grade AS pg ON p.uuid = pg.produto WHERE p.cod_barra LIKE ? OR pg.cod_barra LIKE ? OR pg.cod_barra_alternativo LIKE ? GROUP BY p.uuid ORDER BY p.cod_barra;";
        String[] selection = new String[]{"%" + cod_barra + "%", "%" + cod_barra + "%", "%" + cod_barra + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorCodBarra(String cod_barra) {
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorCodBarraCursor(cod_barra);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorDescricaoCursor(String descricao) {
        String sql = "SELECT uuid AS _id, * FROM produto WHERE descricao LIKE ? ORDER BY descricao";
        String[] selection = new String[]{"%" + descricao + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorDescricao(String descricao) {
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorDescricaoCursor(descricao);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorMarcaCursor(String marca) {
        String sql = "SELECT p.*, p.uuid AS _id FROM produto AS p INNER JOIN marca AS m ON p.marca = m.nome WHERE nome LIKE ? ORDER BY p.descricao";
        String[] selection = new String[]{"%" + marca + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorMarca(String marca) {
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorMarcaCursor(marca);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));
            }
        }

        cursor.close();

        return produtos;
    }

    public Cursor listarPorFornecedorCursor(String fornecedor) {
        String sql = "SELECT p.uuid AS _id, p.* FROM produto AS p INNER JOIN fornecedor AS f ON p.fornecedor = f.cnpj WHERE f.nome LIKE ? ORDER BY p.descricao";
        String[] selection = new String[]{"%" + fornecedor + "%"};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Produto> listarPorFornecedor(String fornecedor) {
        DAOProdutoGrade daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        ArrayList<Produto> produtos = new ArrayList<>();

        Cursor cursor = listarPorFornecedorCursor(fornecedor);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Produto p = new Produto();

                p.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                p.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                p.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                p.setNcm(cursor.getString(cursor.getColumnIndexOrThrow("ncm")));
                p.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                p.setMarca(daoMarca.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("marca"))));
                p.setProdutoGrades(daoProdutoGrade.listarPorProduto(p));

                produtos.add(p);
            }
        }

        cursor.close();
        return produtos;
    }
}
