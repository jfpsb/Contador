package com.vandamodaintima.jfpsb.contador.view.grade;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.grade.CadastrarTipoGradeController;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ICadastrarView;

public class CadastrarTipoGrade extends ActivityBaseView implements ICadastrarView {
    private EditText txtNome;
    private Button btnCadastrar;
    private ConexaoBanco conexaoBanco;
    private CadastrarTipoGradeController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_cadastrar_tipo_grade);
        stub.inflate();

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new CadastrarTipoGradeController(this, conexaoBanco);

        txtNome = findViewById(R.id.txtNome);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            controller.getModel().setNome(txtNome.getText().toString().toUpperCase());
            controller.salvar();
        });
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void focoEmViewInicial() {
        txtNome.requestFocus();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
