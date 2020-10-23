package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.app.Activity;
import android.content.Intent;
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
    private Button btnLerCodigoBarras;
    private EditText txtCodBarra;
    private EditText txtPreco;
    private Spinner spinnerTipoGrade;
    private Spinner spinnerGrade;
    private ListView listViewGradeFormacaoAtual;

    private static final int TELA_LER_CODIGO_BARRAS = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_inserir_produto_grade, container, false);

        btnLerCodigoBarras = telaCadastroView.findViewById(R.id.btnLerCodigoBarras);
        txtCodBarra = telaCadastroView.findViewById(R.id.txtCodBarra);

        btnLerCodigoBarras.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TelaLerCodigoBarrasCadastrarProduto.class);
            startActivityForResult(intent, TELA_LER_CODIGO_BARRAS);
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TELA_LER_CODIGO_BARRAS) {
            if(resultCode == Activity.RESULT_OK) {
                String codigo = data.getStringExtra("codigo_lido");
                txtCodBarra.setText(codigo);
            }
        }
    }
}
