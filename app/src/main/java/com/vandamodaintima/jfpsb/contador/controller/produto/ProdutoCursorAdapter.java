package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.CursorAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

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
        TextView txtDescricao = view.findViewById(R.id.txtDescricao);
        TextView txtPreco = view.findViewById(R.id.txtPreco);
        TextView txtPossuiGrades = view.findViewById(R.id.txtPossuiGrade);

        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
        String cod_barra_grade = cursor.getString(cursor.getColumnIndexOrThrow("cod_barra_grade"));

        if (cod_barra_grade == null) {
            txtPossuiGrades.setText("NÃO");
        } else {
            txtPossuiGrades.setText("SIM");
        }

        txtCodBarra.setText(id);
        txtDescricao.setText(descricao);
        txtPreco.setText(String.valueOf(preco));
    }
}