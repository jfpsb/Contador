package com.vandamodaintima.jfpsb.contador.view.produto;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.List;
import java.util.Locale;

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

        TextView labelDescricao = convertView.findViewById(R.id.txtDescricaoGrade);
        TextView labelCodBarra = convertView.findViewById(R.id.txtCodBarra);
        TextView labelPreco = convertView.findViewById(R.id.txtPreco);
        TextView txtPossuiGrades = convertView.findViewById(R.id.txtPossuiGrade);

        Produto produto = produtos.get(position);

        if (produto.getProdutoGrades().size() == 0) {
            txtPossuiGrades.setText("NÃO");
        } else {
            txtPossuiGrades.setText("SIM");
        }

        labelDescricao.setText(produto.getDescricao());
        labelCodBarra.setText(produto.getCodBarra());
        labelPreco.setText(String.format(produto.getPreco().toString(), Locale.getDefault()));

        return convertView;
    }
}
