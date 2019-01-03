package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

import java.util.List;

public class FornecedorAdapter extends ArrayAdapter<Fornecedor> {
    private int resourceLayout;
    private Context mContext;

    public FornecedorAdapter(Context context, int resource, List<Fornecedor> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Fornecedor p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.listaFornecedorCnpj);
            TextView tt2 = v.findViewById(R.id.listaFornecedorNome);
            TextView tt3 = v.findViewById(R.id.listaFornecedorFantasia);

            if (tt1 != null) {
                tt1.setText(p.getCnpj());
            }

            if (tt2 != null) {
                tt2.setText(p.getNome());
            }

            if (tt3 != null) {
                tt3.setText(p.getFantasia());
            }
        }

        return v;
    }
}