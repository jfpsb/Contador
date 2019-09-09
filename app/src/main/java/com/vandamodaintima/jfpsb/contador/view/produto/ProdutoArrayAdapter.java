package com.vandamodaintima.jfpsb.contador.view.produto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.List;

public class ProdutoArrayAdapter extends ArrayAdapter<Produto> {
    private List<Produto> produtos;

    public ProdutoArrayAdapter(@NonNull Context context, @NonNull List<Produto> objects) {
        super(context, 0, objects);
        produtos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pesquisa_produto, parent, false);
        }

        TextView labelDescricao = convertView.findViewById(R.id.labelDescricao);
        TextView labelCodBarra = convertView.findViewById(R.id.labelCodBarra);
        TextView labelPreco = convertView.findViewById(R.id.labelPreco);

        Produto produto = produtos.get(position);

        labelDescricao.setText(produto.getDescricao());
        labelCodBarra.setText(produto.getCod_barra());
        labelPreco.setText(produto.getPreco().toString());

        return convertView;
    }
}
