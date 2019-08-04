package com.vandamodaintima.jfpsb.contador.view.loja;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.loja.CadastrarLojaController;
import com.vandamodaintima.jfpsb.contador.controller.loja.SpinnerLojaAdapter;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.ArrayList;

public class CadastrarLoja extends TelaCadastro {
    private Button btnCadastrar;
    private EditText txtNome;
    private EditText txtCnpj;
    private Spinner spinnerMatrizes;

    private CadastrarLojaController cadastrarLojaController;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_loja, container, false);

        conexaoBanco = new ConexaoBanco(getContext());
        cadastrarLojaController = new CadastrarLojaController(this, conexaoBanco);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        txtNome = viewInflate.findViewById(R.id.txtNome);
        spinnerMatrizes = viewInflate.findViewById(R.id.spinnerMatrizes);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LojaModel loja = new LojaModel(conexaoBanco);

                loja.setCnpj(txtCnpj.getText().toString());
                loja.setNome(txtNome.getText().toString().trim().toUpperCase());

                LojaModel matriz = (LojaModel) spinnerMatrizes.getSelectedItem();
                if (!matriz.getCnpj().equals("0")) {
                    loja.setMatriz(matriz);
                }

                cadastrarLojaController.cadastrar(loja);
            }
        });

        ArrayList<LojaModel> matrizes = new ArrayList<>();
        LojaModel loja = new LojaModel(conexaoBanco);
        loja.setCnpj("0");
        loja.setNome("SEM MATRIZ");
        matrizes.add(loja);
        matrizes.addAll(cadastrarLojaController.getMatrizes());
        ArrayAdapter spinnerAdapter = new SpinnerLojaAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, matrizes);
        spinnerMatrizes.setAdapter(spinnerAdapter);

        return viewInflate;
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
        txtCnpj.getText().clear();
        spinnerMatrizes.setSelection(0);
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void focoEmViewInicial() {
        txtCnpj.requestFocus();
    }
}
