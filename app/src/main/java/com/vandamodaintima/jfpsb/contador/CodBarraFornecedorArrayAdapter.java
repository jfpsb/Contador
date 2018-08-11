package com.vandamodaintima.jfpsb.contador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;

import java.util.ArrayList;

public class CodBarraFornecedorArrayAdapter extends ArrayAdapter<CodBarraFornecedor> {
    public CodBarraFornecedorArrayAdapter(Context context, ArrayList<CodBarraFornecedor> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CodBarraFornecedor codBarraFornecedor = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_cod_barra_fornecedor, parent, false);
        }

        TextView codigo = convertView.findViewById(R.id.labelCodBarra);

        codigo.setText(codBarraFornecedor.getCodigo());

        return convertView;
    }
}
