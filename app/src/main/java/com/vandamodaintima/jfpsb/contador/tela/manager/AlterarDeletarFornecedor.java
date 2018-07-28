package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarFornecedor extends AlterarDeletarEntidade {

    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtFantasia;
    private Fornecedor fornecedor;
    private FornecedorManager fornecedorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_fornecedor);
        stub.inflate();

        fornecedor = (Fornecedor) getIntent().getExtras().getSerializable("fornecedor");

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        txtFantasia = findViewById(R.id.txtFantasia);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtCnpj.setText(fornecedor.getCnpj());
        txtNome.setText(fornecedor.getNome());

        if(fornecedor.getFantasia().isEmpty()) {
            txtFantasia.setText("Não Possui Nome Fantasia");
        }
        else {
            txtFantasia.setText(fornecedor.getFantasia());
        }

        setManagers();

        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();
        setBtnAtualizar();
        setBtnDeletar();
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(conn);
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderAtualizar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Fornecedor");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Fornecedor " + fornecedor.getCnpj() + " - " + fornecedor.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(cnpj))
                        throw new Exception("O campo de cnpj não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo de nome não pode ficar vazio!");

                    Fornecedor toUpdate = new Fornecedor();
                    toUpdate.setCnpj(cnpj);
                    toUpdate.setNome(nome.toUpperCase());

                    boolean result = fornecedorManager.atualizar(toUpdate, fornecedor.getCnpj());

                    if(result) {
                        Toast.makeText(AlterarDeletarFornecedor.this, "O Fornecedor Foi Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                    else {
                        Toast.makeText(AlterarDeletarFornecedor.this, "Erro ao Atualizar Fornecedor", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarFornecedor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Atualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Fornecedor");
        alertBuilderDeletar.setMessage("Tem certeza que deseja deletar o fornecedor " + fornecedor.getCnpj() + " - " + fornecedor.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = fornecedorManager.deletar(fornecedor.getCnpj());

                if(result) {
                    Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Deletado Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarFornecedor.this, "Erro ao Deletar Fornecedor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
