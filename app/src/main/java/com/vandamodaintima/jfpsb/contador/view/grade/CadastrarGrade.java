package com.vandamodaintima.jfpsb.contador.view.grade;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.grade.CadastrarGradeController;
import com.vandamodaintima.jfpsb.contador.controller.grade.SpinnerTipoGradeAdapter;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ICadastrarView;

public class CadastrarGrade extends ActivityBaseView implements ICadastrarView {
    private EditText txtNome;
    private Spinner spinnerTipoGrade;
    private Button btnCadastrar;
    private ConexaoBanco conexaoBanco;
    private CadastrarGradeController controller;
    private SpinnerTipoGradeAdapter spinnerTipoGradeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_cadastrar_grade);
        stub.inflate();

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new CadastrarGradeController(this, conexaoBanco);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        spinnerTipoGrade = findViewById(R.id.spinnerTipoGrade);
        txtNome = findViewById(R.id.txtNome);
        spinnerTipoGradeAdapter = new SpinnerTipoGradeAdapter(getApplicationContext(), controller.getTipoGrades());
        spinnerTipoGrade.setAdapter(spinnerTipoGradeAdapter);

        btnCadastrar.setOnClickListener(v -> {
            controller.getModel().setNome(txtNome.getText().toString().toUpperCase());
            controller.getModel().setTipoGrade((TipoGrade) spinnerTipoGrade.getSelectedItem());
            controller.salvar();
        });
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
        spinnerTipoGrade.setSelection(0);
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
