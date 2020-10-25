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
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesquisa_produto, parent, false);
        }

        ProdutoGrade produtoGrade = objects.get(position);

        final TextView lblCodBarra = convertView.findViewById(R.id.labelCodBarra);
        TextView lblPreco = convertView.findViewById(R.id.labelPreco);
        TextView lblDescricao = convertView.findViewById(R.id.labelDescricao);

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

        return convertView;
    }

    public List<ProdutoGrade> getObjects() {
        return objects;
    }

    public void setObjects(List<ProdutoGrade> objects) {
        this.objects = objects;
    }
}
