package com.vandamodaintima.jfpsb.contador.view.contagem;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.Date;

public class CadastrarContagem extends TelaCadastro {

    private Button btnCadastrar;
    private Spinner spinnerLoja;

    private Loja loja;

    private SQLiteDatabase sqLiteDatabase;
    private InserirContagemController cadastrarContagemController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        spinnerLoja = view.findViewById(R.id.spinnerLoja);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        cadastrarContagemController = new InserirContagemController(this, sqLiteDatabase, getContext());

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contagem contagem = new Contagem();

                contagem.setData(new Date());
                contagem.setLoja(loja);

                cadastrarContagemController.cadastrar(contagem);
            }
        });

        spinnerLoja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                loja = cadastrarContagemController.retornaLojaEscolhidaSpinner(cnpj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cadastrarContagemController.popularSpinnerLoja();

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
