package com.vandamodaintima.jfpsb.contador.view.loja;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.loja.AlterarDeletarLojaController;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

public class AlterarDeletarLoja extends TelaAlterarDeletar {

    private EditText txtCnpj;
    private EditText txtNome;
    private LojaModel lojaModel;
    ConexaoBanco conexaoBanco;
    AlterarDeletarLojaController alterarDeletarLojaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_loja);
        stub.inflate();

        lojaModel = (LojaModel) getIntent().getExtras().getSerializable("loja");

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);

        inicializaBotoes();

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        alterarDeletarLojaController = new AlterarDeletarLojaController(this);

        txtCnpj.setText(String.valueOf(lojaModel.getCnpj()));
        txtNome.setText(lojaModel.getNome());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_alterar_deletar_loja, menu);
        return true;
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Loja");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Apagar a Loja " + lojaModel.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alterarDeletarLojaController.deletar(lojaModel);
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarLoja.this, "Loja Não Foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Loja");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Loja " + lojaModel.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    lojaModel.setNome(txtNome.getText().toString().toUpperCase());

                    alterarDeletarLojaController.atualizar(lojaModel);
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(LOG, e.getMessage(), e);
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarLoja.this, "A Loja Não Foi Alterada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
