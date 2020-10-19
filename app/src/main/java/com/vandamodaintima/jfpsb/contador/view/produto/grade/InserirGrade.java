package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

public class InserirGrade extends TelaCadastro {
    private Button btnInserirFormacaoAtual;
    private Button btnInserir;
    private EditText txtCodBarra;
    private EditText txtPreco;
    private Spinner spinnerTipoGrade;
    private Spinner spinnerGrade;
    private ListView listViewGradeFormacaoAtual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_inserir_produto_grade, container, false);
        return telaCadastroView;
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
        txtPreco.getText().clear();
    }

    @Override
    public void focoEmViewInicial() {
        txtCodBarra.requestFocus();
    }
}
