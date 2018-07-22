package com.vandamodaintima.jfpsb.contador.tela.manager;

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

    private String chave; //Chave original do objeto já que ela pode ser alterada

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

        chave = loja.getCnpj();

        setAlertBuilder(loja);
        setBtnAtualizar();
        setBtnDeletar();
    }

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(conn);
    }

    @Override
    protected void setAlertBuilder(final Object loja) {
        final Loja objLoja = (Loja) loja;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Loja");
        builder.setMessage("Tem certeza que deseja apagar a loja " + objLoja.getNome() + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = lojaManager.deletar(objLoja.getCnpj());

                if(result) {
                    Toast.makeText(AlterarDeletarLoja.this, "Loja Deletada Com Sucesso", Toast.LENGTH_SHORT).show();
                    PesquisarLoja.populaListView();
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarLoja.this, "Erro ao Deletar Loja", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarLoja.this, "Loja não foi deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("O campo cnpj não pode estar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("O campo nome não pode estar vazio!");

                    loja.setCnpj(cnpj);
                    loja.setNome(nome.toUpperCase());

                    boolean result = lojaManager.atualizar(loja, chave);

                    if(result) {
                        Toast.makeText(AlterarDeletarLoja.this, "A loja Foi Atualizada Com Sucesso!", Toast.LENGTH_SHORT).show();
                        PesquisarLoja.populaListView();
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

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
