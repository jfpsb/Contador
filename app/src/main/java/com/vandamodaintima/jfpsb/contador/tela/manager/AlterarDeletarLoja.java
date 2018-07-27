package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarLoja extends AlterarDeletarEntidade {

    private EditText txtCnpj;
    private EditText txtNome;
    private Loja loja;
    private LojaManager lojaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_loja);
        stub.inflate();

        setManagers();

        loja = (Loja) getIntent().getExtras().getSerializable("loja");

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtCnpj.setText(String.valueOf(loja.getCnpj()));
        txtNome.setText(loja.getNome());

        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();
        setBtnAtualizar();
        setBtnDeletar();
    }

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(conn);
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
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Loja");
        alertBuilderDeletar.setMessage("Tem certeza que deseja apagar a loja " + loja.getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = lojaManager.deletar(loja.getCnpj());

                if(result) {
                    Toast.makeText(AlterarDeletarLoja.this, "Loja Deletada Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Deletar Loja", Toast.LENGTH_SHORT).show();
                }
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
    protected void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Loja");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Loja " + loja.getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("O campo cnpj não pode estar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo nome não pode estar vazio!");

                    Loja toUpdate = new Loja();

                    toUpdate.setCnpj(cnpj);
                    toUpdate.setNome(nome);

                    boolean result = lojaManager.atualizar(toUpdate, loja.getCnpj());

                    if(result) {
                        Toast.makeText(AlterarDeletarLoja.this, "A loja Foi Atualizada Com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                    else {
                        Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Contador", e.getMessage(), e);
                }
            }
        });
    }
}
