package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.CursorAdapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProdutoGrade;

import java.util.List;

public class ProdutoCursorAdapter extends CursorAdapter {

    ProdutoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_produto, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtCodBarra = view.findViewById(R.id.txtCodBarra);
        TextView txtDescricao = view.findViewById(R.id.txtDescricaoGrade);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("cod_barra"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));

        txtCodBarra.setText(cod_barra);
        txtDescricao.setText(descricao);
    }
}