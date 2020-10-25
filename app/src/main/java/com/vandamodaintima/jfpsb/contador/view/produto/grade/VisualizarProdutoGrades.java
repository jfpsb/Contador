package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.grade.ProdutoGradeArrayAdapter;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisualizarProdutoGrades extends TelaPesquisa {
    private ProdutoGradeArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizar_produto_grade, container, false);

        listView = view.findViewById(R.id.listViewProdutoGrade);

        arrayAdapter = new ProdutoGradeArrayAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {

    }

    @Override
    public void realizarPesquisa() {
        arrayAdapter.clear();
        Fragment fragment = getActivity().getSupportFragmentManager().getFragments().get(0);
        InserirProdutoGrade inserirProdutoGrade = (InserirProdutoGrade) fragment;
        arrayAdapter.addAll(inserirProdutoGrade.getProdutoGrades());
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume() {
        realizarPesquisa();
        super.onResume();
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {

    }
}