package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.AlterarDeletarFornecedorController;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

public class AlterarDeletarFornecedor extends TelaAlterarDeletar {

    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtFantasia;
    private FornecedorModel fornecedor;

    private AlterarDeletarFornecedorController alterarDeletarFornecedorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_fornecedor);
        stub.inflate();

        fornecedor = (FornecedorModel) getIntent().getExtras().getSerializable("fornecedor");

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        txtFantasia = findViewById(R.id.txtFantasia);

        inicializaBotoes();

        alterarDeletarFornecedorController = new AlterarDeletarFornecedorController(this);

        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());

        if (!fornecedor.getFantasia().isEmpty()) {
            txtFantasia.setText(fornecedor.getFantasia());
        }
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar FornecedorModel");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o FornecedorModel " + fornecedor.getCnpj() + " - " + fornecedor.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String nome = txtNome.getText().toString().toUpperCase();
                    String fantasia = txtFantasia.getText().toString().toUpperCase();

                    fornecedor.setNome(nome);
                    fornecedor.setFantasia(fantasia);

                    alterarDeletarFornecedorController.atualizar(fornecedor);
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarFornecedor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarFornecedor.this, "FornecedorModel Não Foi Atualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar FornecedorModel");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o FornecedorModel " + fornecedor.getCnpj() + " - " + fornecedor.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alterarDeletarFornecedorController.deletar(fornecedor);
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarFornecedor.this, "FornecedorModel Não Foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
