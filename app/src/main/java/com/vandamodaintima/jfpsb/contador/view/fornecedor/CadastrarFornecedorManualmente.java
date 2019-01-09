package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.CadastrarFornecedorManualmenteController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmente extends ActivityBaseView implements CadastrarView {
    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtFantasia;
    private Button btnCadastrar;

    private SQLiteDatabase sqLiteDatabase;
    private CadastrarFornecedorManualmenteController cadastrarFornecedorManualmenteController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_cadastrar_fornecedor_manualmente);
        stub.inflate();

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        txtFantasia = findViewById(R.id.txtFantasia);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        sqLiteDatabase = new ConexaoBanco(getApplicationContext()).conexao();
        cadastrarFornecedorManualmenteController = new CadastrarFornecedorManualmenteController(this, sqLiteDatabase);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(txtCnpj.getText().toString());
                fornecedor.setNome(txtNome.getText().toString());
                fornecedor.setFantasia(txtFantasia.getText().toString());

                cadastrarFornecedorManualmenteController.cadastrar(fornecedor);
            }
        });
    }

    @Override
    public void limparCampos() {
        txtCnpj.getText().clear();
        txtNome.getText().clear();
        txtFantasia.getText().clear();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {

    }

    @Override
    public void focoEmViewInicial() {
        txtCnpj.requestFocus();
    }
}
