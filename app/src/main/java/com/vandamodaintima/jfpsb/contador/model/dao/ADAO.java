package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.AModel;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.Serializable;
import java.util.List;

public abstract class ADAO<T extends AModel & IModel<T> & Serializable> {
    protected String TABELA;
    protected ConexaoBanco conexaoBanco;

    ADAO(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public abstract Boolean inserir(T t);

    public abstract Boolean inserir(List<T> lista);

    public abstract Boolean atualizar(T t);

    public Boolean deletar(T objeto) {
        try {
            conexaoBanco.conexao().beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("deletado", true);
            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{objeto.getIdentifier().toString()});
            conexaoBanco.conexao().setTransactionSuccessful();
            return true;
        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    public boolean deletarLista(List<T> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();
            for(T obj : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("deletado", true);
                conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{obj.getIdentifier().toString()});
            }
            conexaoBanco.conexao().setTransactionSuccessful();
            return true;
        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }
        return false;
    }

    public abstract List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     *
     * @param ids Identificadores da entidade no banco de dados
     * @return Retorna o objeto encontrado com a ids ou nulo, se não encontrar
     */
    public abstract T listarPorId(Object... ids);

    public abstract int getMaxId();
}
