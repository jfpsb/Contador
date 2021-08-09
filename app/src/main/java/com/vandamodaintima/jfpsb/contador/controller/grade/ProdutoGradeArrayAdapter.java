package com.vandamodaintima.jfpsb.contador.controller.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.List;

public class ProdutoGradeArrayAdapter extends ArrayAdapter<ProdutoGrade> {
    private List<ProdutoGrade> objects;
    private boolean mostrarProduto;

    public ProdutoGradeArrayAdapter(@NonNull Context context, List<ProdutoGrade> objects, boolean mostrarProduto) {
        super(context, 0, objects);
        this.mostrarProduto = mostrarProduto;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesquisa_produto_grade, parent, false);
        }

        ProdutoGrade produtoGrade = objects.get(position);

        final TextView lblCodBarra = convertView.findViewById(R.id.txtCodBarra);
        TextView lblPreco = convertView.findViewById(R.id.txtPreco);
        TextView lblDescricao = convertView.findViewById(R.id.txtDescricaoGrade);

        String descricao = "";

        if (mostrarProduto)
            descricao += produtoGrade.getProduto().getDescricao() + " - ";

        lblCodBarra.setText(produtoGrade.getCodBarra());
        lblPreco.setText(String.valueOf(produtoGrade.getPreco_venda()));

        descricao += produtoGrade.getGradesToSmallString();

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
