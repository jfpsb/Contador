package com.vandamodaintima.jfpsb.contador.controller.contagem;


import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class ContagemProdutoCursorAdapter extends CursorAdapter {

    ContagemProdutoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_produto_contagem_sem_grade, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView lblCodBarra = view.findViewById(R.id.txtCodigoProduto);
        TextView lblDescricao = view.findViewById(R.id.txtDescricaoProduto);
        TextView lblQuantidade = view.findViewById(R.id.lblQuantidade);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("produto_grade_cod_barra"));

        if(cod_barra == null) {
            cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("produto_cod_barra"));
        }
        
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quant"));

        lblCodBarra.setText(cod_barra);
        lblDescricao.setText(descricao);
        lblQuantidade.setText(String.valueOf(quantidade));
    }
}
