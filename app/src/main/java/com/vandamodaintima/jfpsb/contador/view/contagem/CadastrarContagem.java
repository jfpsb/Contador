package com.vandamodaintima.jfpsb.contador.view.contagem;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.InserirContagemController;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.Date;

public class CadastrarContagem extends TelaCadastro {

    private Button btnCadastrar;
    private Spinner spinnerLoja;
    private InserirContagemController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        spinnerLoja = view.findViewById(R.id.spinnerLoja);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new InserirContagemController(this, conexaoBanco);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.cadastrar();
            }
        });

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                controller.carregaLoja(cnpj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        controller.carregaSpinnerLoja();

        return view;
    }

    @Override
    public void limparCampos() {
        spinnerLoja.setSelection(0);
    }

    @Override
    public void focoEmViewInicial() {
        spinnerLoja.requestFocus();
    }

    public void setSpinnerLojaAdapter(SpinnerAdapter adapter) {
        spinnerLoja.setAdapter(adapter);
    }
}
