package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.marca.AlterarDeletarMarcaController;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

public class AlterarDeletarMarca extends TelaAlterarDeletar {
    private EditText txtNome;
    private MarcaModel marcaModel;
    private AlterarDeletarMarcaController alterarDeletarMarcaController;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_marca);
        stub.inflate();

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        marcaModel = new MarcaModel(conexaoBanco);

        String id = getIntent().getStringExtra("marca");
        marcaModel.load(id);

        txtNome = findViewById(R.id.txtNome);
        alterarDeletarMarcaController = new AlterarDeletarMarcaController(this);
        txtNome.setText(marcaModel.getNome());
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Marca");

        alertBuilderDeletar.setMessage("Tem Certeza que Deseja Deletar a Marca?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alterarDeletarMarcaController.deletar(marcaModel);
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não foi Deletada");
            }
        });
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Marca");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Marca?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String nome = txtNome.getText().toString().toUpperCase();
                    marcaModel.setNome(nome);
                    alterarDeletarMarcaController.atualizar(marcaModel);
                } catch (Exception e) {
                    Log.e(LOG, e.getMessage(), e);
                    Toast.makeText(AlterarDeletarMarca.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não Foi Alterada");
            }
        });
    }
}
