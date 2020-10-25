package com.vandamodaintima.jfpsb.contador.view.loja;


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
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.Objects;

public class CadastrarLoja extends TelaCadastro {
    private Button btnCadastrar;
    private EditText txtNome;
    private EditText txtCnpj;
    private EditText txtTelefone;
    private EditText txtEndereco;
    private EditText txtInscricaoEstadual;
    private Spinner spinnerMatrizes;

    private CadastrarLojaController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_loja, container, false);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new CadastrarLojaController(this, conexaoBanco);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        txtNome = viewInflate.findViewById(R.id.txtNome);
        txtTelefone = viewInflate.findViewById(R.id.txtTelefone);
        txtEndereco = viewInflate.findViewById(R.id.txtEndereco);
        txtInscricaoEstadual = viewInflate.findViewById(R.id.txtInscricaoEstadual);
        spinnerMatrizes = viewInflate.findViewById(R.id.spinnerMatrizes);

        btnCadastrar.setOnClickListener(view -> {
            controller.getLoja().setCnpj(txtCnpj.getText().toString());
            controller.getLoja().setNome(txtNome.getText().toString().trim().toUpperCase());
            controller.getLoja().setTelefone(txtTelefone.getText().toString().trim());
            controller.getLoja().setEndereco(txtEndereco.getText().toString().trim());
            controller.getLoja().setInscricaoEstadual(txtInscricaoEstadual.getText().toString().trim());
            controller.carregaMatriz(spinnerMatrizes.getSelectedItem());
            controller.salvar();
        });

        ArrayAdapter<Loja> spinnerAdapter = new SpinnerLojaAdapter(Objects.requireNonNull(getContext()), controller.getMatrizes());
        spinnerMatrizes.setAdapter(spinnerAdapter);

        return viewInflate;
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
        txtCnpj.getText().clear();
        spinnerMatrizes.setSelection(0);
        controller.reset();
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
