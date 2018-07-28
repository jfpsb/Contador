package com.vandamodaintima.jfpsb.contador;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.util.EditTextMask;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class FornecedorCursorAdapter extends CursorAdapter {
    public FornecedorCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_lista_fornecedor, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelCnpj = view.findViewById(R.id.listaFornecedorCnpj);
        TextView labelNome = view.findViewById(R.id.listaFornecedorNome);
        TextView labelFantasia = view.findViewById(R.id.listaFornecedorFantasia);

        String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        String fantasia = cursor.getString(cursor.getColumnIndexOrThrow("fantasia"));

        cnpj = EditTextMask.mask(cnpj, EditTextMask.CNPJ);

        labelCnpj.setText(cnpj);
        labelNome.setText(nome);

        if(fantasia == null || fantasia.isEmpty()) {
            labelFantasia.setText("Não Possui");
        }
        else {
            labelFantasia.setText(fantasia);
        }
    }
}
