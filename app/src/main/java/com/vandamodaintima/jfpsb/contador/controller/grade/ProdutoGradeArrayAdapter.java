package com.vandamodaintima.jfpsb.contador.controller.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.List;

public class ProdutoGradeArrayAdapter extends ArrayAdapter<ProdutoGrade> {
    private List<ProdutoGrade> objects;

    public ProdutoGradeArrayAdapter(@NonNull Context context, List<ProdutoGrade> objects) {
        super(context, 0, objects);
        setNotifyOnChange(true);
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        ProdutoGrade produtoGrade = objects.get(position);

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesquisa_produto, parent, false);
        final TextView lblCodBarra = layout.findViewById(R.id.labelCodBarra);
        TextView lblPreco = layout.findViewById(R.id.labelPreco);
        TextView lblDescricao = layout.findViewById(R.id.labelDescricao);

        StringBuilder descricao = new StringBuilder();

        for (int i = 0; i < produtoGrade.getGrades().size(); i++) {
            Grade grade = produtoGrade.getGrades().get(i);
            descricao.append(grade.getTipoGrade().getNome()).append(" ").append(grade.getNome());

            if (i != (produtoGrade.getGrades().size() - 1)) {
                descricao.append("/");
            }
        }

        lblCodBarra.setText(produtoGrade.getCodBarra());
        lblPreco.setText(String.valueOf(produtoGrade.getPreco()));
        lblDescricao.setText(descricao);

        return layout;
    }

    public List<ProdutoGrade> getObjects() {
        return objects;
    }
}
